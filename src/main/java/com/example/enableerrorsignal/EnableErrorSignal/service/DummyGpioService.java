package com.example.enableerrorsignal.EnableErrorSignal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("local")
public class DummyGpioService implements GpioServiceInterface{

    private static final Logger log = LoggerFactory.getLogger(DummyGpioService.class);
    public DummyGpioService() {

    }

    public void turnOnRedLight() {
        log.info("Red light turned on");
    }

    public void turnOnGreenLight() {
        log.info("Green light turned on");
    }

    public void turnOffRedLight() {

        log.info("Red light turned off");
    }

    public void turnOffGreenLight() {

        log.info("Green light turned off");
    }
}
