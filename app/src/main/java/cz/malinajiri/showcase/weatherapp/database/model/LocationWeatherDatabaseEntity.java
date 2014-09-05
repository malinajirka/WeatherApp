package cz.malinajiri.showcase.weatherapp.database.model;

import android.content.Context;

import cz.malinajiri.showcase.weatherapp.R;
import cz.malinajiri.showcase.weatherapp.WeatherAppApplication;
import cz.malinajiri.showcase.weatherapp.entity.gson.ServerWeatherEntity;
import cz.malinajiri.showcase.weatherapp.utility.UnitConverter;
import cz.malinajiri.showcase.weatherapp.utility.WindDirectionUtils;
import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Key;
import se.emilsjolander.sprinkles.annotations.Table;


@Table("Weather")
public class LocationWeatherDatabaseEntity extends Model {

    public static final double KILOMETERS_IN_A_MILE = 1.609344;

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_TEMPERATURE = "temperature";
    public static final String COLUMN_HUMIDITY = "humidity";
    public static final String COLUMN_PRESSURE = "pressure";
    public static final String COLUMN_WINDSPEED = "wind_speed";
    public static final String COLUMN_WIND_DEGREE = "wind_degree";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_CLOUDS = "clouds";
    public static final String COLUMN_WEATHER = "weather";
    public static final String COLUMN_IS_CURRENT_LOCATION = "is_current_location";


    @Key
    @Column(COLUMN_ID)
    long id;
    @Column(COLUMN_COUNTRY)
    String country;
    @Column(COLUMN_CITY)
    String city;

    @Column(COLUMN_TEMPERATURE)
    float temp;
    @Column(COLUMN_HUMIDITY)
    int humidity;
    @Column(COLUMN_PRESSURE)
    float pressure;
    @Column(COLUMN_WINDSPEED)
    float windSpeed;
    @Column(COLUMN_WIND_DEGREE)
    float windDegree;
    @Column(COLUMN_LATITUDE)
    double latitude;
    @Column(COLUMN_LONGITUDE)
    double longitude;
    @Column(COLUMN_CLOUDS)
    int clouds;
    @Column(COLUMN_WEATHER)
    String weather;

    @Column(COLUMN_IS_CURRENT_LOCATION)
    int isCurrentLocation;


    public LocationWeatherDatabaseEntity(long id) {
        this.id = id;
    }


    public LocationWeatherDatabaseEntity(ServerWeatherEntity serverLocWeather) {
        id = serverLocWeather.getId();
        city = serverLocWeather.getCity();
        temp = serverLocWeather.getTemp();
        humidity = serverLocWeather.getHumidity();
        pressure = serverLocWeather.getPressure();
        windSpeed = serverLocWeather.getWindSpeed();
        windDegree = serverLocWeather.getWindDegree();
        latitude = serverLocWeather.getLatitude();
        longitude = serverLocWeather.getLongitude();
        clouds = serverLocWeather.getClouds();
        country = serverLocWeather.getCountry();
        weather = serverLocWeather.getWeather();
    }


    public LocationWeatherDatabaseEntity() {
    }


    public String getWindDirection(Context ctx) {
        return WindDirectionUtils.getWindDirectionString(ctx, windDegree);
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getFormattedTemp() {
        return String.format("%.0f", UnitConverter.kelvinToDefaultTemperature(temp, WeatherAppApplication.getInstance())) + "°";
    }


    public void setTemp(float temp) {
        this.temp = temp;
    }


    public String getFormattedHumidity() {
        return humidity + " " + WeatherAppApplication.getInstance().getString(R.string.fragment_today_humidity_unit);
    }


    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }


    public float getPressure() {
        return pressure;
    }


    public void setPressure(float pressure) {
        this.pressure = pressure;
    }


    public String getFormattedWindSpeed() {
        Context ctx = WeatherAppApplication.getInstance();
        String lengthUnit = WeatherAppApplication.getInstance().getConf().getLengthUnit(ctx);
        if (ctx.getString(R.string.pref_speed_unit_kmh).equals(lengthUnit)) {
            return String.format("%.1f " + lengthUnit, windSpeed);
        } else {
            return String.format("%.1f " + lengthUnit, windSpeed / KILOMETERS_IN_A_MILE);
        }


    }


    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }


    public double getLatitude() {
        return latitude;
    }


    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    public double getLongitude() {
        return longitude;
    }


    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public String getCity() {
        return city;
    }


    public void setCity(String city) {
        this.city = city;
    }


    public String getCountry() {
        return country;
    }




    public int getWeatherImageDrawable() {
        //Dummy icon picker
        if (clouds < 50) {
            return R.drawable.ic_sun_location;
        } else if (clouds > 75) {
            return R.drawable.ic_lighning_location;
        } else {
            return R.drawable.ic_wind_location;
        }
    }

    public void setIsCurrentLocation(boolean bool){
        isCurrentLocation = bool ? 1 : 0;
    }


    public boolean isCurrentLocation() {
        return isCurrentLocation == 0 ? false : true;
    }


    public String getWeather() {
        return weather;
    }
}

