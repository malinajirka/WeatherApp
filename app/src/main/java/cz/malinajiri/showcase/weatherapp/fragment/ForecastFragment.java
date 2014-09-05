package cz.malinajiri.showcase.weatherapp.fragment;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import cz.malinajiri.showcase.weatherapp.R;
import cz.malinajiri.showcase.weatherapp.activity.MainActivity;
import cz.malinajiri.showcase.weatherapp.adapter.DailyForecastAdapter;
import cz.malinajiri.showcase.weatherapp.client.ApiHelper;
import cz.malinajiri.showcase.weatherapp.entity.gson.ServerForecastEntity;
import cz.malinajiri.showcase.weatherapp.listener.OnLocationChangedListener;
import cz.malinajiri.showcase.weatherapp.location.MyLocationProvider;
import cz.malinajiri.showcase.weatherapp.location.exception.LocationNotFoundException;


public class ForecastFragment extends Fragment implements Response.Listener<List<ServerForecastEntity>>, Response.ErrorListener, OnLocationChangedListener {

    private DailyForecastAdapter mAdapter;


    public static ForecastFragment newInstance(int section) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_SECTION_NUMBER, section);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast_list, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mAdapter == null) {
            setupList();
        }

    }


    private void setupList() {
        ListView list = (ListView) getView().findViewById(android.R.id.list);
        list.setEmptyView(getView().findViewById(android.R.id.empty));
        mAdapter = new DailyForecastAdapter(getActivity(), new ArrayList<ServerForecastEntity>());
        list.setAdapter(mAdapter);
    }




    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter == null || mAdapter.isEmpty()) {
            try {
                MyLocationProvider.getInstance().registerLocationListener(this, getActivity());
            } catch (LocationNotFoundException e) {
                locationCannotBeFound();
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        MyLocationProvider.getInstance().unregisterLocationListener(this);
    }


    @Override
    public void locationUpdated(Location newLocation) {
        ApiHelper.downloadDailyForecast(newLocation.getLatitude(), newLocation.getLongitude(), this,this);
        MyLocationProvider.getInstance().unregisterLocationListener(this);
    }


    @Override
    public void googlePlayServicesError() {
        Toast.makeText(getActivity(), R.string.global_google_play_services_error, Toast.LENGTH_SHORT).show();
    }



    private void locationCannotBeFound() {
        Toast.makeText(getActivity(), R.string.global_location_not_found, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResponse(List<ServerForecastEntity> items) {
        for(int i = 0; i < items.size(); i ++){
            items.get(i).setDay(i);
        }
        mAdapter.swapData(items);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        locationCannotBeFound();
    }
}
