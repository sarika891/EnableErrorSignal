package com.example.enableerrorsignal.EnableErrorSignal.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailReaderScheduler {

    private final EmailService emailService;
    private final java.util.concurrent.ExecutorService executorService = java.util.concurrent.Executors.newFixedThreadPool(5);

    public EmailReaderScheduler(final EmailService emailService) {
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 30000, initialDelay = 30000)
    public void checkMailboxPeriodically() {
        executorService.submit(() -> emailService.checkEmail());
    }
}
