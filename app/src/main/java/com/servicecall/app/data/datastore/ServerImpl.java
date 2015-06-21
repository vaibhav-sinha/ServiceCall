package com.servicecall.app.data.datastore;

import android.content.Context;

import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.data.requests.ComplaintsPostRequest;
import com.servicecall.app.data.requests.LoadCategoriesDataRequest;
import com.servicecall.app.model.Complaint;
import com.servicecall.app.data.api.NetworkPostApi;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Vaibhav on 6/20/2015.
 */
public class ServerImpl implements Server {

    @Inject
    ComplaintsPostRequest complaintsPostRequest;
    @Inject
    LoadCategoriesDataRequest loadCategoriesDataRequest;

    public ServerImpl() {
        ServiceCallApplication.getApplication().getComponent().inject(this);
    }

    @Override
    public void postComplaints(Context context, List<Complaint> complaintList) {
        complaintsPostRequest.processRequest(context, complaintList);
    }

    @Override
    public void loadCategoriesData(Context context) {
        loadCategoriesDataRequest.processRequest(context);
    }
}
