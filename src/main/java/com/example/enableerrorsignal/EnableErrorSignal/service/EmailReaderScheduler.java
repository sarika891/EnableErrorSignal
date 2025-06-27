package com.example.enableerrorsignal.EnableErrorSignal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class EmailReaderScheduler {

    private static final Logger log = LoggerFactory.getLogger(EmailReaderScheduler.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final EmailService emailService;

    public EmailReaderScheduler(EmailService emailService) {
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 30000)
    public void reportCurrentTime() {
        try {
            emailService.listenForNewEmails();
        } catch (MessagingException e) {
            log.error("Reading the email failed with this error: {}", e.getMessage());
        }
    }
}
