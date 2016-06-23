package com.example.mohit.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mohit.sunshine.app.Utilities.Utility;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private String mLocation ;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLocation = Utility.getPreferredLocation(this);

        if (findViewById(R.id.weather_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present , then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a fragment
            // transaction.
            if (savedInstanceState == null) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.add(R.id.weather_detail_container, new DetailActivityFragment());
                transaction.commit();
            }
        }else {
            mTwoPane = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intentSettings = new Intent(getApplication(), SettingsActivity.class);
            startActivity(intentSettings);
            return true;
        }

        if (id == R.id.action_map) {
            openPreferredLocationMap();
        }

        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationMap() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String location = sharedPreferences.getString(
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default)
        );

        Uri baseUri = Uri.parse("geo:0,0?");
        Uri.Builder builder = baseUri.buildUpon();
        Uri finalUri = builder.appendQueryParameter("q", location).build();

        Intent intentMap = new Intent(Intent.ACTION_VIEW);
        intentMap.setData(finalUri);
        if (intentMap.resolveActivity(getPackageManager()) != null) {
            startActivity(intentMap);
        }else{
            Log.d(LOG_TAG, "couldn't call " + location + ", no receiving apps installed");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String location = Utility.getPreferredLocation(this);
        //update the location in our second pane using the Fragment Manager
        if (location != null && (!location.equals(mLocation))) {
            ForecastFragment ff = (ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            if (ff != null) {
                ff.onLocationChanged();
            }
            mLocation = location;
        }
    }
}
