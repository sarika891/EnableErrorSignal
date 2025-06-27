package com.example.enableerrorsignal.EnableErrorSignal.service;

import com.sun.mail.imap.IMAPFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import java.util.Properties;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailReaderScheduler.class);
    private final GpioServiceInterface gpioService;
    private final Store emailReadingClient;

    public EmailService(GpioServiceInterface gpioService) throws MessagingException {
        this.gpioService = gpioService;
        this.emailReadingClient = getEmailReadingClient();
        this.emailReadingClient.connect();
    }

    private Store getEmailReadingClient() throws NoSuchProviderException {
        return getAuthenticationSession().getStore("imaps");
    }

    public void listenForNewEmails() throws MessagingException {
        log.info("Fetch emails....");
        if (!emailReadingClient.isConnected()) {
            emailReadingClient.connect();
        }
        Folder folder = emailReadingClient.getFolder("INBOX");
        if (folder instanceof IMAPFolder) {
            IMAPFolder inbox = (IMAPFolder) folder;
            inbox.open(Folder.READ_WRITE);

            inbox.addMessageCountListener(new MessageCountListener() {
                @Override
                public void messagesAdded(MessageCountEvent event) {
                    for (Message message : event.getMessages()) {
                        try {
                            log.info("New Email Subject: {}", message.getSubject());
                            gpioService.turnOnRedLight();
                            log.info("Red light blink method triggered.");
                        } catch (MessagingException e) {
                            log.error("Error reading email subject: {}", e.getMessage());
                        }
                    }
                }

                @Override
                public void messagesRemoved(MessageCountEvent event) {
                    // Handle message removal if needed
                }
            });
        }
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