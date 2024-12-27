package com.project.spendly2.services.implementation;



import com.project.spendly2.dto.requests.LoginRequest;
import com.project.spendly2.dto.requests.RegisterDto;
import com.project.spendly2.dto.responses.ApiResponse;
import com.project.spendly2.dto.responses.AuthResponse;
import com.project.spendly2.events.RegistrationCompleteEvent;
import com.project.spendly2.models.entities.Users;
import com.project.spendly2.models.entities.Wallet;
import com.project.spendly2.models.enums.AccountStatus;
import com.project.spendly2.repo.UserRepository;
import com.project.spendly2.repo.WalletRepository;
import com.project.spendly2.security.JwtService;
import com.project.spendly2.services.AuthServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthServices {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ApplicationEventPublisher publisher;
    private final WalletServiceImpl walletService;
    private final OtpService otpService;
    private final WalletRepository walletRepo;





    public ApiResponse<String> registerUser(RegisterDto registerDto, HttpServletRequest request) {

        if (userRepository.findByEmail(registerDto.email()).isPresent()) {
            return new ApiResponse<>("Email is already registered", HttpStatus.BAD_REQUEST);
        }


        if (userRepository.findByUsername(registerDto.nickname()).isPresent()) {
            return new ApiResponse<>("Username already exists", HttpStatus.BAD_REQUEST);
        }


        String pin = createTransactionPin(registerDto.transactionPin());


        Users newUser = Users.builder()
                .username(registerDto.nickname())
                .firstName(registerDto.firstName())
                .lastName(registerDto.lastName())
                .email(registerDto.email())
                .bvn(registerDto.bvn())
                .phonenumber(registerDto.phonenumber())
                .password(passwordEncoder.encode(registerDto.password()))
                .transactionPin(pin)
                .accountStatus(AccountStatus.SUSPENDED) // Default status
                .build();


        String otp = otpService.generateOtp(request.getSession()); // or consider stateless OTP generation


        userRepository.save(newUser);


        publisher.publishEvent(new RegistrationCompleteEvent(newUser, otp));


        return new ApiResponse<>(
                "Check your email for OTP verification",
                "Successfully created account",
                HttpStatus.OK
        );
    }




    public ApiResponse<String> verifyEmail(String email, String otp, HttpSession session){
        Optional<Users> user = userRepository.findByEmail(email);

        if (user.isEmpty()){
            throw new RuntimeException("User not found");
        }

        if (user.get().getAccountStatus().equals(AccountStatus.ACTIVE)) {
            throw new RuntimeException("User is already verified");
        }
        if (otpService.verifyOtp(session, otp)){

            user.get().setAccountStatus(AccountStatus.ACTIVE);
            Wallet wallet = walletRepo.save(walletService.createWallet(user.get()));
            userRepository.save(user.get());
            return new ApiResponse<>(user.get().getFirstName() + " Your email is successfully verified", HttpStatus.OK);
        }
        else
        {
            return new ApiResponse<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ApiResponse<AuthResponse> loginUser(LoginRequest login){

        Optional<Users> user = userRepository.findByEmail(login.email());
        if(user.isPresent() && user.get().getAccountStatus().equals(AccountStatus.ACTIVE)){
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.email(), login.password()));
            String token = jwtService.generateToken(user.get());

            SecurityContextHolder.getContext().setAuthentication(auth);

            AuthResponse authResponse = new AuthResponse(
                    token,
                    user.get().getFirstName(),
                    user.get().getLastName(),
                    user.get().getEmail()
            );
            return new ApiResponse<>("User logged in successfully",HttpStatus.valueOf(200), authResponse);
        }else {
            return new ApiResponse<>("Email is not registered kindly Sign Up", HttpStatus.valueOf(401));
        }

    }


    public String createTransactionPin(String pin) {
        if(pin.length() == 4){
            return passwordEncoder.encode(pin);

        } else {
            return "Pin length must be 4";
        }

    }


    public ApiResponse<String> resendOtp(String email, HttpSession session) {

        Optional<Users> users = userRepository.findByEmail(email);

        if (users.get().getAccountStatus().equals(AccountStatus.SUSPENDED)){
            String otp = otpService.generateOtp(session);
            log.info(otp);
            publisher.publishEvent(new RegistrationCompleteEvent(users.get(), otp));
            return new ApiResponse<>("Your otp has been resent", HttpStatus.OK);
        }else {
            return new ApiResponse<>("Email doesnot  exist",HttpStatus.BAD_REQUEST);
        }

    }


}







