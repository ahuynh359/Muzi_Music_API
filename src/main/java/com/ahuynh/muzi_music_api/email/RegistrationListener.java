package com.ahuynh.muzi_music_api.email;

import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {


    private final VerificationTokenService verificationTokenService;

    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = getRandomNumberString();
        verificationTokenService.saveVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = token;
//                = event.getAppUrl() + "/auth?token=" + token;
        String message = "Hi ," + user.getUsername() + " please verify opt";
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);


        email.setText(message + "\r\n" + confirmationUrl);
        mailSender.send(email);
    }

    private String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }
}