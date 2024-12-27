package com.project.spendly2.events.listener;

import com.project.spendly2.events.RegistrationCompleteEvent;
import com.project.spendly2.models.entities.Users;
import com.project.spendly2.services.implementation.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RegistationCompleteListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final EmailService emailService;
    private Users theUser;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        theUser = event.getUsers();
        String token = event.getOtp();

        try {
            sendVerificationEmail(theUser.getEmail(), token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public void sendVerificationEmail(String email, String otp){
       String subject = "Email Verification";
       String body = "<p> Hi, " + theUser.getFirstName() + " , </p>" +
               "<p> Thank you for registering with us at Spendly. </p>" +
               "<p> Please use this otp: <b> "+otp +"</b> </p>"+
               "<p> Enjoy seamless banking experience with Spendly!. Welcome to the family</p>";
       emailService.sendMessage(email, subject, body);
    }
}
