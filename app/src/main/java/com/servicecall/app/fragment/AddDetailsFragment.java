package com.servicecall.app.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.servicecall.app.R;
import com.servicecall.app.base.BaseFragment;
import com.servicecall.app.data.api.DataApi;
import com.servicecall.app.event.ComplaintSaveResponseEvent;
import com.servicecall.app.event.ComplaintSubmitOrDiscardEvent;
import com.servicecall.app.model.Complaint;
import com.servicecall.app.util.Session;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class AddDetailsFragment extends BaseFragment {

    @Inject
    Session session;
    @Inject
    DataApi dataApi;

    SweetAlertDialog pDialog;

    private CategoryWithChildCategoryDto categoryWithChildCategoryDto;

    @InjectView(R.id.ad_tv_complaint)
    TextView complaintName;
    @InjectView(R.id.ad_tv_description)
    EditText description;
    @InjectView(R.id.ad_s_count)
    Spinner count;
    @InjectView(R.id.ad_b_another)
    Button another;
    @InjectView(R.id.ad_b_submit)
    Button submit;
    @InjectView(R.id.ad_b_discard)
    Button discard;

    public AddDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_details, container, false);
        ButterKnife.inject(this, rootView);
        categoryWithChildCategoryDto = (CategoryWithChildCategoryDto) getActivity().getIntent().getSerializableExtra("complaint");
        complaintName.setText(categoryWithChildCategoryDto.getName());
        if(session.getComplaints().size() > 1) {
            submit.setText("Submit all");
            discard.setText("Discard all");
        }
        else {
            submit.setText("Submit");
            discard.setText("Discard");
        }
        final Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(),android.R.layout.simple_spinner_item, items);
        count.setAdapter(adapter);

        return rootView;
    }

    @OnClick(R.id.ad_b_submit)
    public void onSubmit() {
        if(session.getLatitude() != null) {
            addComplaintToSession();
            pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Submitting");
            pDialog.setCancelable(false);
            pDialog.show();
            submitComplaints();
        }
        else {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Could not get your location. Make sure location setting is enabled on your device and try again.")
                    .show();
        }
    }

    @OnClick(R.id.ad_b_discard)
    public void onDiscard() {
        session.getComplaints().clear();
        sendEvent();
    }

    @OnClick(R.id.ad_b_another)
    public void onAnother() {
        if(session.getLatitude() != null) {
            addComplaintToSession();
            sendEvent();
        }
        else {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Could not get your location. Make sure location setting is enabled on your device and try again.")
                    .show();
        }
    }

    private void addComplaintToSession() {
        Complaint complaint = new Complaint();
        complaint.setCategoryId(categoryWithChildCategoryDto.getId());
        complaint.setDescription(description.getText().toString());
        complaint.setCount((Integer) count.getSelectedItem());
        complaint.setHomeLocation(session.getUserRevGeocodedLocation());
        complaint.setLatitude(session.getLatitude());
        complaint.setLongitude(session.getLongitude());
        session.getComplaints().add(complaint);
    }

    private void submitComplaints() {
        dataApi.postComplaints(getActivity(), session.getComplaints());
    }

    private void sendEvent() {
        ComplaintSubmitOrDiscardEvent event = new ComplaintSubmitOrDiscardEvent();
        event.setSuccess(true);
        eventBus.post(event);
    }

    public void onEventMainThread(ComplaintSaveResponseEvent event) {
        if(event.isSuccess()) {
            pDialog.dismissWithAnimation();
            session.getComplaints().clear();
            sendEvent();
        }
        else {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Error")
                    .setContentText("Oops! Something went wrong. Retry?")
                    .setCancelText("No, discard complaints!")
                    .setConfirmText("Yes, try again!")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                            session.getComplaints().clear();
                            sendEvent();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            submitComplaints();
                        }
                    })
                    .show();
        }
    }

}
