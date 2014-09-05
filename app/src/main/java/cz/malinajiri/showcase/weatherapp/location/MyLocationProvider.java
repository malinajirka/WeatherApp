package cz.malinajiri.showcase.weatherapp.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import java.util.HashSet;
import java.util.Set;

import cz.malinajiri.showcase.weatherapp.R;
import cz.malinajiri.showcase.weatherapp.WeatherAppApplication;
import cz.malinajiri.showcase.weatherapp.listener.OnLocationChangedListener;
import cz.malinajiri.showcase.weatherapp.location.exception.LocationNotFoundException;
import cz.malinajiri.showcase.weatherapp.utility.LogUtils;


public class MyLocationProvider implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, LocationListener, android.location.LocationListener {

    private static final int MAX_LOCATION_TIME_IN_MILLIS = Integer.parseInt(WeatherAppApplication.getInstance().getString(R.string.max_location_age_in_millis));
    private static final int UPDATE_INTERVAL_IN_MILLIS = Integer.parseInt(WeatherAppApplication.getInstance().getString(R.string.location_update_interval_in_millis));
    private static final int REQUESTED_ACCURACY_IN_METERS = Integer.parseInt(WeatherAppApplication.getInstance().getString(R.string.required_accuracy_in_meters));
    private static final int MIN_LOCATION_DISTANCE_IN_METERS = Integer.parseInt(WeatherAppApplication.getInstance().getString(R.string.min_location_distance));

    private static MyLocationProvider instance;
    private final Set<OnLocationChangedListener> subscribers = new HashSet<OnLocationChangedListener>(1);


    private LocationClient mLocationClient;
    private Location location = null;
    private boolean isWaitingForGooglePlayLocationUpdate;

    private LegacyLastLocationFinder locFinder;

    private int unavailableLocationSource = 0;

    private boolean waitingForLocation = false;


    public static MyLocationProvider getInstance() {
        if (instance == null) {
            instance = new MyLocationProvider();
            //This happens when the app is started  (may happen only once in a month)
            instance.location = WeatherAppApplication.getInstance().getConf().getLastLocation();
        }

        return instance;
    }


    public void registerLocationListener(OnLocationChangedListener listener, Context ctx) throws LocationNotFoundException {
        if (isLocationRequestNeeded(location)) {
            subscribers.add(listener);
            try {
                requestNewLocationUpdate(ctx);
            } catch (LocationNotFoundException e) {
                unavailableLocationSource++;
            }
            try {
                requestGooglePlayNewLocationUpdate(ctx);
            } catch (GooglePlayServicesNotAvailableException e) {
                unavailableLocationSource++;
            }
            if (unavailableLocationSource >= 2) {


                throw new LocationNotFoundException();
            } else {
                unavailableLocationSource = 0;
            }
            waitingForLocation = true;
        } else {
            listener.locationUpdated(location);
        }
    }


    public Location getLastLocation() {
        return location;
    }


    private void updateLocation(Location loc) {
        if (loc == null) {
            return;
        }
        Location lastLoc = WeatherAppApplication.getInstance().getConf().getLastLocation();
        WeatherAppApplication.getInstance().getConf().setLastLocation(location);
        location = loc;

        if (lastLoc != null && loc.distanceTo(lastLoc) > MIN_LOCATION_DISTANCE_IN_METERS){
            LogUtils.LOGD("Location   " + loc.getLatitude() + "   " + loc.getLongitude());
            informAllSubscribers();
        }

    }


    private void informAllSubscribers() {
        for (OnLocationChangedListener item : subscribers) {
            item.locationUpdated(location);
        }
        subscribers.clear();
    }


    private void requestNewLocationUpdate(Context ctx) throws LocationNotFoundException {
        if (locFinder == null) {
            locFinder = new LegacyLastLocationFinder(ctx);
        }
        locFinder.setChangedLocationListener(this);
        Location location = locFinder.getLastBestLocation(REQUESTED_ACCURACY_IN_METERS, MAX_LOCATION_TIME_IN_MILLIS);
        if (acceptLocation(location)) {
            return;
        }
    }


    private void requestGooglePlayNewLocationUpdate(Context ctx) throws GooglePlayServicesNotAvailableException {
        if (!isWaitingForGooglePlayLocationUpdate) {
            int resultCode =
                    GooglePlayServicesUtil.
                            isGooglePlayServicesAvailable(ctx);
            // If Google Play services is available
            if (ConnectionResult.SUCCESS == resultCode) {

                if (mLocationClient == null) {
                    mLocationClient = new LocationClient(ctx, this, this);

                }
                if (!mLocationClient.isConnecting() && !mLocationClient.isConnected()) {
                    mLocationClient.connect();
                }
                isWaitingForGooglePlayLocationUpdate = true;
            } else {
                throw new GooglePlayServicesNotAvailableException(0);
            }

        }

    }


    @Override
    public void onConnected(Bundle bundle) {
        Location lastKnownLocation = mLocationClient.getLastLocation();
        if (acceptLocation(lastKnownLocation)) {
            return;
        }

        mLocationClient.requestLocationUpdates(getLocationRequest(), this);


    }


    @Override
    public void onLocationChanged(Location location) {
        if (waitingForLocation && !isLocationRequestNeeded(location)) {
            updateLocation(location);

        }
    }


    private boolean acceptLocation(Location location) {
        if (isLocationRequestNeeded(location)) {
            return false;
        } else {
            onLocationChanged(location);
            return true;
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //ignore
    }


    @Override
    public void onProviderEnabled(String provider) {
        //ignore
    }


    @Override
    public void onProviderDisabled(String provider) {
        //ignore
    }


    @Override
    public void onDisconnected() {
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        errorInformAllSubscribers();

    }


    private void errorInformAllSubscribers() {
        for (OnLocationChangedListener item : subscribers) {
            item.googlePlayServicesError();
        }
    }


    private void unsubscribeAllSubscribers() {
        subscribers.removeAll(subscribers);
        cancelLocationUpdates();
    }


    public void unregisterLocationListener(OnLocationChangedListener listener) {
        subscribers.remove(listener);
        if (subscribers.isEmpty()) {
            cancelLocationUpdates();
        }
    }


    private void cancelLocationUpdates() {
        waitingForLocation = false;
        unregisterLocationFinder();
        unregisterGooglePlayServices();
    }


    private void unregisterLocationFinder() {
        if (locFinder != null) {
            locFinder.cancel();
            locFinder = null;
        }
    }


    private void unregisterGooglePlayServices() {
        isWaitingForGooglePlayLocationUpdate = false;
        if (mLocationClient != null && (mLocationClient.isConnected())) {
            mLocationClient.removeLocationUpdates(this);
            mLocationClient.disconnect();
        }
    }


    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLIS);
        locationRequest.setFastestInterval(UPDATE_INTERVAL_IN_MILLIS);
        return locationRequest;
    }


    private boolean isLocationRequestNeeded(Location location) {
        if (location == null) return true;
        boolean locationIsNotOutdated = System.currentTimeMillis() - location.getTime() < MAX_LOCATION_TIME_IN_MILLIS;
        boolean locationIsAccurateEnough = location.getAccuracy() <= REQUESTED_ACCURACY_IN_METERS;
        if (locationIsNotOutdated && locationIsAccurateEnough) {
            return false;
        }
        return true;
    }
}
