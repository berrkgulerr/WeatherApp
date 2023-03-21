package com.example.weatherApp;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Weather {
    private String weatherMain;
    private String weatherDesc;
    private Integer mainTemp;
    private Integer mainFeels;
    private Integer mainPress;
    private Integer mainHum;
    private Integer windSpeed;
    private Integer windDeg;
    private String sysCountry;
    private String name;
}

