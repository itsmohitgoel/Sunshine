package com.example.mohit.sunshine.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mohit.sunshine.app.Utilities.Utility;

import static com.example.mohit.sunshine.app.data.WeatherContract.WeatherEntry;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private final String FORECAST_SHARE_HASHTAG = "#SunshineApp";
    private String mforecastString;
    private ShareActionProvider mShareActionProvider;
    private static final int DETAIL_FORECAST_LOADER = 0;
    //specify the columns required and utilize the Projection
    private static final String[] FORECAST_COLUMNS = {
            WeatherEntry.TABLE_NAME + "." + WeatherEntry._ID,
            WeatherEntry.COLUMN_DATE,
            WeatherEntry.COLUMN_SHORT_DESC,
            WeatherEntry.COLUMN_MAX_TEMP,
            WeatherEntry.COLUMN_MIN_TEMP
    };

    // Below constants corresponds to the projection defined above, and must change if
    // projection changes
    private static final int COL_WEATHER_ID = 0;
    private static final int COL_WEATHER_DATE = 1;
    private static final int COL_WEATHER_DESC = 2;
    private static final int COL_WEATHER_MIN_TEMP = 3;
    private static final int COL_WEATHER_MAX_TEMP = 4;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
//        Intent incomingIntent = getActivity().getIntent();
//        if (incomingIntent != null ) {
//            mforecastString = incomingIntent.getDataString();
//        }
//
//        TextView tv = (TextView) rootView.findViewById(R.id.textView_detail_text);
//        if(mforecastString != null){
//            tv.setText(mforecastString);
//        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mforecastString != null) {
            createForecastShareIntent(mShareActionProvider);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void createForecastShareIntent(ShareActionProvider actionProvider) {
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_TEXT, mforecastString + FORECAST_SHARE_HASHTAG);
        intentShare.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        if (actionProvider != null) {
            actionProvider.setShareIntent(intentShare);
        } else {
            Log.e(LOG_TAG, "Share Action Provider is null");
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "IN onCreateLoader()-----");
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return  null;
        }

        //Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        Uri detailWeatherUri = intent.getData();
        CursorLoader cursorLoader = new CursorLoader(
                getActivity(),
                detailWeatherUri,
                FORECAST_COLUMNS,
                null,null,null
        );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "In onLoadFinished()");
        if (!data.moveToFirst()) {
            return;
        }

        String dateString = Utility.formatDate(data.getLong(COL_WEATHER_DATE));
        String weatherDescription = data.getString(COL_WEATHER_DESC);
        boolean isMetric = Utility.isMetric(getActivity());
        String high = Utility.formatTemperature(data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
        String low = Utility.formatTemperature(data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);

        mforecastString = String.format("%s - %s -%s/%s", dateString, weatherDescription, high, low);

        TextView detailTextView = (TextView) getView().findViewById(R.id.textView_detail_text);
        detailTextView.setText(mforecastString);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
