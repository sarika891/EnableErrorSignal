package com.example.enableerrorsignal.EnableErrorSignal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.mail.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final GpioServiceInterface gpioService;

    public EmailService(GpioServiceInterface gpioService) {
        this.gpioService = gpioService;
    }

    public void checkEmail() {
        log.info("===========================================");
        log.info("Checking email...");
        try {
            Store emailReadingClient = getEmailReadingClient();
            Folder inbox = getInboxFolder(emailReadingClient);
            Message[] messages = inbox.getMessages();
            if (didAnyErrorOccurred(messages)) {
                gpioService.turnOnRedLight();
                gpioService.turnOffGreenLight();
                log.info("At least one error occurred");
            } else {
                gpioService.turnOffRedLight();
                gpioService.turnOnGreenLight();
                log.info("No error occurred");
            }
            inbox.close(false);
            emailReadingClient.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("===========================================");
    }

    private boolean didAnyErrorOccurred(Message[] messages) throws MessagingException {
        boolean anErrorOccurred = false;
        for (Message message : messages) {
            if (isMessageRecent(message.getSentDate()) && message.getSubject().contains("Alert")) {
                anErrorOccurred = true;
            }
        }
        return anErrorOccurred;
    }

    private boolean isMessageRecent(Date sentDate) {
        return new Date().toInstant().minus(5, ChronoUnit.MINUTES)
                .isBefore(sentDate.toInstant());
    }

    private Store getEmailReadingClient() throws MessagingException {
        Store store = getAuthenticationSession().getStore("imaps");
        store.connect();
        return store;
    }

    private static Folder getInboxFolder(Store store) throws MessagingException {
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        return inbox;
    }


    private Session getAuthenticationSession() {
        String host = "mail.mailo.com";
        String username = "paleaccidentallyconsidering@mailo.com";
        String password = "hBWA_P492sk:";
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", host);
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.ssl.enable", "true");

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }
}