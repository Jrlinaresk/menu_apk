// File: io/nxlabs/ui/maps/LocationRepository.java
package com.palgao.menu.modules.maps;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class LocationRepository {

    // Actipan 66, Insurgentes Mixcoac, Benito Juárez, 03100 Benito Juárez, CDMX, México

    private static final double STORE_LATITUDE = 19.370095636974632;
    private static final double STORE_LONGITUDE = -99.17971427116413;

    private final LocationManager locationManager;
    private final LocationListener locationListener;
    private static final String TAG = "LocationRepository";
    private LocationCallback locationCallback;

    public LocationRepository(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "Location updated: " + location.getLatitude() + ", " + location.getLongitude());
                if (locationCallback != null) {
                    locationCallback.onLocationReceived(location);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d(TAG, "Provider status changed: " + provider + ", Status: " + status);
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d(TAG, "Provider enabled: " + provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d(TAG, "Provider disabled: " + provider);
                if (locationCallback != null) {
                    locationCallback.onError("Provider disabled: " + provider);
                }
            }
        };
    }

    public Location getStoreLocation() {
        Location storeLocation = new Location("");
        storeLocation.setLatitude(STORE_LATITUDE);
        storeLocation.setLongitude(STORE_LONGITUDE);
        return storeLocation;
    }

    public void requestUserLocation(Context context, final LocationCallback callback) {
        this.locationCallback = callback;
        if (checkLocationPermissions(context)) {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation != null) {
                    callback.onLocationReceived(lastKnownLocation);
                } else {
                    callback.onError("No last known location available.");
                }
            } catch (SecurityException e) {
                Log.e(TAG, "SecurityException: " + e.getMessage());
                callback.onError("SecurityException: " + e.getMessage());
            }
        } else {
            callback.onError("Location permissions are not granted.");
        }
    }

    private boolean checkLocationPermissions(Context context) {
        return ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public interface LocationCallback {
        void onLocationReceived(Location location);
        void onError(String error);
    }
}
