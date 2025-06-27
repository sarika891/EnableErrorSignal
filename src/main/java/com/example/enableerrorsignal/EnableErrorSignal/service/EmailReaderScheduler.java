package com.example.enableerrorsignal.EnableErrorSignal.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailReaderScheduler {

    private final EmailService emailService;

    public EmailReaderScheduler(final EmailService emailService) {
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 30000, initialDelay = 1000)
    public void checkMailboxPeriodically() {
        emailService.checkEmail();
    }
}
