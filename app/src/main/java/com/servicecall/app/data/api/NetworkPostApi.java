package com.servicecall.app.data.api;

import android.content.Context;

import com.servicecall.app.model.Complaint;

import java.util.List;

/**
 * Created by Vaibhav on 6/20/2015.
 */
public interface NetworkPostApi {
    void postComplaints(Context context, List<Complaint> complaintList);
}
