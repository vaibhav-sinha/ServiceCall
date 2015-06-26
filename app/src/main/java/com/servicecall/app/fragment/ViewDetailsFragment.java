package com.servicecall.app.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.servicecall.app.R;
import com.servicecall.app.activity.SelectCategoryActivity;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseFragment;
import com.servicecall.app.data.api.DataApi;
import com.servicecall.app.helper.MyIssueDAO;
import com.servicecall.app.model.ServerComplaint;
import com.servicecall.app.util.Session;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class ViewDetailsFragment extends BaseFragment {

    @Inject
    Session session;
    @Inject
    DataApi dataApi;

    SweetAlertDialog pDialog;

    ServerComplaint serverComplaint;

    @InjectView(R.id.vdSelected)
    TextView complaintName;
    @InjectView(R.id.vdAmenity)
    TextView issueCategory;
    @InjectView(R.id.vdDescription)
    TextView description;
    @InjectView(R.id.vd_s_count)
    TextView count;
    @InjectView(R.id.vd_b_another)
    Button another;

    @InjectView(R.id.vdReporterName)
    TextView reporterName;
    @InjectView(R.id.vdIssueLocation)
    TextView issueLocation;
    @InjectView(R.id.vdLatitude)
    TextView latitude;
    @InjectView(R.id.vdLongitude)
    TextView longitude;
    @InjectView(R.id.vdOccupantType)
    TextView occupantType;
    @InjectView(R.id.vdPropertyType)
    TextView propertyType;
    @InjectView(R.id.vdAdditionalInfo)
    TextView additionalInfo;
    @InjectView(R.id.issueIcon)
    ImageView issuePic;

    
    int colorId;
    int colorIdPressed;

    // Progress Dialog
    private ProgressDialog progressDialog;
    Boolean addedToBasket = false;

    MyIssueDAO serverComplaintDAO;

    public ViewDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceCallApplication.getApplication().getComponent().inject(this);
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_details, container, false);
        ButterKnife.inject(this, rootView);
        try {
            serverComplaint = (ServerComplaint) getActivity().getIntent().getParcelableExtra("complaint");
            complaintName.setText(serverComplaint.getIssueDetail());
            issueCategory.setText(serverComplaint.getIssueParent());
            description.setText(serverComplaint.getDescription());
            count.setText("" + serverComplaint.getQuantity());
            reporterName.setText(serverComplaint.getReporterName());
            issueLocation.setText(serverComplaint.getHomeLocation());
            latitude.setText(serverComplaint.getLatitude());
            longitude.setText(serverComplaint.getLongitude());
            occupantType.setText(serverComplaint.getOccupantType());
            propertyType.setText(serverComplaint.getPropertyType());
            additionalInfo.setText(serverComplaint.getAdditionalInfo());
        } catch (Exception e){
            e.printStackTrace();
        }
        //Initial state

        String bgColor = "#" + serverComplaint.getIssueParentColor();
        int drawableResourceId = getActivity().getResources().getIdentifier(serverComplaint.getIssueParentImageUrl().split("\\.")[0], "drawable", getActivity().getPackageName());
        try{
            issuePic.setBackgroundColor(Color.parseColor(bgColor));
            issuePic.setImageDrawable(getActivity().getResources().getDrawable(drawableResourceId));
        } catch (Exception e){
            issuePic.setBackgroundColor(Color.parseColor("#0099cc"));
            issuePic.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.category4));
        }

        final String[] colorHexCode = new String[]{"538195"};
        for(String hexCodeVal : colorHexCode ) {
            colorId = getActivity().getResources().getIdentifier("n_" + hexCodeVal.toLowerCase() + "_n", "color", getActivity().getPackageName());
            colorIdPressed = getActivity().getResources().getIdentifier("p_" + hexCodeVal.toLowerCase() + "_p", "color", getActivity().getPackageName());
            StateListDrawable states = new StateListDrawable();
            states.addState(new int[] {android.R.attr.state_pressed},
                    getActivity().getResources().getDrawable(colorIdPressed));
            states.addState(new int[] {android.R.attr.state_focused},
                    getActivity().getResources().getDrawable(colorIdPressed));
            states.addState(new int[] {},
                    getActivity().getResources().getDrawable(colorId));
            if(Build.VERSION.SDK_INT >= 16) {
                if(hexCodeVal.contentEquals("7B9FAD")) {
                    another.setBackground(states);
                }
            } else {
                if(hexCodeVal.contentEquals("7B9FAD")) {
                    another.setBackgroundDrawable(states);
                } 
            }
        }
        
        another.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                                            Intent myIntent = new Intent(getActivity(), SelectCategoryActivity.class);
                                            startActivityForResult(myIntent, 0);
            }
        });
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

}
