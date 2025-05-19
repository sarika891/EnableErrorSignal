package com.example.enableerrorsignal.EnableErrorSignal.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import java.util.Properties;

@Service
public class EmailService {

    public boolean checkForAlertEmail() {
        String host = "imap.gmail.com"; // Replace with your mail server
        String username = "sarikachawla89@gmail.com"; // Replace with your email
        String password = "Genpact@12"; // Replace with your password

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

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            for (Message message : messages) {
                if (message.getSubject().contains("Alert")) {
                    return true;
                }
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}