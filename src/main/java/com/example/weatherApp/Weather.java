package com.example.weatherApp;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Weather {
    private String weatherDesc;
    private String weatherIcon;
    private int mainTemp;
    private int mainFeels;
    private int mainTempMin;
    private int mainTempMax;
    private int mainPress;
    private int mainHum;
    private int windSpeed;
    private int windDeg;
    private int cloudsAll;
    private String sysCountry;
    private double sysSunrise;
    private double sysSunset;
    private int timezone;
    private String name;
}

