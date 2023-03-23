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
    private int mainHum;
    private int windSpeed;
    private int cloudsAll;
    private String sysCountry;
    private int sysSunrise;
    private int sysSunset;
    private int timeStamp;
    private String name;
}

