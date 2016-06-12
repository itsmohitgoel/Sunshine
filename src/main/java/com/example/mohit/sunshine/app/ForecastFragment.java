package com.example.mohit.sunshine.app;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mohit.sunshine.app.Utilities.Utility;
import com.example.mohit.sunshine.app.adapters.ForecastAdapter;
import com.example.mohit.sunshine.app.data.WeatherContract;
import com.example.mohit.sunshine.app.listeners.Updatable;
import com.example.mohit.sunshine.app.webservices.FetchWeatherAsyncTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment implements Updatable , LoaderManager.LoaderCallbacks<Cursor>{
    private final String LOG_TAG = ForecastFragment.class.getSimpleName();
    private static final int FORECAST_LOADER  = 0;
    private ForecastAdapter mForecastAdapter;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//        String locationSetting = Utility.getPreferredLocation(getActivity());

        // Create some dummy data for the ListView.
        String[] data = {"Mon 6/23 - Sunny - 31/17",
                "Tue 6/24 - Foggy - 21/8",
                "Wed 6/25 - Cloudy - 22/17",
                "Thurs 6/26 - Rainy - 18/11",
                "Fri 6/27 - Foggy - 21/10",
                "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
                "Sun 6/29 - Sunny - 20/7"};
        List<String> weekForecast = new ArrayList<>(Arrays.asList(data));

        //Initialize adapter
//        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
//                locationSetting, System.currentTimeMillis()
//        );
        // Sort order: Ascending by date.
//        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";

//        Cursor cursor = getActivity().getContentResolver().query(
//                weatherForLocationUri,
//                null,null,null,
//                sortOrder
//        );

        // The CursorAdapter will take data from cursor and populate the ListView
        // However, we can't use FLAG_AUTO_QUERY since its deprecated, so we will
        // end up with empty list the first time we run.
        mForecastAdapter = new ForecastAdapter(getActivity(), null, 0);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public void onWeatherUpdate(List<String> weatherData) {
//        mForecastAdapter.clear();
//        for (String s : weatherData) {
//            mForecastAdapter.add(s);
//        }
    }

    /**
     * Execute async task to download weather data
     * as per the user defined 'location' setting
     */
    private void updateWeather() {
        FetchWeatherAsyncTask weatherTask = new FetchWeatherAsyncTask(getActivity());
        weatherTask.updatableObject = this;
        String locationValue = Utility.getPreferredLocation(getActivity());
        weatherTask.execute(locationValue);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String locationSetting = Utility.getPreferredLocation(getActivity());
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(locationSetting, System.currentTimeMillis());
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";

        CursorLoader cLoader = new CursorLoader(getActivity(),
                weatherForLocationUri,
                null, null, null,
                sortOrder);
        return cLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mForecastAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mForecastAdapter.swapCursor(null);
    }
}
