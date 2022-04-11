package com.enessimsek.earthquakeproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class EarthquakeProjectApplication {

	public static void main(String[] args) {


		SpringApplication.run(EarthquakeProjectApplication.class, args);
	}

}
