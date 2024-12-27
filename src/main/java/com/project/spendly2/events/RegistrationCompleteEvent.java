package com.project.spendly2.events;

import com.project.spendly2.models.entities.Users;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private Users users;
    private String  otp;

    public RegistrationCompleteEvent(Users users, String otp) {
        super(users);
        this.users = users;
        this.otp = otp;
    }
}
