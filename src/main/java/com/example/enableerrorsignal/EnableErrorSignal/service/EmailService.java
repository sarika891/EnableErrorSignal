package com.example.enableerrorsignal.EnableErrorSignal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final GpioServiceInterface gpioService;
    private final Set<String> processedEmails = new HashSet<>();

    public EmailService(GpioServiceInterface gpioService) {
        this.gpioService = gpioService;
    }

   // @Scheduled(initialDelay = 300000, fixedDelay = 300000) // Initial delay of 5 minutes, then runs every 5 minutes
    public void checkEmail() {
        log.info("Scheduled task triggered at: {}", Instant.now());
        log.info("===========================================");
        log.info("Checking email...");
        try {
            Store emailReadingClient = getEmailReadingClient();
            log.info("Connected to email store.");

            Folder inbox = getInboxFolder(emailReadingClient);
            log.info("Inbox folder opened. Total messages: {}", inbox.getMessageCount());

            Message[] messages = inbox.getMessages();
            log.info("Retrieved {} messages from inbox.", messages.length);
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
        } catch (MessagingException e) {
            log.error("MessagingException occurred: {}", e.getMessage());
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
        }
        log.info("===========================================");
    }

    private boolean didAnyErrorOccurred(Message[] messages) throws MessagingException {
        boolean anErrorOccurred = false;

        if (messages.length == 0) {
            log.info("No messages in the inbox.");
            return false;
        }

        // Get the most recent email
        Message latestMessage = messages[messages.length - 1];
        if (!latestMessage.isSet(Flags.Flag.SEEN)) { // Process only unread emails
            String subject = latestMessage.getSubject();
            Date sentDate = latestMessage.getSentDate();
            if (sentDate == null) {
                sentDate = latestMessage.getReceivedDate();
            }

            log.info("Processing latest unread message - Subject: {}, Sent Date: {}", subject, sentDate);

            if (sentDate != null && isMessageRecent(sentDate) && subject != null && subject.contains("Alert")) {
                anErrorOccurred = true;
                log.info("Error email found - Subject: {}, Sent Date: {}", subject, sentDate);
            } else {
                log.info("Message does not meet criteria - Subject: {}, Sent Date: {}", subject, sentDate);
            }

            // Mark the email as read
            latestMessage.setFlag(Flags.Flag.SEEN, true);
            log.info("Marked email as read - Subject: {}", subject);
        } else {
            log.info("Latest message is already read - Subject: {}", latestMessage.getSubject());
        }

        return anErrorOccurred;
    }

    private boolean isMessageRecent(Date sentDate) {
        if (sentDate == null) {
            log.warn("Message sentDate is null, skipping this message.");
            return false;
        }
        Instant messageInstant = sentDate.toInstant();
        Instant fiveMinutesAgo = Instant.now().minus(5, ChronoUnit.MINUTES);

        log.debug("Message sent date: {}, Five minutes ago: {}", messageInstant, fiveMinutesAgo);

        return messageInstant.isAfter(fiveMinutesAgo);
    }

    private static Folder getInboxFolder(Store store) throws MessagingException {
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE); // Change to READ_WRITE to allow marking messages as read
        return inbox;
    }

    private Store getEmailReadingClient() throws MessagingException {
        Store store = getAuthenticationSession().getStore("imaps");
        store.connect();
        return store;
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