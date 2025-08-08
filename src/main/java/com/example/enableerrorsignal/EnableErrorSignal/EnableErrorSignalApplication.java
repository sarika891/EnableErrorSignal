package com.example.enableerrorsignal.EnableErrorSignal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EnableErrorSignalApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(EnableErrorSignalApplication.class, args);
	}

}
