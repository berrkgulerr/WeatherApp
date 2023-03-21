package com.example.weatherApp;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.city}")
    private String city;

    private final RestTemplate restTemplate;

    @Autowired
    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    public Weather getWeather() {
        try {
            String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", city, apiKey);
            String response = restTemplate.getForObject(url, String.class);

            JsonNode root = mapper.readTree(response);
            JsonNode weather = root.get("weather").get(0);
            String weather_main = weather.get("main").asText();
            String weather_desc = weather.get("description").asText();

            JsonNode main = root.get("main");
            Integer main_temp = main.get("temp").asInt();
            Integer main_feels = main.get("feels_like").asInt();
            Integer main_pressure = main.get("pressure").asInt();
            Integer main_humidity = main.get("humidity").asInt();

            JsonNode wind = root.get("wind");
            Integer wind_speed = wind.get("speed").asInt();
            Integer wind_deg = wind.get("deg").asInt();

            String sys_country = root.get("sys").get("country").asText();
            String name = root.get("name").asText();

            return new Weather(weather_main, weather_desc, main_temp, main_feels, main_pressure, main_humidity,
                    wind_speed, wind_deg, sys_country, name);
        } catch (Exception e) {
            return null;
        }
    }
}