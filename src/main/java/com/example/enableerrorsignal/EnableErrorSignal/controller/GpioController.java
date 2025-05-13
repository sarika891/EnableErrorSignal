package com.example.enableerrorsignal.EnableErrorSignal.controller;

import com.example.enableerrorsignal.EnableErrorSignal.service.GpioService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ComponentScan
@RequestMapping("/api/gpio")
public class GpioController {

    private final GpioService gpioService;

    public GpioController(GpioService gpioService) {
        this.gpioService = gpioService;
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
}