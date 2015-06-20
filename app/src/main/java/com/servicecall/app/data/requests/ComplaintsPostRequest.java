package com.servicecall.app.data.requests;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.config.Constants;
import com.servicecall.app.data.requests.volley.GenericPostVolleyRequest;
import com.servicecall.app.event.ComplaintSaveResponseEvent;
import com.servicecall.app.event.ComplaintSavedEvent;
import com.servicecall.app.model.Complaint;
import com.servicecall.app.model.ComplaintSaveResponse;
import com.servicecall.app.model.ErrorDto;
import com.servicecall.app.util.NetworkAccessHelper;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Created by Vaibhav on 6/20/2015.
 */
public class ComplaintsPostRequest {

    @Inject
    EventBus eventBus;
    @Inject
    NetworkAccessHelper networkAccessHelper;

    public ComplaintsPostRequest() {
        ServiceCallApplication.getApplication().getComponent().inject(this);
    }

    public void processRequest(Context context, List<Complaint> complaintList)
    {
        GenericPostVolleyRequest<List<Complaint>> request = new GenericPostVolleyRequest<>(Constants.COMPLAINTS_POST_URL, createErrorListener(), createSuccessListener(), complaintList);
        networkAccessHelper.submitNetworkRequest("PostComplaints", request);
    }

    private Response.Listener<String> createSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ComplaintSaveResponse complaintSaveResponse = new Gson().fromJson(response, ComplaintSaveResponse.class);
                ComplaintSaveResponseEvent event = new ComplaintSaveResponseEvent();
                if(complaintSaveResponse == null || complaintSaveResponse.getError()) {
                    event.setSuccess(false);
                }
                else {
                    event.setSuccess(true);
                }
                eventBus.post(event);
            }
        };
    }


    private Response.ErrorListener createErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorDto errorDto = null;
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null) {
                    errorDto = new Gson().fromJson(new String(response.data), ErrorDto.class);
                }
                ComplaintSavedEvent event = new ComplaintSavedEvent();
                event.setSuccess(false);
                if(errorDto == null) {
                    event.setErrorMessage(error.toString());
                }
                else {
                    event.setErrorMessage(errorDto.getMessage());
                }
                eventBus.post(event);
            }
        };
    }

}
