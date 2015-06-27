package com.servicecall.app.dagger.component;

import com.servicecall.app.activity.AddDetailsActivity;
import com.servicecall.app.activity.AddressInfoActivity;
import com.servicecall.app.activity.EditDetailsActivity;
import com.servicecall.app.activity.HomeActivity;
import com.servicecall.app.activity.SelectCategoryActivity;
import com.servicecall.app.activity.SelectComplaintActivity;
import com.servicecall.app.activity.SelectSubCategoryActivity;
import com.servicecall.app.activity.SplashActivity;
import com.servicecall.app.activity.ViewDetailsActivity;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.dagger.module.ApplicationModule;
import com.servicecall.app.data.api.DataApi;
import com.servicecall.app.data.datastore.ServerImpl;
import com.servicecall.app.data.requests.ComplaintsPostRequest;
import com.servicecall.app.data.requests.LoadCategoriesDataRequest;
import com.servicecall.app.fragment.AddDetailsFragment;
import com.servicecall.app.fragment.AddressInfoFragment;
import com.servicecall.app.fragment.EditDetailsFragment;
import com.servicecall.app.fragment.GoogleMapFragment;
import com.servicecall.app.fragment.HomeActivityFragment;
import com.servicecall.app.fragment.SelectCategoryFragment;
import com.servicecall.app.fragment.SelectComplaintFragment;
import com.servicecall.app.fragment.SelectSubCategoryFragment;
import com.servicecall.app.fragment.SplashFragment;
import com.servicecall.app.fragment.ViewDetailsFragment;
import com.servicecall.app.helper.CameraHelper;
import com.servicecall.app.util.LocationUtil;
import com.servicecall.app.util.NetworkAccessHelper;
import com.servicecall.app.util.ReverseGeocodingTask;
import com.servicecall.app.util.Session;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Vaibhav on 6/14/2015.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(ServiceCallApplication application);
    void inject(HomeActivity activity);
    void inject(HomeActivityFragment fragment);
    void inject(SplashActivity activity);
    void inject(SplashFragment fragment);
    void inject(SelectCategoryActivity activity);
    void inject(SelectCategoryFragment fragment);
    void inject(LocationUtil locationUtil);
    void inject(NetworkAccessHelper networkAccessHelper);
    void inject(Session session);
    void inject(CameraHelper cameraHelper);
    void inject(ReverseGeocodingTask reverseGeocodingTask);
    void inject(GoogleMapFragment googleMapFragment);
    void inject(SelectSubCategoryActivity activity);
    void inject(SelectSubCategoryFragment fragment);
    void inject(SelectComplaintActivity activity);
    void inject(SelectComplaintFragment fragment);
    void inject(AddDetailsActivity activity);
    void inject(AddDetailsFragment fragment);
    void inject(EditDetailsActivity activity);
    void inject(EditDetailsFragment fragment);
    void inject(ViewDetailsActivity activity);
    void inject(ViewDetailsFragment fragment);
    void inject(AddressInfoActivity activity);
    void inject(AddressInfoFragment fragment);
    void inject(ServerImpl server);
    void inject(DataApi dataApi);
    void inject(ComplaintsPostRequest request);
    void inject(LoadCategoriesDataRequest request);

}
