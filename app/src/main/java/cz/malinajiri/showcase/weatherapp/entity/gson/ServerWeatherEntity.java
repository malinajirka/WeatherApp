package cz.malinajiri.showcase.weatherapp.entity.gson;

import java.util.ArrayList;
import java.util.List;

public class ServerWeatherEntity {

    private long id;
    /**
     * City name
     */
    String name;

    CityLocation coord;

    private Country sys;

    private WeatherMain main;
    private Wind wind;
    private Cloud clouds;

    private List<Weather> weather;


    /**
     * Just for testing purposes.
     * @param id
     */
    public ServerWeatherEntity(long id) {
        this.id = id;
        name = "";
        coord = new CityLocation(0,0);
        sys = new Country("");
        main = new WeatherMain(273.15f,0,0);
        wind = new Wind(0,0);
        clouds = new Cloud(999);
        weather = new ArrayList<Weather>();
        weather.add(new Weather(""));
    }


    public long getId() {
        return id;
    }


    public String getCity() {
        return name;
    }


    public CityLocation getCoord() {
        return coord;
    }


    public float getTemp() {
        return main.temp;
    }


    public int getHumidity() {
        return main.humidity;
    }


    public float getPressure() {
        return main.pressure;
    }


    public float getWindSpeed() {
        return wind.speed;
    }


    public float getWindDegree() {
        return wind.deg;
    }


    public double getLatitude() {
        return coord.lat;
    }


    public double getLongitude() {
        return coord.lon;
    }


    public int getClouds() {
        return clouds.all;
    }

    public String getCountry(){return sys.country;}


    public String getWeather() {
        if(weather!=null && !weather.isEmpty()){
            return weather.get(0).main;
        }
        return "";
    }


    private class WeatherMain {
        private WeatherMain(float temp, int humidity, float pressure) {
            this.temp = temp;
            this.humidity = humidity;
            this.pressure = pressure;
        }
        /**
         * In Kelvin
         */
        float temp;
        int humidity;
        float pressure;
    }

    private class Wind {
        private Wind(float speed, float deg) {
            this.speed = speed;
            this.deg = deg;
        }


        float speed;
        float deg;
    }


    private class Country {
        private Country(String country) {
            this.country = country;
        }


        String country;
    }

    private class CityLocation {
        private CityLocation(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }


        double lat;
        double lon;
    }

    private class Cloud {
        private Cloud(int all) {
            this.all = all;
        }


        /**
         * Cloudiness (%)
         */
        int all;
    }

    private class Weather {
        private Weather(String main) {
            this.main = main;
        }


        String main;
    }
}
