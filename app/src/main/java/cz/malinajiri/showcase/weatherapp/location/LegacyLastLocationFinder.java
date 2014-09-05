package cz.malinajiri.showcase.weatherapp.location;

/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import cz.malinajiri.showcase.weatherapp.location.exception.LocationNotFoundException;
import cz.malinajiri.showcase.weatherapp.utility.LogUtils;


/**
 * Legacy implementation of Last Location Finder for all Android platforms down
 * to Android 1.6.
 * <p/>
 * This class let's you find the "best" (most accurate and timely) previously
 * detected location using whatever providers are available.
 * <p/>
 * Where a timely / accurate previous location is not detected it will return
 * the newest location (where one exists) and setup a one-off location update to
 * find the current location.
 */

public class LegacyLastLocationFinder implements ILastLocationFinder {

    protected static String TAG = "PreGingerbreadLastLocationFinder";

    protected LocationListener locationListener;
    protected LocationManager locationManager;
    protected Criteria criteria;
    protected Context context;

    protected boolean isWatingForLocation = false;

    protected float minAccuracy;
    protected long minTime;
    /**
     * This one-off {@link android.location.LocationListener} simply listens for a single
     * location update before unregistering itself. The one-off location update
     * is returned via the {@link android.location.LocationListener} specified in
     * {@link }.
     */
    protected MyLocationListener singleUpdateGpsListener;
    protected MyLocationListener singleUpdateNetworkListener;

    /**
     * Construct a new Legacy Last Location Finder.
     *
     * @param context Context
     */
    public LegacyLastLocationFinder(Context context) {
        locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        this.context = context;
    }

    public Location getLastBestLocation(float minAccuracy, long minTime)
            throws LocationNotFoundException {
        long currentTime = System.currentTimeMillis();
        minTime = currentTime - minTime;
        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;
        long bestTime = 0;


        this.minAccuracy = minAccuracy;
        this.minTime = minTime;

        // Iterate through all the providers on the system, keeping
        // note of the most accurate result within the acceptable time limit.
        // If no result is found within maxTime, return the newest Location.
        List<String> matchingProviders = locationManager.getProviders(criteria,false);

        for (String provider : matchingProviders) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                float accuracy = location.getAccuracy();
                long time = location.getTime();

                if ((time > minTime && accuracy < bestAccuracy)) {
                    bestResult = location;
                    bestAccuracy = accuracy;
                    bestTime = time;
                } else if (time < minTime && bestAccuracy == Float.MAX_VALUE
                        && time > bestTime) {
                    bestResult = location;
                    bestTime = time;
                }
            }
        }

        // If the best result is beyond the allowed time limit, or the accuracy
        // of the
        // best result is wider than the acceptable maximum distance, request a
        // single update.
        // This check simply implements the same conditions we set when
        // requesting regular
        // location updates every [minTime] and [minDistance].
        // Prior to Gingerbread "one-shot" updates weren't available, so we need
        // to implement
        // this manually.
        String provider = null;
        if (locationListener != null
                && (bestTime < minTime || bestAccuracy > minAccuracy)
                && !isWatingForLocation) {

            provider = locationManager.getBestProvider(criteria, true);

            if (provider != null) {
                isWatingForLocation = true;
                LogUtils.LOGD("LegacyLocation - requesting location update.");
                if (provider.equals(LocationManager.GPS_PROVIDER)) {
                    singleUpdateGpsListener = new MyLocationListener();
                    locationManager.requestLocationUpdates(provider, 0, 0,
                            singleUpdateGpsListener, context.getMainLooper());

                }
                provider = LocationManager.NETWORK_PROVIDER;
                singleUpdateNetworkListener = new MyLocationListener();
                try {
                    locationManager.requestLocationUpdates(provider, 0, 0,
                            singleUpdateNetworkListener,
                            context.getMainLooper());
                } catch (IllegalArgumentException a) {
                } // Just beacuse emulator issue
            }else{
                LogUtils.LOGD( "LegacyLocation - NO ACCEPTABLE PROVIDER");
            }

        }else{
            LogUtils.LOGD("LegacyLocation - LOCATION IS ACCEPTABLE RIGHT AWAY.");
        }
        if (bestResult == null && provider == null) {
            throw new LocationNotFoundException();
        }

        return bestResult;
    }

    private boolean isLocationAcceptable(Location location) {
        if (location == null) return false;
        if (!location.hasAccuracy() || location.getAccuracy() > minAccuracy) return false;
        if (location.getTime() < minTime) return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void setChangedLocationListener(LocationListener l) {
        locationListener = l;
    }

    ;

    /**
     * {@inheritDoc}
     */
    public void cancel() {
        isWatingForLocation = false;
        if (singleUpdateGpsListener != null)
            locationManager.removeUpdates(singleUpdateGpsListener);
        if (singleUpdateNetworkListener != null)
            locationManager.removeUpdates(singleUpdateNetworkListener);
        locationListener = null;
    }

    private class MyLocationListener implements LocationListener {
        private int counter;

        public void onLocationChanged(Location location) {
            if (!isWatingForLocation) {
                return;
            }
            if(location!=null){
                LogUtils.LOGD("LegacyLocation - Location changed!!!");
            }
            if (locationListener != null && location != null) {
                if (isLocationAcceptable(location)) {
                    locationListener.onLocationChanged(location);
                    cancel();
                    return;
                }
            }
//            counter++;
//            if (locationListener != null && location != null) {
//                if (!location.getProvider().equals(
//                        LocationManager.NETWORK_PROVIDER)
//                        || singleUpdateNetworkListener != null) {
//                    locationListener.onLocationChanged(location);
//                }
//            }
//
//            if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
//                if (counter > 2) {
//                    if (singleUpdateNetworkListener != null) {
//                        locationManager
//                                .removeUpdates(singleUpdateNetworkListener);
//                        singleUpdateNetworkListener = null;
//                    }
//
//                    if (singleUpdateGpsListener != null) {
//                        locationManager.removeUpdates(singleUpdateGpsListener);
//                        singleUpdateGpsListener = null;
//                    }
//                    isWatingForLocation = false;
//                }
//                return;
//            } else if (location.getProvider().equals(
//                    LocationManager.NETWORK_PROVIDER)) {
//                if (counter > 2) {
//                    if (singleUpdateNetworkListener != null) {
//                        locationManager
//                                .removeUpdates(singleUpdateNetworkListener);
//                        singleUpdateNetworkListener = null;
//                    }
//
//                    if (singleUpdateGpsListener == null) {
//                        isWatingForLocation = false;
//                    }
//                }
//            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    }

}
