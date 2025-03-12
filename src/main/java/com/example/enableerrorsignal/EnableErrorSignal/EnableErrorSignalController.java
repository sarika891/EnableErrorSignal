package com.example.enableerrorsignal.EnableErrorSignal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class EnableErrorSignalController {

    @GetMapping("/monitoring/{id}")

    public String ledstatus(@PathVariable String id , Model model) {
        model.addAttribute("id", ledId);
        return "Here is the status of LED with id" + ledId;
    }

    @GetMapping("/monitoring/{ledId}/ON")
    public String turnledon(@PathVariable String ledId) {
        return "Turning LED ON";
    }

    @GetMapping("/monitoring/{ledId}/OFF")
    public String turnledoff(@PathVariable String ledId) {
        return "Turning LED OFF";
    }

}
