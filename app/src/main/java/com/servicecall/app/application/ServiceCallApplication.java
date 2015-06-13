package com.servicecall.app.application;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.servicecall.app.dagger.component.ApplicationComponent;
import com.servicecall.app.dagger.component.DaggerApplicationComponent;
import com.servicecall.app.dagger.module.ApplicationModule;

/**
 * Created by Vaibhav on 6/14/2015.
 */
public class ServiceCallApplication extends Application {

    private ApplicationComponent applicationComponent;
    private static ServiceCallApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
        instance = this;
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }

    public static ServiceCallApplication from(@NonNull Context context) {
        return (ServiceCallApplication) context.getApplicationContext();
    }

    public static ServiceCallApplication getApplication() {
        return instance;
    }
}
