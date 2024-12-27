package com.project.spendly2.models.entities;

import com.project.spendly2.models.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity(name = "account_user")
@Getter
@Builder
@Setter
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Users extends BaseEntity implements UserDetails {

    private String firstName;

    private String lastName;

    private String bvn;

    private String username;

    private String email;

    private String password;

   private String phonenumber;

    private String transactionPin;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @OneToMany(mappedBy = "users", cascade = CascadeType.PERSIST)
    private List<Transactions> transactions;

    @OneToMany(mappedBy = "users", cascade = CascadeType.PERSIST)
    private List<Savings> saving;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
