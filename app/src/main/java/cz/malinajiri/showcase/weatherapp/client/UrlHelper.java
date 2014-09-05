package cz.malinajiri.showcase.weatherapp.client;

import android.content.Context;

import java.util.List;

import cz.malinajiri.showcase.weatherapp.R;
import cz.malinajiri.showcase.weatherapp.WeatherAppApplication;
import cz.malinajiri.showcase.weatherapp.database.model.LocationWeatherDatabaseEntity;


public class UrlHelper {

    private static final String BASE_URL = WeatherAppApplication.getInstance().getString(R.string.url_base);


    protected static String getSingleLocationWeatherUrl(long id) {
        Context ctx = WeatherAppApplication.getInstance();
        return BASE_URL + ctx.getString(R.string.url_singl_weather_by_id, id);
    }


    protected static String getSingleLocationWeatherUrl(double lat, double lon) {
        Context ctx = WeatherAppApplication.getInstance();
        return BASE_URL + ctx.getString(R.string.url_single_weather_by_location, lat, lon).replace(',', '.');
    }


    protected static String getDailyForecastUrl(double lat, double lon) {
        Context ctx = WeatherAppApplication.getInstance();
        return BASE_URL + ctx.getString(R.string.url_forecast_by_location, lat, lon).replace(',', '.');
    }


    protected static String getMultipleLocationWeatherUrl(List<LocationWeatherDatabaseEntity> items) {
        Context ctx = WeatherAppApplication.getInstance();

        StringBuilder builder = new StringBuilder();

        for (LocationWeatherDatabaseEntity item : items) {
            builder.append(item.getId()).append(",");
        }
        //removes the last comma
        builder.deleteCharAt(builder.length() - 1);

        return BASE_URL + ctx.getString(R.string.url_multiple_weather_by_id, builder.toString());
    }
}
