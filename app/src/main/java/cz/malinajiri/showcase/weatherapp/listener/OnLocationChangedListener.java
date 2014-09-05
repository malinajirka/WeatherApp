package cz.malinajiri.showcase.weatherapp.listener;

import android.location.Location;

public interface OnLocationChangedListener {

    public void locationUpdated(Location newLocation);

    public void googlePlayServicesError();
}
