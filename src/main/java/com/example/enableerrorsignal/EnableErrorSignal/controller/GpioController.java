package com.example.enableerrorsignal.EnableErrorSignal.controller;

import com.example.enableerrorsignal.EnableErrorSignal.service.GpioService;
import org.springframework.web.bind.annotation.*;

@RestController
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
}