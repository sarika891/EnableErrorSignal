package com.example.enableerrorsignal.EnableErrorSignal.controller;

import com.example.enableerrorsignal.EnableErrorSignal.service.EmailReaderScheduler;
import com.example.enableerrorsignal.EnableErrorSignal.service.GpioServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ComponentScan
@EnableScheduling
@RequestMapping("/api")
public class EnableErrorSignalController {

    @Autowired
    private GpioServiceInterface gpioService;


    @Autowired
    private EmailReaderScheduler emailReaderScheduler;
    //private EmailService emailService;

}
