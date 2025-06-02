package com.example.enableerrorsignal.EnableErrorSignal.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("local")
public class DummyGpioService implements GpioServiceInterface{


    public DummyGpioService() {

    }

    public void turnOnRedLight() {
        System.out.println("Red light turned on");
    }

    public void turnOnGreenLight() {
        System.out.println("Red light turned on");
    }

    public void turnOffRedLight() {

        System.out.println("Red light turned of");
    }

    public void turnOffGreenLight() {

        System.out.println("Red light turned off");
    }
}
