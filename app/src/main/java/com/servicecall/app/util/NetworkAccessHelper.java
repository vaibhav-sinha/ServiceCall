package com.servicecall.app.util;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.servicecall.app.base.BaseClass;

import javax.inject.Inject;

/**
 * Created by Vaibhav on 6/14/2015.
 */
public class NetworkAccessHelper extends BaseClass {

    @Inject
    RequestQueue requestQueue;

    public void submitNetworkRequest(String requestTag, Request request) {
        requestQueue.cancelAll(requestTag);
        requestQueue.add(request);
        request.setTag(requestTag);
    }
}
