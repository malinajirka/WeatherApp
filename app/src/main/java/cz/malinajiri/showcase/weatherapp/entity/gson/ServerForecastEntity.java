package cz.malinajiri.showcase.weatherapp.entity.gson;

import java.util.List;

import cz.malinajiri.showcase.weatherapp.R;
import cz.malinajiri.showcase.weatherapp.WeatherAppApplication;
import cz.malinajiri.showcase.weatherapp.utility.UnitConverter;


public class ServerForecastEntity {

    String day;

    Temperature temp;
    List<Weather> weather;

    int clouds;


    public String getDay() {
        return day;
    }


    public String getWeather() {
        if (weather != null && !weather.isEmpty()) {
            return weather.get(0).main;
        }
        return "";
    }


    public String getFormattedTemp() {
        return String.format("%.0f", UnitConverter.kelvinToDefaultTemperature(temp.day, WeatherAppApplication.getInstance())) + "°";
    }


    public int getWeatherImageDrawable() {
        String weather = getWeather();
        //dummy get drawable
       if(weather.equals("Clouds")){
           return R.drawable.ic_wind_location;
       }else if(weather.equals("Rain")){
           return R.drawable.ic_lighning_location;
       }else{
           return R.drawable.ic_sun_location;
       }
    }


    public void setDay(int dayOffset) {
        day = UnitConverter.getDayString(dayOffset, WeatherAppApplication.getInstance().getResources());
    }


    private class Temperature {
        float day;
    }

    private class Weather {
        String main;
    }


}
