package com.servicecall.app.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.servicecall.app.R;
import com.servicecall.app.activity.BasketComplaintListActivity;
import com.servicecall.app.activity.SelectCategoryActivity;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseFragment;
import com.servicecall.app.data.api.DataApi;
import com.servicecall.app.helper.BasketComplaintDAO;
import com.servicecall.app.model.BasketComplaint;
import com.servicecall.app.util.Session;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class EditDetailsFragment extends BaseFragment {

    @Inject
    Session session;
    @Inject
    DataApi dataApi;

    SweetAlertDialog pDialog;

    BasketComplaint basketComplaint;

    @InjectView(R.id.adSelected)
    TextView complaintName;
    @InjectView(R.id.adAmenity)
    TextView issueCategory;
    @InjectView(R.id.adDescription)
    EditText description;
    @InjectView(R.id.ad_s_count)
    Spinner count;
    @InjectView(R.id.ad_b_another)
    Button another;
    @InjectView(R.id.ad_b_basket)
    Button submit;
    @InjectView(R.id.ad_b_discard)
    Button discard;

    int colorId;
    int colorIdPressed;

    // Progress Dialog
    private ProgressDialog progressDialog;
    Boolean addedToBasket = false;

    BasketComplaintDAO basketComplaintDAO;

    public EditDetailsFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_add_details, container, false);
        ButterKnife.inject(this, rootView);
        try {
            basketComplaint = (BasketComplaint) getActivity().getIntent().getParcelableExtra("complaint");
            complaintName.setText(basketComplaint.getIssueDetail());
            issueCategory.setText(basketComplaint.getIssueParent());
            description.setText(basketComplaint.getDescription());

        } catch (Exception e){
            e.printStackTrace();
        }
        //Initial state

        final Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(),R.layout.item_spinner, items);
        count.setAdapter(adapter);

        double compValue = Double.valueOf(basketComplaint.getQuantity());
        int compareValue = Math.round((int) compValue);
        int counterPosition = adapter.getPosition(compareValue);
        count.setSelection(counterPosition);

        final String[] colorHexCode = new String[]{"7B9FAD","90BBC9","DBDBDB"};
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
                    submit.setBackground(states);
                } else if (hexCodeVal.contentEquals("90BBC9")) {
                    another.setBackground(states);
                } else if (hexCodeVal.contentEquals("DBDBDB")) {
                    discard.setBackground(states);
                }
            } else {
                if(hexCodeVal.contentEquals("7B9FAD")) {
                    submit.setBackgroundDrawable(states);
                } else if (hexCodeVal.contentEquals("90BBC9")) {
                    another.setBackgroundDrawable(states);
                } else if (hexCodeVal.contentEquals("DBDBDB")) {
                    discard.setBackgroundDrawable(states);
                }
            }
        }

        submit.setText("Update Basket");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Do you want to save the changes?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                            basketComplaint.setQuantity(String.valueOf(count.getSelectedItem()));
                                            basketComplaint.setDescription(description.getText().toString());
                                            BasketComplaintDAO basketComplaintDAO = new BasketComplaintDAO(getActivity());
                                            basketComplaintDAO.updateBasketComplaint(basketComplaint);
                                            Intent myIntent = new Intent(getActivity(), BasketComplaintListActivity.class);
                                            startActivityForResult(myIntent, 0);
                                    }
                                });
                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent myIntent = new Intent(getActivity(), BasketComplaintListActivity.class);
                                startActivityForResult(myIntent, 0);
                            }
                        });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();

                Button posB = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                Button negB = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                posB.setBackgroundResource(R.drawable.blue_dark_blue_highlight);
                posB.setTextColor(Color.WHITE);
                posB.setTransformationMethod(null);
                negB.setTransformationMethod(null);

            }
        });

        another.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Do you want to save the changes?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        try {
                                            basketComplaint.setQuantity(String.valueOf(count.getSelectedItem()));
                                            basketComplaint.setDescription(description.getText().toString());
                                            BasketComplaintDAO basketComplaintDAO = new BasketComplaintDAO(getActivity());
                                            basketComplaintDAO.updateBasketComplaint(basketComplaint);
                                            Intent myIntent = new Intent(getActivity(), SelectCategoryActivity.class);
                                            startActivityForResult(myIntent, 0);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Toast.makeText(getActivity(), "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent myIntent = new Intent(getActivity(), SelectCategoryActivity.class);
                                startActivityForResult(myIntent, 0);
                            }
                        });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();

                Button posB = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                Button negB = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                posB.setBackgroundResource(R.drawable.blue_dark_blue_highlight);
                posB.setTextColor(Color.WHITE);
                posB.setTransformationMethod(null);
                negB.setTransformationMethod(null);

            }
        });
        return rootView;
    }

    @OnClick(R.id.ad_b_discard)
    public void onDiscard() {
        session.getComplaints().clear();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

}
