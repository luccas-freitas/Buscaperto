package com.luccas.buscaperto.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.auth.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.luccas.buscaperto.model.offer.Offer;
import com.luccas.buscaperto.ui.ProductDetailActivity;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

public class LocationMonitoringService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();
    private DatabaseReference mDatabase;

    public static final int LOCATION_INTERVAL = 10000;
    public static final int FASTEST_LOCATION_INTERVAL = 5000;

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest.setInterval(LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_LOCATION_INTERVAL);

        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY;

        mLocationRequest.setPriority(priority);
        mLocationClient.connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mDatabase = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("user-favorites" + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(Bundle dataBundle) {
        if (ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng from = new LatLng(location.getLatitude(), location.getLongitude());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    Offer offer = child.getValue(Offer.class);

                    Double x = Double.parseDouble(offer.getStore().getCoordinates().getX());
                    Double y = Double.parseDouble(offer.getStore().getCoordinates().getY());
                    LatLng to = new LatLng(x, y);

                    Double distance = SphericalUtil.computeDistanceBetween(from, to);

                    if(distance < 50) {
                        String name;
                        if(offer.getProduct().getShortName() != null)
                            name = offer.getProduct().getShortName();
                        else
                            name = offer.getName();

                        String notification = name +
                                " está a aproximadamente " +
                                distance.intValue() +
                                " metros de você!";

                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(getApplicationContext(), "")
                                        .setSmallIcon(R.drawable.notification_icon)
                                        .setContentTitle("Buscaperto")
                                        .setContentText(getString(R.string.remember))
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText(notification))
                                        .setPriority(Notification.PRIORITY_MAX)
                                        .setAutoCancel(true)
                                        .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE);

                        Intent resultIntent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                        resultIntent.putExtra(ProductDetailActivity.CURRENT_OFFER, offer);

                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                        // Adds the back stack for the Intent (but not the Intent itself)
                        stackBuilder.addParentStack(ProductDetailActivity.class);
                        // Adds the Intent that starts the Activity to the top of the stack
                        stackBuilder.addNextIntent(resultIntent);
                        PendingIntent resultPendingIntent =
                                stackBuilder.getPendingIntent(
                                        0,
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                );
                        mBuilder.setContentIntent(resultPendingIntent);
                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        mNotificationManager.notify(0, mBuilder.build());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
}