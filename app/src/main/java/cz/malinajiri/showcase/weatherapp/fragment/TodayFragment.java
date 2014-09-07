package cz.malinajiri.showcase.weatherapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import cz.malinajiri.showcase.weatherapp.R;
import cz.malinajiri.showcase.weatherapp.activity.LocationListActivity;
import cz.malinajiri.showcase.weatherapp.activity.MainActivity;
import cz.malinajiri.showcase.weatherapp.client.ApiHelper;
import cz.malinajiri.showcase.weatherapp.database.model.LocationWeatherDatabaseEntity;
import cz.malinajiri.showcase.weatherapp.fragment.dialog.ListDialogFragment;
import cz.malinajiri.showcase.weatherapp.listener.OnListDialogItemClickedListener;
import cz.malinajiri.showcase.weatherapp.listener.OnLocationChangedListener;
import cz.malinajiri.showcase.weatherapp.location.MyLocationProvider;
import cz.malinajiri.showcase.weatherapp.location.exception.LocationNotFoundException;
import se.emilsjolander.sprinkles.Query;


public class TodayFragment extends Fragment implements OnListDialogItemClickedListener, OnLocationChangedListener, Response.ErrorListener, Response.Listener<LocationWeatherDatabaseEntity> {

    private static final int REQUEST_CODE_SHARE_DIALOG = 4568;
    private static final String TAG_SHARE_DIALOG = "share_dialog";

    /**
     * Values are tied with values in array.xml -> array_share_dialog
     */
    private static final int SHARE_DIALOG_SHARE = 0;
    private static final int SHARE_DIALOG_SHARE_VIA_EMAIL = 1;
    private static final int SHARE_DIALOG_SHARE_VIA_SMS = 2;

    private LocationWeatherDatabaseEntity entity;


    public static TodayFragment newInstance(int section) {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_SECTION_NUMBER, section);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        setupViews(view);
        return view;
    }


    private void setupViews(View view) {
        view.findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareDialog();
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private void renderView() {
        View parent = getView();
        if (parent == null) {
            return;
        }
        ImageView image = (ImageView) parent.findViewById(R.id.weather_img);
        TextView temp = (TextView) parent.findViewById(R.id.temperature);
        TextView city = (TextView) parent.findViewById(R.id.city_and_country);

        TextView precipitation = (TextView) parent.findViewById(R.id.precipitation);
        TextView humidity = (TextView) parent.findViewById(R.id.humidity);
        TextView pressure = (TextView) parent.findViewById(R.id.pressure);
        TextView wind = (TextView) parent.findViewById(R.id.wind);
        TextView compass = (TextView) parent.findViewById(R.id.compass);

        //dummy image
        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_sun));
        city.setText(entity.getCity() + ", " + entity.getCountry());
        temp.setText(entity.getFormattedTemp() + " | " + entity.getWeather());
        humidity.setText(entity.getFormattedHumidity());
        pressure.setText(entity.getPressure() + " " + getString(R.string.fragment_today_pressure_unit));
        wind.setText(entity.getFormattedWindSpeed());
        compass.setText(entity.getWindDirection(getActivity()));
        //Api doesn't provider information about precipitation
        precipitation.setText(0 + " " + getString(R.string.fragment_today_precipitation_unit));



        parent.findViewById(android.R.id.empty).setVisibility(View.GONE);
        parent.findViewById(R.id.container).setVisibility(View.VISIBLE);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!((MainActivity) getActivity()).isDrawerOpen()) {
            inflater.inflate(R.menu.menu_today, menu);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.location:
                startActivity(new Intent(getActivity(), LocationListActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        DialogFragment shareDialog = (DialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag(TAG_SHARE_DIALOG);
        if (shareDialog != null) {
            shareDialog.setTargetFragment(this, REQUEST_CODE_SHARE_DIALOG);
        }
        if (entity == null) {
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
        DialogFragment shareDialog = (DialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag(TAG_SHARE_DIALOG);
        if (shareDialog != null) {
            shareDialog.setTargetFragment(null, -1);
        }
        MyLocationProvider.getInstance().unregisterLocationListener(this);
    }


    private void showShareDialog() {
        ListDialogFragment.newInstance(R.string.fragment_today_share_dialog_title,
                R.array.array_share_dialog, REQUEST_CODE_SHARE_DIALOG, this)
                .show(getActivity().getSupportFragmentManager(), TAG_SHARE_DIALOG);


    }


    @Override
    public void onDialogItemClicked(int which, int requestCode) {
        if (REQUEST_CODE_SHARE_DIALOG == requestCode) {
            switch (which) {
                case SHARE_DIALOG_SHARE:
                    shareDefault();
                    break;

                case SHARE_DIALOG_SHARE_VIA_EMAIL:
                    shareViaEmail();
                    break;

                case SHARE_DIALOG_SHARE_VIA_SMS:
                    shareViaSMS();
                    break;
            }
        }
    }


    private void shareDefault() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getShareText());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    private void shareViaSMS() {
        Uri uri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", getShareText());
        startActivity(intent);
    }


    private void shareViaEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " " + entity.getCity());
        emailIntent.putExtra(Intent.EXTRA_TEXT, getShareText());
        startActivity(Intent.createChooser(emailIntent, getString(R.string.fragment_today_share_dialog_item_2)));
    }


    private String getShareText() {
        StringBuilder builder = new StringBuilder();
        builder.append("City: ").append(entity.getCity()).append(" ")
                .append("Temperature: ").append(entity.getFormattedTemp()).append(" ")
                .append("Humidity: ").append(entity.getFormattedHumidity()).append(" ")
                .append("Pressure: ").append(entity.getPressure()).append(" ")
                .append("Wind speed: ").append(entity.getFormattedWindSpeed()).append(" ")
                .append("Wind direction: ").append(entity.getWindDirection(getActivity()));
        return builder.toString();
    }


    @Override
    public void locationUpdated(Location newLocation) {
        ApiHelper.downloadLocationWeather(newLocation.getLatitude(), newLocation.getLongitude(), this, this);
        MyLocationProvider.getInstance().unregisterLocationListener(this);
    }


    @Override
    public void googlePlayServicesError() {
        Toast.makeText(getActivity(), R.string.global_google_play_services_error, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResponse(LocationWeatherDatabaseEntity item) {
        entity = item;
        updateCurrentLocationEntityInDatabase();
        renderView();
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        locationCannotBeFound();
    }


    private void locationCannotBeFound() {
        Toast.makeText(getActivity(), R.string.global_location_not_found, Toast.LENGTH_SHORT).show();
    }

    private void updateCurrentLocationEntityInDatabase(){
        //TODO[1] Shouldn't load data from database on the UI thread
        LocationWeatherDatabaseEntity item = Query.one(LocationWeatherDatabaseEntity.class,
                "SELECT * from Weather where "+ LocationWeatherDatabaseEntity.COLUMN_IS_CURRENT_LOCATION +" =?",1)
                .get();
        entity.setIsCurrentLocation(true);

        if(item==null){
            entity.saveAsync();
            return;
        }

        if(item.getId()!=entity.getId()){
            item.setIsCurrentLocation(false);
            item.saveAsync();
            entity.saveAsync();
        }
    }


}
