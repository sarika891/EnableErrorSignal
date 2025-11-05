package com.example.enableerrorsignal.EnableErrorSignal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class EmailReaderScheduler {

    private final EmailService emailService;
    private final java.util.concurrent.ExecutorService executorService = java.util.concurrent.Executors.newFixedThreadPool(5);
    private static final Logger log = LoggerFactory.getLogger(EmailReaderScheduler.class);

    public EmailReaderScheduler(final EmailService emailService) {
        log.info("EmailReaderScheduler initialized.");
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 30000, initialDelay = 30000)
    public void checkMailboxPeriodically() {
        log.info("check periodic email.");
        executorService.submit(() -> emailService.checkEmail());
    }
}
