package cz.malinajiri.showcase.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import java.util.Locale;


public class WeatherAppConfig {

    private static final String LAST_LOCATION_LATITUDE = "last_location_latitude";
    private static final String LAST_LOCATION_LONGITUDE = "last_location_longitude";
    private static final String LAST_LOCATION_TIME = "last_location_time";

    private static final String LAST_MULTIPLE_LOCATION_WEATHER_UPDATE = "last_multiple_location_update";
    private static final long MULTIPLE_LOCATION_WEATHER_CACHE = 1000 * 60 * 60;//1 hour
    private static final String FIRST_LOAD = "first_load";

    private SharedPreferences mSettings;


    public WeatherAppConfig(Context gContext) {

        mSettings = PreferenceManager.getDefaultSharedPreferences(gContext);
    }


    public static String getLanguage() {
        return Locale.getDefault().getLanguage();
    }


    private SharedPreferences.Editor getEditor() {
        return mSettings.edit();
    }


    public Location getLastLocation() {
        Location loc = new Location("unknown");
        loc.setTime(1);
        loc.setLatitude(Double.valueOf(mSettings.getString(LAST_LOCATION_LATITUDE, "0.0")));
        loc.setLongitude(Double.valueOf(mSettings.getString(LAST_LOCATION_LONGITUDE, "0.0")));
        loc.setTime(Long.valueOf(mSettings.getString(LAST_LOCATION_TIME, "0")));
        return loc;
    }


    public void setLastLocation(Location lastLocation) {
        SharedPreferences.Editor edit = getEditor();
        edit.putString(LAST_LOCATION_LATITUDE, lastLocation.getLatitude() + "");
        edit.putString(LAST_LOCATION_LONGITUDE, lastLocation.getLongitude() + "");
        edit.putString(LAST_LOCATION_TIME, lastLocation.getTime() + "");
        edit.commit();
    }


    public boolean shouldRefreshMultipleWeatherData() {
        long updated = mSettings.getLong(LAST_MULTIPLE_LOCATION_WEATHER_UPDATE, 1);
        if (System.currentTimeMillis() - updated > MULTIPLE_LOCATION_WEATHER_CACHE) {
            return true;
        }
        return false;
    }


    public void setMultipleWeatherDataRefreshed() {
        SharedPreferences.Editor edit = getEditor();
        edit.putLong(LAST_MULTIPLE_LOCATION_WEATHER_UPDATE, System.currentTimeMillis());
        edit.commit();
    }


    public String getTemperatureUnits(Context ctx) {
        return mSettings.getString(ctx.getString(R.string.pref_key_temp_unit), ctx.getString(R.string.pref_temp_unit_celsius));
    }


    public String getLengthUnit(Context ctx) {
        return mSettings.getString(ctx.getString(R.string.pref_key_speed_unit), ctx.getString(R.string.pref_speed_unit_kmh));
    }


    public boolean isFirstLoad() {
        boolean isFirstLoad = mSettings.getBoolean(FIRST_LOAD, true);
        if (isFirstLoad) {
            SharedPreferences.Editor edit = getEditor();
            edit.putBoolean(FIRST_LOAD, false);
            edit.commit();
        }
        return isFirstLoad;
    }
}