package com.example.enableerrorsignal.EnableErrorSignal.service;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.DigitalStateChangeEvent;
import org.springframework.stereotype.Service;

@Service
public class GpioService {

    private final Context pi4j;
    private final DigitalOutput redLed;
    private final DigitalOutput greenLed;

    public GpioService() {
        pi4j = Pi4J.newAutoContext();

     /*   if (!pi4j.registry().exists("gpio")) {
            throw new IllegalStateException("No GPIO chip initialized! Ensure you are running on a Raspberry Pi with proper permissions.");
        }*/
        DigitalOutputConfigBuilder redConfig = DigitalOutput.newConfigBuilder(pi4j)
                .id("red-led")
                .name("Red LED")
                .address(2)
                .shutdown(DigitalState.LOW);

        DigitalOutputConfigBuilder greenConfig = DigitalOutput.newConfigBuilder(pi4j)
                .id("green-led")
                .name("Green LED")
                .address(3)
                .shutdown(DigitalState.LOW);

        redLed = pi4j.create(redConfig);
        greenLed = pi4j.create(greenConfig);
    }

    public void turnOnRedLight() {
        redLed.high();
        greenLed.low();
    }

    public void turnOnGreenLight() {
        greenLed.high();
        redLed.low();
    }
}
