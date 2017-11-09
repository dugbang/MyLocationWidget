package com.example.shbae.mylocationwidget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

/**
 * Created by shbae on 2017-11-09.
 */

public class MyLocationProvider extends AppWidgetProvider {

    private static double ycoord;
    private static double xcoord;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for(int i = 0; i < appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];

            String url = "geo: " + ycoord + ", " + xcoord;
            String query = ycoord + ", " + xcoord + "(내위치)";
            String encodedQuery = Uri.encode(query);
            String urlStr = url + "?q=" + encodedQuery + "&z=15";
            Uri uri = Uri.parse(urlStr);

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.mylocation);
            views.setOnClickPendingIntent(R.id.txtInfo, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        context.startService(new Intent(context, GPSLocationService.class));
    }

    private static class GPSLocationService extends Service {
        LocationManager manager;

        @Override
        public void onCreate() {
            super.onCreate();

            manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startListening();

            return super.onStartCommand(intent, flags, startId);
        }

        private void startListening() {
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10000,
                    0,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            ycoord = location.getLongitude();
                            xcoord = location.getLatitude();
                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }

                        @Override
                        public void onProviderEnabled(String s) {

                        }

                        @Override
                        public void onProviderDisabled(String s) {

                        }
                    });
        }

        @Override
        public void onDestroy() {
            stopListening();

            super.onDestroy();
        }

        private void stopListening() {

        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
