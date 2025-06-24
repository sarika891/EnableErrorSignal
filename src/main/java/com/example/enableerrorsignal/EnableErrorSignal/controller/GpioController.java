package com.example.enableerrorsignal.EnableErrorSignal.controller;

import com.example.enableerrorsignal.EnableErrorSignal.service.EmailService;
import com.example.enableerrorsignal.EnableErrorSignal.service.GpioServiceInterface;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ComponentScan
@RequestMapping("/api/gpio")
public class GpioController {

    private final GpioServiceInterface gpioService;
    private final EmailService emailService;

    public GpioController(GpioServiceInterface gpioService,
                          EmailService emailService) {
        this.gpioService = gpioService;
        this.emailService = emailService;
    }

    @PostMapping("/red/blink")
    public void startRedLight() {
        gpioService.turnOnRedLight();
    }

    @PostMapping("/green/on")
    public void StartGreenLight() {
        gpioService.turnOnGreenLight();
    }

    @PostMapping("/red/off")
    public void turnOffRedLight() {
        gpioService.turnOffRedLight();
    }

    @PostMapping("/green/off")
    public void turnOffGreenLight() {
        gpioService.turnOffGreenLight();
    }

    @GetMapping ("/email")
    public void reademail() {
        // This method is a placeholder for email functionality
        // You can implement the logic to read emails here
       // emailService.checkMailboxPeriodically();
       // System.out.println("Reading email.");
    }
}