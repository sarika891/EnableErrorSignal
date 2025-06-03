package com.example.enableerrorsignal.EnableErrorSignal;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.platform.Platforms;
import com.pi4j.util.Console;
import org.springframework.stereotype.Component;

@Component
public class test {
    Context pi4j = Pi4J.newAutoContext();
    Platforms platforms = pi4j.platforms();
    Console console = new Console();
    public test() {
        console.box("Pi4J PLATFORMS");
        console.println();
        platforms.describe().print(System.out);
        console.println();
    }
}
