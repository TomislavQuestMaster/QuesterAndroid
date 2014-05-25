package net.thequester.android.services;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

import net.thequester.android.MainActivity;
import net.thequester.android.R;

/**
 * Author: Tomo
 */
public class LocationService extends IntentService
        implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private NotificationManager notificationManager;

    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(1000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    private LocationClient locationClient;
    private Location destination;
    private double radius;


    public LocationService() {
        super("Location service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        destination = new Location("Destination");
        destination.setLatitude(intent.getDoubleExtra("Latitude", 0));
        destination.setLongitude(intent.getDoubleExtra("Longitude", 0));
        radius = intent.getDoubleExtra("Radius", 0);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        locationClient = new LocationClient(getApplicationContext(), this, this);
        locationClient.connect();

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    @Override
    public void onConnected(Bundle bundle) {
        locationClient.requestLocationUpdates(REQUEST, this);
        Log.d("QuesterLog", "Connected to location client");
    }

    @Override
    public void onDisconnected() {
        Log.d("QuesterLog", "Disconnected from location client");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("QuesterLog", "Connection failed");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onLocationChanged(Location location) {
        Log.d("QuesterLog", "New location " + location);


        if (destination.distanceTo(location) <= radius) {

            Notification notification = new Notification.Builder(this)
                    .setContentTitle("You reached the destination")
                    .setContentText("LAT: " + location.getLatitude() + ", LON: " + location.getLongitude())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0))
                    .setOngoing(true)
                    .build();
            notificationManager.notify(1, notification);

            locationClient.disconnect();
            stopSelf();
        }

    }


}
