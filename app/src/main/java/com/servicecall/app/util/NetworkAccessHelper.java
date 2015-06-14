package com.servicecall.app.util;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.servicecall.app.application.ServiceCallApplication;

import javax.inject.Inject;

/**
 * Created by Vaibhav on 6/14/2015.
 */
public class NetworkAccessHelper {

    @Inject
    RequestQueue requestQueue;

    public NetworkAccessHelper() {
        ServiceCallApplication.getApplication().getComponent().inject(this);
    }

    public void submitNetworkRequest(String requestTag, Request request) {
        requestQueue.cancelAll(requestTag);
        requestQueue.add(request);
        request.setTag(requestTag);
    }
}
