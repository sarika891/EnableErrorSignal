package com.example.enableerrorsignal.EnableErrorSignal.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.*;
import java.util.Properties;

@Service
public class EmailService {

    private final GpioServiceInterface gpioService;

    public EmailService(GpioServiceInterface gpioService) {
        this.gpioService = gpioService;
    }

    @Scheduled(fixedRate = 60000) // Runs every 60 seconds
    public void checkMailboxPeriodically() {
        String host = "imap.mail.yahoo.com";
        String username = "sarikachawla23";
        String password = "Nushku@12";

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

            System.out.println("Connecting to email server...");

            Store store = session.getStore("imaps");
            store.connect();

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            for (Message message : messages) {
                if (message.getSubject().contains("Alert")) {
                    gpioService.turnOnRedLight();
                    break;
                }
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}