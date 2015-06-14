package com.servicecall.app.fragment;


import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.servicecall.app.R;
import com.servicecall.app.adapter.CategoryListAdapter;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseFragment;
import com.servicecall.app.event.CategorySelectEvent;
import com.servicecall.app.event.RevGeocodeEvent;
import com.servicecall.app.util.GenericUtil;
import com.servicecall.app.util.LocationUtil;
import com.servicecall.app.util.ReverseGeocodingTask;
import com.servicecall.app.util.Session;
import com.servicecall.app.widget.ProgressTextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SelectCategoryFragment extends BaseFragment implements OnMapReadyCallback {

    @Inject
    LocationUtil locationUtil;
    @Inject
    Context applicationContext;
    @Inject
    Session session;

    private GoogleMapFragment googleMapFragment;
    private Location lastLocation;
    private ReverseGeocodingTask reverseGeocodingTask;
    private Boolean mapReady;
    private Boolean retryRevGeocoding = false;

    @InjectView(R.id.sc_gv_amenity_list)
    GridView gvAmenityList;
    @InjectView(R.id.sc_tv_rev_geocode)
    ProgressTextView asRevGeocode;

    public SelectCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceCallApplication.getApplication().getComponent().inject(this);
        eventBus.register(this);
        lastLocation = null;
        mapReady = false;
        googleMapFragment = new GoogleMapFragment();
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction().add(R.id.sc_fl_map, googleMapFragment).commit();
        }
        //Do setup
        googleMapFragment.setContext(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        locationUtil.subscribe(applicationContext, true);
    }

    @Override
    public void onStop() {
        locationUtil.unsubscribe();
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_category, container, false);
        ButterKnife.inject(this, rootView);
        asRevGeocode.setTextColor(Color.parseColor("#929292"));

        CategoryListAdapter amenityListAdapter = new CategoryListAdapter(getActivity(), R.layout.item_category_list, session.getCategoryWithChildCategoryDtoList(), null, null);
        gvAmenityList.setAdapter(amenityListAdapter);
        gvAmenityList.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                CategorySelectEvent event = new CategorySelectEvent();
                event.setSuccess(true);
                event.setCategoryWithChildCategoryDto((CategoryWithChildCategoryDto) gvAmenityList.getAdapter().getItem(pos));
                eventBus.post(event);
            }
        });
        return rootView;
    }

    public void onEventMainThread(Location location) {
        Double distance;
        Boolean doRevGeoCoding;

        if(mapReady) {
            googleMapFragment.updateMarkerLocation(location.getLatitude(), location.getLongitude());
        }

        if(lastLocation != null) {
            distance = GenericUtil.calculateDistance(location.getLatitude(), location.getLongitude(), lastLocation.getLatitude(), lastLocation.getLongitude());
            if (distance > 100) {
                doRevGeoCoding = true;
            }
            else {
                doRevGeoCoding = false;
            }
        }
        else {
            doRevGeoCoding = true;
        }

        if(doRevGeoCoding || retryRevGeocoding) {
            lastLocation = location;
            if(reverseGeocodingTask != null) {
                if(reverseGeocodingTask.getStatus() == AsyncTask.Status.FINISHED) {
                    reverseGeocodingTask = new ReverseGeocodingTask(getActivity(), location);
                    reverseGeocodingTask.execute();
                }
            }
            else {
                reverseGeocodingTask = new ReverseGeocodingTask(getActivity(), location);
                reverseGeocodingTask.execute();
            }
        }
    }

    public void onEventMainThread(RevGeocodeEvent event) {
        if(event.isSuccess()) {
            asRevGeocode.setActualText(event.getRevGeocodedLocation());
            session.setUserRevGeocodedLocation(event.getRevGeocodedFullData());
            retryRevGeocoding = false;
        }
        else {
            retryRevGeocoding = true;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMapFragment.disableGestures();
        mapReady = true;
    }
}

