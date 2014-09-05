package cz.malinajiri.showcase.weatherapp;


import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.malinajiri.showcase.weatherapp.database.model.LocationWeatherDatabaseEntity;
import cz.malinajiri.showcase.weatherapp.entity.gson.ServerWeatherEntity;
import cz.malinajiri.showcase.weatherapp.utility.LogUtils;
import se.emilsjolander.sprinkles.Migration;
import se.emilsjolander.sprinkles.Sprinkles;


public class WeatherAppApplication extends Application {

    private static final String TAG = LogUtils.makeLogTag(WeatherAppApplication.class);

    private static WeatherAppApplication mInstance;
    private WeatherAppConfig conf;


    public WeatherAppApplication() {
        super();
    }


    public static WeatherAppApplication getInstance() {
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        conf = new WeatherAppConfig(this);
        mInstance = this;

        initSprinklesDB();

        //Just for testing purposes
        if(conf.isFirstLoad()){
            new LocationWeatherDatabaseEntity(new ServerWeatherEntity(524901)).saveAsync();
            new LocationWeatherDatabaseEntity(new ServerWeatherEntity(703448)).saveAsync();
            new LocationWeatherDatabaseEntity(new ServerWeatherEntity(2643743)).saveAsync();
            new LocationWeatherDatabaseEntity(new ServerWeatherEntity(5391959)).saveAsync();

            new LocationWeatherDatabaseEntity(new ServerWeatherEntity(4393217)).saveAsync();
            new LocationWeatherDatabaseEntity(new ServerWeatherEntity(5419384)).saveAsync();
            new LocationWeatherDatabaseEntity(new ServerWeatherEntity(4887398)).saveAsync();
            new LocationWeatherDatabaseEntity(new ServerWeatherEntity(5134295)).saveAsync();

            new LocationWeatherDatabaseEntity(new ServerWeatherEntity(2988507)).saveAsync();
            new LocationWeatherDatabaseEntity(new ServerWeatherEntity(3117735)).saveAsync();
            new LocationWeatherDatabaseEntity(new ServerWeatherEntity(2950159)).saveAsync();
            new LocationWeatherDatabaseEntity(new ServerWeatherEntity(3143244)).saveAsync();
        }



    }


    public WeatherAppConfig getConf() {
        return conf;
    }


    private void initSprinklesDB() {
        Sprinkles sprinkles = Sprinkles.init(getApplicationContext());

        sprinkles.addMigration(new Migration() {
            @Override
            protected void onPreMigrate() {
            }

            @Override
            protected void doMigration(SQLiteDatabase db) {
                db.execSQL(
                        "CREATE TABLE Weather ( " +
                                LocationWeatherDatabaseEntity.COLUMN_ID + " INTEGER PRIMARY KEY,"+
                                LocationWeatherDatabaseEntity.COLUMN_COUNTRY + " TEXT,"+
                                LocationWeatherDatabaseEntity.COLUMN_CITY +" TEXT,"+
                                LocationWeatherDatabaseEntity.COLUMN_WEATHER +" TEXT,"+
                                LocationWeatherDatabaseEntity.COLUMN_TEMPERATURE + " REAL,"+
                                LocationWeatherDatabaseEntity.COLUMN_PRESSURE + " REAL,"+
                                LocationWeatherDatabaseEntity.COLUMN_WINDSPEED +  " REAL,"+
                                LocationWeatherDatabaseEntity.COLUMN_WIND_DEGREE + " REAL,"+
                                LocationWeatherDatabaseEntity.COLUMN_LATITUDE + " REAL,"+
                                LocationWeatherDatabaseEntity.COLUMN_LONGITUDE + " REAL,"+
                                LocationWeatherDatabaseEntity.COLUMN_HUMIDITY + " INTEGER,"+
                                LocationWeatherDatabaseEntity.COLUMN_CLOUDS + " INTEGER,"+
                                LocationWeatherDatabaseEntity.COLUMN_IS_CURRENT_LOCATION + " INTEGER"+
                                ")"
                );
            }

            @Override
            protected void onPostMigrate() {
            }
        });
    }


    public Gson getGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }


}
