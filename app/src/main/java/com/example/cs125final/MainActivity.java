package com.example.cs125final;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    /**
     * .
     */
    private double latitude;
    /**
     * .
     */
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button search = findViewById(R.id.searhHere);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView it = findViewById(R.id.textView);
                JSONObject cloest = cloest();
                it.setText(cloest.getString("name"));
            }
        });
    }

    public JSONObject cloest() {
        String it = getJson("ParkingInfo");
        JSONObject current = JSONObject.parseObject(it);
        System.out.println(current);
        JSONArray currentTwo = current.getJSONArray("parking");
        JSONObject first = currentTwo.getJSONObject(0);
        LatLng firstPoint = new LatLng(first.getDouble("latitude"),
                first.getDouble("longitude"));
        LatLng currentPosition = new LatLng(latitude, longitude);
        double closerParking = distanceParking(currentPosition, firstPoint);
        for (int i = 0; i < currentTwo.size(); i++) {
            JSONObject thisPoint = currentTwo.getJSONObject(i);
            LatLng thisPosition = new LatLng(thisPoint.getDouble("latitude"),
                    thisPoint.getDouble("longitude"));
            double distance = distanceParking(currentPosition, thisPosition);
            if (distance < closerParking) {
                closerParking = distance;
            }
        }
        JSONObject toReturn = first;
        for (int i = 0; i < currentTwo.size(); i++) {
            JSONObject itPoint = currentTwo.getJSONObject(i);
            LatLng itPosition = new LatLng(itPoint.getDouble("latitude"),
                    itPoint.getDouble("longitude"));
            if (distanceParking(currentPosition, itPosition) == closerParking) {
                toReturn = itPoint;
            }
        }
        return toReturn;
    }

    public void itsLocation(Context c) {
        String itLocation = Context.LOCATION_SERVICE;
        LocationManager manager = (LocationManager) c.getSystemService(itLocation);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
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
        Location location = manager.getLastKnownLocation(locationProvider);
        latitude = location.getLatitude();
        longitude = location.getLongitude();
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

    public double distanceParking(LatLng currentDis, LatLng potParking) {
        double magnitude = Math.abs(Math.sqrt(Math.pow(currentDis.latitude, potParking.latitude)
                + Math.pow(currentDis.longitude, potParking.longitude)));
        return magnitude;
    }
}
