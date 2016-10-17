package com.example.mohit.sunshine.app.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.mohit.sunshine.app.MainActivity;
import com.example.mohit.sunshine.app.R;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Mohit on 12-10-2016.
 */
public class MyGcmListenerService extends GcmListenerService {

    public static final String LOG_TAG = MyGcmListenerService.class.getSimpleName();
    public static final String EXTRA_DATA = "data";
    public static final String EXTRA_WEATHER = "weather";
    public static final String EXTRA_LOCATION = "location";
    public static final int NOTIFICATION_ID = 1;

    /**
     * Called when message is received
     * @param from SenderID of the sender
     * @param bundle Bundle containing message data as key/value pairs.
     *               For set of keys use bundle.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        super.onMessageReceived(from, bundle);

        // Time to unparcel the bundle
        if (!bundle.isEmpty()) {
            // Not a bad idea to check that the message is coming from your server.
            if (getString(R.string.gcm_defaultSenderId).equals(from)) {
                // Process message and then post a notification of the received message.
//                    JSONObject jsonObject = new JSONObject(bundle.getString(EXTRA_DATA));
                    String weather = bundle.getString(EXTRA_WEATHER);
                    String location = bundle.getString(EXTRA_LOCATION);
                    String alert = String.format(getString(R.string.gcm_weather_alert), weather, location);

                    sendNotification(alert);
            }
            Log.i(LOG_TAG, "Received: " + bundle.toString());
        }
    }

    /**
     * Put the message into a notification and post it.
     * This is just one simple example of what you might choose to do with the GCM message.
     *
     * @param message The alert message to be posted
     */
    private void sendNotification(String message) {
        //Create explicit intent for our MainActivity
        Intent notifyRegularIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notifyRegularIntent);
        PendingIntent notifyPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Notification using both a large and a small icon (which yours should) need the large
        // icon as a bitmap. So we need to create that here from the resource ID, and pass the
        // object along in our notification builder. Generally, you want to use the app icon
        // as the small icon, so that the users understand what app is triggering this notification.
        Bitmap largeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.art_storm);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.art_clear)
                .setContentTitle("Weather Alert!")
                .setContentText(message)
                .setLargeIcon(largeIcon)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        builder.setContentIntent(notifyPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

}
