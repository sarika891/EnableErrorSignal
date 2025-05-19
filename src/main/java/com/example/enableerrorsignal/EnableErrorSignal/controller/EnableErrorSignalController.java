package com.example.enableerrorsignal.EnableErrorSignal.controller;

import com.example.enableerrorsignal.EnableErrorSignal.service.EmailService;
import com.example.enableerrorsignal.EnableErrorSignal.service.GpioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ComponentScan
@RequestMapping("/api")
public class EnableErrorSignalController {

    @Autowired
    private GpioService gpioService;


    @Autowired
    private EmailService emailService;

    @GetMapping("/check-email")
    public ResponseEntity<String> checkEmailForAlert() {
        boolean isAlert = emailService.checkForAlertEmail();
        if (isAlert) {
            gpioService.turnOnRedLight();
            return ResponseEntity.ok("Alert email detected. Red light turned on.");
        } else {
            gpioService.turnOnGreenLight();
            return ResponseEntity.ok("No alert email detected. Green light turned on.");
        }
    }
}
