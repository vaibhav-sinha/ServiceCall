package com.servicecall.app.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Vaibhav on 6/14/2015.
 */
public class GoogleMapClusterItem implements ClusterItem {

    private final LatLng mPosition;

    public GoogleMapClusterItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
