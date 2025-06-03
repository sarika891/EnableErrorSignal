package com.example.enableerrorsignal.EnableErrorSignal.controller;

import com.example.enableerrorsignal.EnableErrorSignal.service.EmailService;
import com.example.enableerrorsignal.EnableErrorSignal.service.GpioServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ComponentScan
@RequestMapping("/api")
public class EnableErrorSignalController {

    @Autowired
    private GpioServiceInterface gpioService;


    @Autowired
    private EmailService emailService;

}
