package com.servicecall.app.dagger.module;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.servicecall.app.application.ServiceCallApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Vaibhav on 6/14/2015.
 */
@Module
public class ApplicationModule {

    private final ServiceCallApplication serviceCallApplication;

    public ApplicationModule(ServiceCallApplication application) {
        this.serviceCallApplication = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return serviceCallApplication;
    }

    @Provides @Singleton
    public RequestQueue provideRequestQueue() {
        /** Set up to use OkHttp */
        return Volley.newRequestQueue(this.serviceCallApplication);
    }
}
