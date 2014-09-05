package cz.malinajiri.showcase.weatherapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import cz.malinajiri.showcase.weatherapp.R;
import cz.malinajiri.showcase.weatherapp.WeatherAppApplication;
import cz.malinajiri.showcase.weatherapp.adapter.LocationWeatherAdapter;
import cz.malinajiri.showcase.weatherapp.client.ApiHelper;
import cz.malinajiri.showcase.weatherapp.database.model.LocationWeatherDatabaseEntity;
import cz.malinajiri.showcase.weatherapp.listener.SwipeDismissListViewTouchListener;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.ManyQuery;
import se.emilsjolander.sprinkles.ModelList;
import se.emilsjolander.sprinkles.Query;

public class LocationListFragment extends Fragment implements SwipeDismissListViewTouchListener.DismissCallbacks, Response.Listener<List<LocationWeatherDatabaseEntity>>, Response.ErrorListener {

    private LocationWeatherAdapter mAdapter;
    private ManyQuery.ResultHandler<LocationWeatherDatabaseEntity> mOnDataLoaded = new ManyQuery.ResultHandler<LocationWeatherDatabaseEntity>() {

        @Override
        public boolean handleResult(CursorList<LocationWeatherDatabaseEntity> result) {
            if (result == null) {
                return false;
            }
            if (result.size() == 0) {
                result.close();
                return false;
            }

            //we can't use Cursor loader along with swipe to dismiss gesture.
            ArrayList<LocationWeatherDatabaseEntity> list = new ArrayList<LocationWeatherDatabaseEntity>();
            for (LocationWeatherDatabaseEntity item : result) {
                if (item.isCurrentLocation()) {
                    list.add(0, item);
                } else {
                    list.add(item);
                }
            }
            if (WeatherAppApplication.getInstance().getConf().shouldRefreshMultipleWeatherData()) {
                ApiHelper.downloadLocationWeather(list, LocationListFragment.this, LocationListFragment.this);
            }
            mAdapter.swapData(list);
            result.close();
            return true;
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_list, container, false);
        setupView(view);
        return view;
    }


    private void setupView(View view) {
        Button btnAddLocation = (Button) view.findViewById(R.id.btn_add_location);
        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), R.string.global_not_implemented, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupList();
        if (mAdapter == null || mAdapter.isEmpty()) {
            loadData();
        }


    }


    private void setupList() {
        ListView list = (ListView) getView().findViewById(android.R.id.list);
        list.setEmptyView(getView().findViewById(android.R.id.empty));

        mAdapter = new LocationWeatherAdapter(getActivity(), new ArrayList<LocationWeatherDatabaseEntity>());
        list.setAdapter(mAdapter);
        list.setDivider(getResources().getDrawable(R.drawable.inset_divider_default_list));


        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(list, this);
        list.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        list.setOnScrollListener(touchListener.makeScrollListener());
    }


    private void loadData() {
        Query.all(LocationWeatherDatabaseEntity.class).getAsync(getLoaderManager(), mOnDataLoaded, LocationWeatherDatabaseEntity.class);
    }


    @Override
    public boolean canDismiss(int position) {
        return true;
    }


    @Override
    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            LocationWeatherDatabaseEntity item = mAdapter.getItem(position);
            mAdapter.remove(item);
            item.deleteAsync();
        }
    }


    @Override
    public void onResponse(List<LocationWeatherDatabaseEntity> items) {
        WeatherAppApplication.getInstance().getConf().setMultipleWeatherDataRefreshed();
        mAdapter.swapData(items);
        ModelList<LocationWeatherDatabaseEntity> modelList = new ModelList<LocationWeatherDatabaseEntity>(items);
        modelList.saveAllAsync();
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getActivity(), R.string.global_refresh_data_failed, Toast.LENGTH_SHORT).show();
    }
}



