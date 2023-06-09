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
    private String defaultCityName;

    private final RestTemplate restTemplate;

    @Autowired
    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    public Weather getWeather(String city) {
        if (city == null) {
            city = defaultCityName;
        }
        try {
            String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric", city, apiKey);
            String response = restTemplate.getForObject(url, String.class);

            JsonNode root = mapper.readTree(response);

            JsonNode weather = root.get("weather").get(0);
            String weather_desc = weather.get("description").asText();
            String weather_icon = weather.get("icon").asText();

            JsonNode main = root.get("main");
            int main_temp = main.get("temp").asInt();
            int main_feels = main.get("feels_like").asInt();
            int main_tempMin = main.get("temp_min").asInt();
            int main_tempMax = main.get("temp_max").asInt();
            int main_humidity = main.get("humidity").asInt();

            int wind_speed = root.get("wind").get("speed").asInt();

            int clouds_all = root.get("clouds").get("all").asInt();

            JsonNode sys = root.get("sys");
            String sys_country = sys.get("country").asText();
            int sys_sunrise = sys.get("sunrise").asInt();
            int sys_sunset = sys.get("sunset").asInt();

            int timeStamp = root.get("dt").asInt();
            String name = root.get("name").asText();

            return new Weather(weather_desc, weather_icon,
                    main_temp, main_feels, main_tempMin, main_tempMax,
                    main_humidity, wind_speed, clouds_all, sys_country,
                    sys_sunrise, sys_sunset, timeStamp, name);

        } catch (Exception e) {
            return null;
        }
    }
}