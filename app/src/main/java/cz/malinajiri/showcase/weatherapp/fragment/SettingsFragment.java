package cz.malinajiri.showcase.weatherapp.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.preferences.PreferenceFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import cz.malinajiri.showcase.weatherapp.R;
import cz.malinajiri.showcase.weatherapp.activity.MainActivity;


public class SettingsFragment extends PreferenceFragment  implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static SettingsFragment newInstance(int section) {
        SettingsFragment fragment = new SettingsFragment();
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
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        addPreferencesFromResource(R.xml.preferences);
        renderView();
    }



    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View view =  super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        ListView list = (ListView) view.findViewById(android.R.id.list);
        list.findViewById(android.R.id.list).setBackgroundColor(Color.WHITE);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePreferenceSummary(sharedPreferences,key);
    }



    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    private void renderView() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        updatePreferenceSummary(sharedPreferences,getString(R.string.pref_key_speed_unit));
        updatePreferenceSummary(sharedPreferences,getString(R.string.pref_key_temp_unit));
    }

    private void updatePreferenceSummary(SharedPreferences sharedPreferences,String key) {
        Preference connectionPref = findPreference(key);
        String summary = sharedPreferences.getString(key, "");
        if(!TextUtils.isEmpty(summary)) {
            connectionPref.setSummary(summary);
        }
    }
}
