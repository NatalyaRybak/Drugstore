package com.kmadrugstore.service.impl;

import com.kmadrugstore.service.IEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {

    private final MailSender mailSender;

    private SimpleMailMessage constructMessageToEmail(final String email,
                                                      final String subj,
                                                      final String body) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setSubject(subj);
        mail.setText(body);
        mail.setTo(email);
        return mail;
    }

    @Override
    public void sendMessageToEmail(final String email, final String subj,
                                   final String message) {
        mailSender.send(constructMessageToEmail(email, subj, message));
    }
}