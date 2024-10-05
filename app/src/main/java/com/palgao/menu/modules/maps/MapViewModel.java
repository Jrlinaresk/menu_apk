// File: io/nxlabs/ui/maps/MapViewModel.java
package com.palgao.menu.modules.maps;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.osmdroid.util.GeoPoint;

public class MapViewModel extends ViewModel {
    private final LocationRepository locationRepository;
    private final MutableLiveData<Location> storeLocation = new MutableLiveData<>();
    private final MutableLiveData<Location> userLocation = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MapViewModel(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
        storeLocation.setValue(locationRepository.getStoreLocation());
    }

    public LiveData<Location> getStoreLocation() {
        return storeLocation;
    }

    public LiveData<Location> getUserLocation() {
        return userLocation;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void requestUserLocation(Context context) {
        isLoading.setValue(true);
        locationRepository.requestUserLocation(context, new LocationRepository.LocationCallback() {
            @Override
            public void onLocationReceived(Location location) {
                userLocation.setValue(location);
                isLoading.setValue(false);
            }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
                isLoading.setValue(false);
            }
        });
    }

    public Intent getRouteIntent() {
        Location userLoc = userLocation.getValue();

        // para tets
        GeoPoint userGeoPointFake = new GeoPoint(19.36899577932705, -99.178466410177);
        userLoc.setLatitude(userGeoPointFake.getLatitude());
        userLoc.setLongitude(userGeoPointFake.getLongitude());
        //

        Location storeLoc = storeLocation.getValue();

        if (userLoc != null && storeLoc != null) {
            String uri = "http://maps.google.com/maps/dir/?api=1" +
                    "&origin=" + userLoc.getLatitude() + "," + userLoc.getLongitude() +
                    "&destination=" + storeLoc.getLatitude() + "," + storeLoc.getLongitude();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            return intent;
        }
        return null;
    }
}
