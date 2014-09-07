package cz.malinajiri.showcase.weatherapp.utility;

import android.content.Context;

import cz.malinajiri.showcase.weatherapp.R;

public class WindDirectionUtils {

    public static String getWindDirectionString(Context ctx, float deg){
        String[] windDirectionStrings = ctx.getResources().getStringArray(R.array.array_wind_direction);
        int windDir = (int)Math.floor((  ((double)deg % 360) / 45.001));
        return windDirectionStrings[windDir];
    }
}
