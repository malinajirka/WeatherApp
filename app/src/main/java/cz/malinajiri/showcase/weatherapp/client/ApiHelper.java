package cz.malinajiri.showcase.weatherapp.client;

import com.android.volley.Request;
import com.android.volley.Response;

import java.util.ArrayList;
import java.util.List;

import cz.malinajiri.showcase.weatherapp.database.model.LocationWeatherDatabaseEntity;
import cz.malinajiri.showcase.weatherapp.entity.gson.ServerForecastEntity;
import cz.malinajiri.showcase.weatherapp.entity.gson.ServerForecastResponse;
import cz.malinajiri.showcase.weatherapp.entity.gson.ServerMultipleWeathersResponse;
import cz.malinajiri.showcase.weatherapp.entity.gson.ServerWeatherEntity;
import cz.malinajiri.showcase.weatherapp.utility.LogUtils;


public class ApiHelper {


    public static void downloadLocationWeather(long id, final Response.Listener<LocationWeatherDatabaseEntity> callback, final Response.ErrorListener errorListener) {
        downloadLocationWeather(UrlHelper.getSingleLocationWeatherUrl(id), callback, errorListener);
    }


    public static void downloadLocationWeather(double lat, double lon, final Response.Listener<LocationWeatherDatabaseEntity> callback, final Response.ErrorListener errorListener) {
        downloadLocationWeather(UrlHelper.getSingleLocationWeatherUrl(lat, lon), callback, errorListener);
    }


    private static void downloadLocationWeather(String url, final Response.Listener<LocationWeatherDatabaseEntity> callback, final Response.ErrorListener errorListener) {

        GsonRequest req = new GsonRequest<ServerWeatherEntity>(Request.Method.GET, url,
                ServerWeatherEntity.class, null, new Response.Listener<ServerWeatherEntity>() {


            @Override
            public void onResponse(ServerWeatherEntity item) {
                callback.onResponse(new LocationWeatherDatabaseEntity(item));
            }


        }, errorListener);

        VolleyHelper.addToRequestQueue(req);
    }


    public static void downloadLocationWeather(List<LocationWeatherDatabaseEntity> items, final Response.Listener<List<LocationWeatherDatabaseEntity>> callback, final Response.ErrorListener errorListener) {

        GsonRequest req = new GsonRequest<ServerMultipleWeathersResponse>(Request.Method.GET, UrlHelper.getMultipleLocationWeatherUrl(items),
                ServerMultipleWeathersResponse.class, null, new Response.Listener<ServerMultipleWeathersResponse>() {


            @Override
            public void onResponse(ServerMultipleWeathersResponse response) {

                ArrayList<LocationWeatherDatabaseEntity> weatherItems = new ArrayList<LocationWeatherDatabaseEntity>();
                for (ServerWeatherEntity item : response.list) {
                    weatherItems.add(new LocationWeatherDatabaseEntity(item));
                }
                callback.onResponse(weatherItems);

            }


        }, errorListener);
        VolleyHelper.addToRequestQueue(req);
    }


    public static void downloadDailyForecast(double lat, double lon, final Response.Listener<List<ServerForecastEntity>> callback, final Response.ErrorListener errorListener) {

        GsonRequest req = new GsonRequest<ServerForecastResponse>(Request.Method.GET, UrlHelper.getDailyForecastUrl(lat, lon),
                ServerForecastResponse.class, null, new Response.Listener<ServerForecastResponse>() {


            @Override
            public void onResponse(ServerForecastResponse response) {

                callback.onResponse(response.list);

            }


        }, errorListener);
        VolleyHelper.addToRequestQueue(req);
    }


}
