package cz.malinajiri.showcase.weatherapp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import cz.malinajiri.showcase.weatherapp.R;
import cz.malinajiri.showcase.weatherapp.fragment.ForecastFragment;
import cz.malinajiri.showcase.weatherapp.fragment.NavigationDrawerFragment;
import cz.malinajiri.showcase.weatherapp.fragment.SettingsFragment;
import cz.malinajiri.showcase.weatherapp.fragment.TodayFragment;
import cz.malinajiri.showcase.weatherapp.listener.OpenFragmentListener;
import cz.malinajiri.showcase.weatherapp.utility.LogUtils;
import cz.malinajiri.showcase.weatherapp.utility.TypefaceUtils;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, OpenFragmentListener {


    private static final String TAG = LogUtils.makeLogTag(MainActivity.class);
    public static final String ARG_SECTION_NUMBER = "section_number";
    /**
     * Depends on the position in the drawer!
     */
    public static final int FRAGMENT_TODAY = 0;
    public static final int FRAGMENT_FORECAST = 1;
    public static final int FRAGMENT_SETTINGS = 2;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        openFragment(position,false);
    }


    @Override
    public void openFragment(int position, boolean addToBackStack) {
        // update the menu_main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        switch (position) {
            case FRAGMENT_TODAY:
                fragment = TodayFragment.newInstance(FRAGMENT_TODAY);
                break;
            case FRAGMENT_FORECAST:
                fragment = ForecastFragment.newInstance(FRAGMENT_FORECAST);
                break;
            case FRAGMENT_SETTINGS:
                fragment = SettingsFragment.newInstance(FRAGMENT_SETTINGS);
                break;

        }
        FragmentTransaction trans = fragmentManager.beginTransaction();
        if (addToBackStack) {
            trans.addToBackStack(null);
        }
        trans.replace(R.id.container, fragment)
                .commit();
    }


    public void onSectionAttached(int number) {
        switch (number) {
            case FRAGMENT_TODAY:
                mTitle = getString(R.string.ab_title_fragment_today);
                break;
            case FRAGMENT_FORECAST:
                mTitle = getString(R.string.ab_title_fragment_forecast);
                break;
            case FRAGMENT_SETTINGS:
                mTitle = getString(R.string.ab_title_fragment_settings);
                break;

            default:
                LogUtils.LOGE(TAG, "onSectionAttached - unknown section");
                break;
        }
    }


    public void restoreActionBar() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        //custom font in action bar title
        int titleId = getResources().getIdentifier("action_bar_title", "id",
                "android");
        if(titleId!=0) {
            TextView yourTextView = (TextView) findViewById(titleId);
            yourTextView.setTypeface(TypefaceUtils.getTypeface("proximanova_semibold.otf", this));
        }
        actionBar.setTitle(mTitle);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public boolean isDrawerOpen(){
        return mNavigationDrawerFragment.isDrawerOpen();

    }


}
