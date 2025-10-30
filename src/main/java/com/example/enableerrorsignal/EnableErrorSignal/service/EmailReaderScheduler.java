package com.example.enableerrorsignal.EnableErrorSignal.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Component
public class EmailReaderScheduler {

    private static final String LOCK_FILE_PATH = "/shared/lock/email.lock"; // Shared directory path
    private final EmailService emailService;

    public EmailReaderScheduler(final EmailService emailService) {
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 30000, initialDelay = 30000)
    public void checkMailboxPeriodically() {
        File lockFile = new File(LOCK_FILE_PATH);

        try {
            synchronized (this) {
                int counter = 0;

                // Read the current counter value from the lock file
                if (lockFile.exists()) {
                    List<String> lines = Files.readAllLines(Paths.get(LOCK_FILE_PATH));
                    if (!lines.isEmpty()) {
                        counter = Integer.parseInt(lines.get(0));
                    }
                }

                // Increment the counter and write it back to the lock file
                counter++;
                Files.write(Paths.get(LOCK_FILE_PATH), String.valueOf(counter).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            }

            // Read emails
            emailService.checkEmail();

            synchronized (this) {
                // Read the current counter value from the lock file
                List<String> lines = Files.readAllLines(Paths.get(LOCK_FILE_PATH));
                int counter = Integer.parseInt(lines.get(0));

                // Decrement the counter
                counter--;

                if (counter == 0) {
                    // Delete the lock file if no Raspberry Pis are reading
                    Files.deleteIfExists(Paths.get(LOCK_FILE_PATH));
                } else {
                    // Write the updated counter back to the lock file
                    Files.write(Paths.get(LOCK_FILE_PATH), String.valueOf(counter).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                }
            }
        } catch (IOException e) {
            System.err.println("Error handling lock file: " + e.getMessage());
        }
    }
}
