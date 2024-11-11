package com.example.enableerrorsignal.EnableErrorSignal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class EnableErrorSignalController {

    @GetMapping("/monitoring/{ledId}")
    public String ledstatus() {
        return "Here is the status of LED for {ledId} ";
    }

    @GetMapping("/monitoring/{ledId}/ON")
    public String turnledon() {
        return "Turning LED ON";
    }

    @GetMapping("/monitoring/{ledId}/OFF")
    public String turnledoff() {
        return "Turning LED OFF";
    }

}
