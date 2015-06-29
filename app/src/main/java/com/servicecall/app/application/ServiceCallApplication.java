package com.servicecall.app.application;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.servicecall.app.dagger.component.ApplicationComponent;
import com.servicecall.app.dagger.component.DaggerApplicationComponent;
import com.servicecall.app.dagger.module.ApplicationModule;
import com.servicecall.app.util.LruBitmapCache;

/**
 * Created by Vaibhav on 6/14/2015.
 */
public class ServiceCallApplication extends Application {

    private ApplicationComponent applicationComponent;
    private static ServiceCallApplication instance;

    public static final String TAG = ServiceCallApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

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

    public static synchronized ServiceCallApplication getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }

      }

}
