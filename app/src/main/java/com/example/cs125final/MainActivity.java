package com.example.cs125final;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /**
     * .
     */
    private double latitude;
    /**
     * .
     */
    private double longitude;

    private TextView postionView;

    private LocationManager locationManager;
    private String locationProvider;
    private Location location;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);

        Button search = findViewById(R.id.searhHere);
        search.setOnClickListener(v -> {
            JSONObject cloest = cloest();
            TextView it = findViewById(R.id.name);
            it.setText(cloest.getString("name"));
            TextView second = findViewById(R.id.AvailableSpace);
            String putOne = String.valueOf(cloest.getDouble("maxParking") - cloest.getDouble("cars"));
            second.setText(putOne);
            TextView third = findViewById(R.id.Latitude);
            String putTwo = String.valueOf(cloest.getDouble("latitude"));
            third.setText(putTwo);
            TextView fourth = findViewById(R.id.Longitude);
            String putThird = String.valueOf(cloest.getDouble("longitude"));
            fourth.setText(putThird);
            currentLocation();
        });
    }

    public void currentLocation() {
        postionView = (TextView) findViewById(R.id.positionView);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "No Provider", Toast.LENGTH_SHORT).show();
            return;
        }
        System.out.println("Flow");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(locationProvider);
        System.out.println(location.getAltitude());
        if(location!=null){
            showLocation(location);
        }
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
    }

    /**
     * .
     * @param location
     */
    private void showLocation(Location location){
        postionView = findViewById(R.id.positionView);
        String latLongString;
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            latLongString = "Lat:" + lat + " Long:" + lng;
        } else {
            latLongString = "No location found";
        }
        postionView.setText("Your Current Position is: " + latLongString);
    }

    /**
     * LocationListerner
     *
     */

    LocationListener locationListener =  new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                showLocation(location);
            }
        }
    };

    public JSONObject cloest() {
        String it = getJson("ParkingInfo");
        JSONObject current = JSONObject.parseObject(it);
        System.out.println(current);
        JSONArray currentTwo = current.getJSONArray("parking");
        JSONObject first = currentTwo.getJSONObject(0);
        LatLng firstPoint = new LatLng(first.getDouble("latitude"),
                first.getDouble("longitude"));
        System.out.println(firstPoint);
        LatLng currentPosition = new LatLng(40.112234, -88.229973);
        System.out.println(currentPosition);
        double closerParking = 100000000;
        System.out.println(currentTwo.size());
        for (int i = 0; i < currentTwo.size(); i++) {
            JSONObject thisPoint = currentTwo.getJSONObject(i);
            double left = thisPoint.getDouble("maxParking") - thisPoint.getDouble("cars");
            System.out.println("left" + left);
            if (left == 0) {
                System.out.println("it");
                continue;
            } else {
                LatLng thisPosition = new LatLng(thisPoint.getDouble("latitude"),
                        thisPoint.getDouble("longitude"));
                double distance = distance(currentPosition, thisPosition);
                System.out.println("distance" + distance);
                if (distance < closerParking) {
                    closerParking = distance;
                    System.out.println("IT" + i);
                }
            }
            System.out.println("cloest" + closerParking);
        }
        int toReturn = 0;
        for (int i = 0; i < currentTwo.size(); i++) {
            JSONObject itPoint = currentTwo.getJSONObject(i);
            LatLng itPosition = new LatLng(itPoint.getDouble("latitude"),
                    itPoint.getDouble("longitude"));
            if (distance(currentPosition, itPosition) == closerParking) {
                toReturn = i;
                System.out.println("Hello" + toReturn);
            }
        }
        System.out.println(toReturn);
        return currentTwo.getJSONObject(toReturn);
    }

    public String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = MainActivity.this.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
                Log.d("AAA", line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * Computes the distance between two points represented as LatLngs.
     * @param one one latitude-longitude coordinate pair
     * @param another the other latitude-longitude coordinate pair
     * @return the distance between the two points, in meters
     */
    public static double distance(final LatLng one, final LatLng another) {
        final double latDistanceScale = 110574;
        final double lngDistanceScale = 111320;
        final double degToRad = Math.PI / 180;
        double latRadians = degToRad * one.latitude;
        double latDistance = latDistanceScale * (one.latitude - another.latitude);
        double lngDistance = lngDistanceScale * (one.longitude - another.longitude) * Math.cos(latRadians);
        return Math.sqrt(latDistance * latDistance + lngDistance * lngDistance);
    }
}
