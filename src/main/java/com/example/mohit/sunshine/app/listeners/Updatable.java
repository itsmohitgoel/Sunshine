package com.example.mohit.sunshine.app.listeners;

import java.util.List;

/**
 * Interface with callback methods for communication  b/w AsyncTask
 * and fragments.
 * Created by Mohit on 12-05-2016.
 */
public interface Updatable {
    /**
     * Called mostly on Fragment object, to update its
     * UI
     * @param weatherData
     */
    public abstract void onWeatherUpdate(List<String> weatherData);
}
