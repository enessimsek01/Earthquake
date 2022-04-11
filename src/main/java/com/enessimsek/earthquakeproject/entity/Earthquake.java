package com.enessimsek.earthquakeproject.service;


import com.enessimsek.earthquakeproject.dto.EarthquakeDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class EarthquakeService {

    public List<EarthquakeDto> findAll(){
        String url="https://restcountries.com/v3.1/name/all";
        RestTemplate restTemplate=new RestTemplate();
        return Arrays.asList(restTemplate.getForObject(url,EarthquakeDto[].class));
    }

   /* public EarthquakeDto findByNameAndDays(){

    }*/
}
