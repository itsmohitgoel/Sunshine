package com.example.mohit.sunshine.app.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Mohit on 08-10-2016.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {
    public static final String LOG_TAG = "MyInstanceIdLS";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by
     * the InstanceID provider.
     */
    @Override
    public void onTokenRefresh() {
        // Fetch updated instance ID token.
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
