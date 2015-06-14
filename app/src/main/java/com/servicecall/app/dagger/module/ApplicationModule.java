package com.servicecall.app.dagger.module;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.util.LocationUtil;
import com.servicecall.app.util.Session;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

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

    @Provides
    @Singleton
    public RequestQueue provideRequestQueue() {
        /** Set up to use OkHttp */
        return Volley.newRequestQueue(this.serviceCallApplication);
    }

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return EventBus.builder().logNoSubscriberMessages(false).sendNoSubscriberEvent(false).build();
    }

    @Provides
    @Singleton
    LocationUtil provideLocationUtil() {
        return new LocationUtil();
    }

    @Provides
    @Singleton
    Session provideSession() {
        return new Session();
    }
}