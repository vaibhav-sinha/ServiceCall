package com.servicecall.app.dagger.component;

import com.servicecall.app.activity.HomeActivity;
import com.servicecall.app.activity.SelectCategoryActivity;
import com.servicecall.app.activity.SplashActivity;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseActivity;
import com.servicecall.app.dagger.module.ApplicationModule;
import com.servicecall.app.fragment.GoogleMapFragment;
import com.servicecall.app.fragment.HomeActivityFragment;
import com.servicecall.app.fragment.SelectCategoryFragment;
import com.servicecall.app.fragment.SplashFragment;
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
    void inject(ReverseGeocodingTask reverseGeocodingTask);
    void inject(GoogleMapFragment googleMapFragment);
}
