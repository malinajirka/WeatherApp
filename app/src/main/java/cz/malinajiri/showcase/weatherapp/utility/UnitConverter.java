package cz.malinajiri.showcase.weatherapp.utility;

import android.content.Context;
import android.content.res.Resources;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cz.malinajiri.showcase.weatherapp.R;
import cz.malinajiri.showcase.weatherapp.WeatherAppApplication;


public class UnitConverter {
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final float KELVIN_TO_CELSIUS_SUB = 273.15f;

    public static final float kelvinToDefaultTemperature(float kelvinTemp, Context ctx){
        String tempUnits = WeatherAppApplication.getInstance().getConf().getTemperatureUnits(ctx);
        float result;
        if (ctx.getString(R.string.pref_temp_unit_celsius).equals(tempUnits)) {
            result = kelvinTemp - KELVIN_TO_CELSIUS_SUB;
        } else {
            result = (kelvinTemp - KELVIN_TO_CELSIUS_SUB) * 1.8f + 32f;
        }
        return result;
    }


    public static final String getDayString(int dayOffset, Resources res){
        GregorianCalendar cal = new GregorianCalendar();

        cal.setTime(new Date(System.currentTimeMillis() + DAY_IN_MILLIS * dayOffset));

        int dow = cal.get(Calendar.DAY_OF_WEEK);

        switch (dow) {
            case Calendar.MONDAY:
                return res.getString(R.string.global_monday);
            case Calendar.TUESDAY:
                return res.getString(R.string.global_tuesday);
            case Calendar.WEDNESDAY:
                return res.getString(R.string.global_wednesday);
            case Calendar.THURSDAY:
                return res.getString(R.string.global_thursday);
            case Calendar.FRIDAY:
                return res.getString(R.string.global_friday);
            case Calendar.SATURDAY:
                return res.getString(R.string.global_saturday);
            case Calendar.SUNDAY:
                return res.getString(R.string.global_sunday);
        }

        return "Unknown";
    }

}
