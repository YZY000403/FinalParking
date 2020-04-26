package com.example.cs125final.Logic;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.LocationResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;




// calculate the distance between user's location and the parking's location
public class parkingLots {


    public parkingLots(JsonObject webRecord, GoogleMap map) {

        double curLat = 40.112181;
        double curLng = -88.226932;
        /** the parking info in the jsonObject. */
        // Json parking;

        LatLng curLatLng = new LatLng(curLat, curLng);
        double closerParking = 0.0;

        for (JsonElement y : webRecord.get("parking").getAsJsonArray()) {
            JsonObject parking = y.getAsJsonObject();

            Target allParkingPoint = new Target(map,
                    new LatLng(parking.get("latitude").getAsDouble(),
                            parking.get("longitude").getAsDouble()));

        }


        /**
         * go through all parking space and compare the distance.
         * pick out the closest point and mark it on the map.
         */
            for (JsonElement x : webRecord.get("parking").getAsJsonArray()) {
            JsonObject parking = x.getAsJsonObject();

            LatLng testCoord = new LatLng(parking.get("latitude").getAsDouble(),
                    parking.get("longitude").getAsDouble());
            if (closerParking != 0) {
                if (distanceParking(curLatLng, testCoord) < closerParking) {
                    closerParking = distanceParking(curLatLng, testCoord);
                    Json
                }
            } else {
                closerParking = distanceParking(curLatLng, testCoord);
            }
        }




    }

    /** return the magnitude between the current location and parking location. */
    public double distanceParking(LatLng currentDis, LatLng potParking) {
        double magnitude = Math.abs(Math.sqrt(Math.pow(currentDis.latitude, potParking.latitude)
                + Math.pow(currentDis.longitude, potParking.longitude)));
        return magnitude;
    }

//
//
//    public boolean isParkingWithinRange(LatLng currentLocation, LatLng parkingLocation) {
//        double latDiff = Math.abs(currentLocation.latitude - parkingLocation.latitude);
//        double lngDiff = Math.abs(currentLocation.longitude - parkingLocation.longitude);
//
//        if (Math.sqrt(Math.pow(latDiff, 0) + Math.pow(lngDiff, 0))
//                <= someTestingData.proximityThreshold) {
//            return true;
//        }
//        return false;
//    }
//
//    public void compare(JsonObject parkingInfo, LocationResult current) {
//
//        if ()
//    }


}
