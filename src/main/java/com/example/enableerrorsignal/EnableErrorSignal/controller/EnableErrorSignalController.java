package com.example.enableerrorsignal.EnableErrorSignal.controller;

import com.example.enableerrorsignal.EnableErrorSignal.model.Alert;
import com.example.enableerrorsignal.EnableErrorSignal.service.GpioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ComponentScan
@RequestMapping("/api")
public class EnableErrorSignalController {

    @Autowired
    private GpioService gpioService;

    @PostMapping("/alert")
    public ResponseEntity<String> handleAlert(@RequestBody Alert alert) {
        if (alert.isError()) {
            gpioService.turnOnRedLight();
        } else {
            gpioService.turnOnGreenLight();
        }
        return ResponseEntity.ok("Alert received");
    }
}
