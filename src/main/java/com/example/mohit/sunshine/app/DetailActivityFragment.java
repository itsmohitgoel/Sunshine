package com.example.mohit.sunshine.app;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private final String FORECAST_SHARE_HASHTAG = "#SunshineApp";
    private String forecastString;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent incomingIntent = getActivity().getIntent();
        if (incomingIntent != null && incomingIntent.hasExtra(Intent.EXTRA_TEXT)) {
            forecastString = incomingIntent.getExtras().getString(Intent.EXTRA_TEXT);
        }

        TextView tv = (TextView) rootView.findViewById(R.id.textView_detail_text);
        if(forecastString != null){
            tv.setText(forecastString);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        ShareActionProvider actionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        createForecastShareIntent(actionProvider);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void createForecastShareIntent(ShareActionProvider actionProvider) {
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_TEXT, forecastString + FORECAST_SHARE_HASHTAG);
        intentShare.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        if (actionProvider != null) {
            actionProvider.setShareIntent(intentShare);
        } else {
            Log.e(LOG_TAG, "Share Action Provider is null");
        }

    }
}
