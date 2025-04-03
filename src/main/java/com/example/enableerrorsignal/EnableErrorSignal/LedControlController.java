package com.example.enableerrorsignal.EnableErrorSignal;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalState;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LedControlController {

    private final Context pi4j;
    private final DigitalOutput redLed;
    private final DigitalOutput greenLed;

    public LedControlController() {
        // Create Pi4J context
        this.pi4j = Pi4J.newAutoContext();

        // Provision GPIO pins for the LEDs
        DigitalOutputConfigBuilder redLedConfig = DigitalOutput.newConfigBuilder(pi4j)
                .id("red-led")
                .name("Red LED")
                .address(1) // GPIO pin number for red LED
                .shutdown(DigitalState.LOW)
                .initial(DigitalState.LOW);

        DigitalOutputConfigBuilder greenLedConfig = DigitalOutput.newConfigBuilder(pi4j)
                .id("green-led")
                .name("Green LED")
                .address(2) // GPIO pin number for green LED
                .shutdown(DigitalState.LOW)
                .initial(DigitalState.LOW);

        this.redLed = pi4j.create(redLedConfig);
        this.greenLed = pi4j.create(greenLedConfig);
    }

    @GetMapping("/led/{color}/{state}")
    public String controlLed(@PathVariable String color, @PathVariable String state) {
        DigitalOutput led = "red".equalsIgnoreCase(color) ? redLed : greenLed;
        if ("on".equalsIgnoreCase(state)) {
            led.high();
            return color + " LED is ON";
        } else if ("off".equalsIgnoreCase(state)) {
            led.low();
            return color + " LED is OFF";
        } else {
            return "Invalid state. Use 'on' or 'off'.";
        }
    }
}