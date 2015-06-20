package com.servicecall.app.data.api;

import android.content.Context;

import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.data.datastore.ServerImpl;
import com.servicecall.app.model.Complaint;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Vaibhav on 6/20/2015.
 */
public class DataApi implements NetworkPostApi {

    @Inject
    ServerImpl server;

    public DataApi() {
        ServiceCallApplication.getApplication().getComponent().inject(this);
    }

    @Override
    public void postComplaints(Context context, List<Complaint> complaintList) {
        server.postComplaints(context, complaintList);
    }
}
