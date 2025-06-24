package com.example.enableerrorsignal.EnableErrorSignal.service;

import com.sun.mail.imap.IMAPFolder;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class EmailService {

    private final GpioServiceInterface gpioService;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public EmailService(GpioServiceInterface gpioService) {
        this.gpioService = gpioService;
    }

    @PostConstruct
    public void startEmailListenerInBackground() {
        executorService.submit(() -> {
            try {
                listenForNewEmails();
            } catch (MessagingException e) {
                System.err.println("Error starting email listener: " + e.getMessage());
            }
        });
    }

    public void listenForNewEmails() throws MessagingException {
        String host = "mail.mailo.com";
        String username = "paleaccidentallyconsidering@mailo.com";
        String password = "hBWA_P492sk:";
        System.out.println("Starting email listener...");
        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imaps");
            properties.put("mail.imaps.host", host);
            properties.put("mail.imaps.port", "993");
            properties.put("mail.imaps.ssl.enable", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Store store = session.getStore("imaps");
            store.connect();

            Folder folder = store.getFolder("INBOX");
            if (folder instanceof IMAPFolder) {
                IMAPFolder inbox = (IMAPFolder) folder;
                inbox.open(Folder.READ_WRITE);

                inbox.addMessageCountListener(new MessageCountListener() {
                    @Override
                    public void messagesAdded(MessageCountEvent event) {
                        for (Message message : event.getMessages()) {
                            try {
                                System.out.println("New Email Subject: " + message.getSubject());
                                gpioService.turnOnRedLight();
                                System.out.println("Red light blink method triggered.");
                            } catch (MessagingException e) {
                                System.err.println("Error reading email subject: " + e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void messagesRemoved(MessageCountEvent event) {
                        // Handle message removal if needed
                    }
                });

                System.out.println("Listening for new emails...");
                try {
                    while (true) {
                        inbox.idle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (MessagingException e) {
            System.err.println("Error connecting to email server: " + e.getMessage());
        }
    }
}