package com.servicecall.app.dagger.component;

import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseActivity;
import com.servicecall.app.dagger.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Vaibhav on 6/14/2015.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    /*void inject(ServiceCallApplication application);
    void inject(BaseActivity activity);*/
    void inject(Object object);
}
