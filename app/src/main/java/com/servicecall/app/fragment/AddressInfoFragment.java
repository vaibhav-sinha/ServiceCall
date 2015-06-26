package com.servicecall.app.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.servicecall.app.R;
import com.servicecall.app.activity.MyIssuesListActivity;
import com.servicecall.app.activity.SelectCategoryActivity;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseFragment;
import com.servicecall.app.data.api.DataApi;
import com.servicecall.app.event.ComplaintSaveResponseEvent;
import com.servicecall.app.event.ComplaintSubmitOrDiscardEvent;
import com.servicecall.app.helper.BasketComplaintDAO;
import com.servicecall.app.helper.MyIssueDAO;
import com.servicecall.app.model.BasketComplaint;
import com.servicecall.app.model.ServerComplaint;
import com.servicecall.app.util.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class AddressInfoFragment extends BaseFragment {

    @Inject
    Session session;
    @Inject
    DataApi dataApi;

    SweetAlertDialog pDialog;

    private CategoryWithChildCategoryDto categoryWithChildCategoryDto;
    private CategoryWithChildCategoryDto parentCategoryDto;

    @InjectView(R.id.etUserName)
    EditText userName;
    @InjectView(R.id.etUserNameLabel)
    TextView userNameLabel;
    @InjectView(R.id.etAddressName)
    EditText address;
    @InjectView(R.id.etAddressLabel)
    TextView addressLabel;
    @InjectView(R.id.etPostcodeName)
    EditText postcode;
    @InjectView(R.id.etPostcodeLabel)
    TextView postcodeLabel;

    @InjectView(R.id.etDayTelNumber)
    EditText dayTelNum;
    @InjectView(R.id.etDayTelNumberLabel)
    TextView dayTelNumLabel;
    @InjectView(R.id.etMobileTelNumber)
    EditText mobNum;
    @InjectView(R.id.etMobileNumberLabel)
    TextView mobNumLabel;
    @InjectView(R.id.etEmail)
    EditText email;
    @InjectView(R.id.etEmailLabel)
    TextView emailLabel;
    @InjectView(R.id.etWorkTelNumber)
    EditText workNum;
    @InjectView(R.id.etWorkNumberLabel)
    TextView workNumLabel;

    @InjectView(R.id.etOccupantTypeLabel)
    TextView occupantTypeLabel;
    @InjectView(R.id.et_s_occupant)
    Spinner occupantType;
    @InjectView(R.id.etPropertyTypeLabel)
    TextView propertyTypeLabel;
    @InjectView(R.id.et_s_property)
    Spinner propertyType;
    @InjectView(R.id.etAdditionalInfoLabel)
    TextView additionalInfoLabel;
    @InjectView(R.id.etAdditionalInfo)
    EditText additionalInfo;


    @InjectView(R.id.cbMonAM)
    CheckBox cbMonAm;
    @InjectView(R.id.cbMonPM)
    CheckBox cbMonPm;
    @InjectView(R.id.cbTueAM)
    CheckBox cbTueAm;
    @InjectView(R.id.cbTuePM)
    CheckBox cbTuePm;
    @InjectView(R.id.cbWedAM)
    CheckBox cbWedAm;
    @InjectView(R.id.cbWedPM)
    CheckBox cbWedPm;
    @InjectView(R.id.cbThuAM)
    CheckBox cbThuAm;
    @InjectView(R.id.cbThuPM)
    CheckBox cbThuPm;
    @InjectView(R.id.cbFriAM)
    CheckBox cbFriAm;
    @InjectView(R.id.cbFriPM)
    CheckBox cbFriPm;

    @InjectView(R.id.submitComplaints)
    Button submitComplaints;
    @InjectView(R.id.resetDetails)
    Button resetDetails;
    @InjectView(R.id.discardOrder)
    Button discardOrder;

    int colorId;
    int colorIdPressed;

    ProgressDialog progressDialog;
    MyIssueDAO myIssueDAO;
    BasketComplaintDAO basketComplaintDAO;
    ServerComplaint serverComplaint;
    ArrayList<BasketComplaint> basketComplaints = new ArrayList<>();
    ArrayList<ServerComplaint> serverComplaints = new ArrayList<>();

    String dayTimeAvailability;
    String prevValHolder;
    Boolean pushedToServer = false;
    Boolean alreadyInServer = false;
    Boolean checkboxChangeEvent = false;

    public AddressInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceCallApplication.getApplication().getComponent().inject(this);
        eventBus.register(this);
        setHasOptionsMenu(true);

    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_address_info, container, false);
        ButterKnife.inject(this, rootView);

        final String[] items = new String[]{"General needs tenant","Temporary housing tenant","Other tenant","Leaseholder","Freeholder"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.item_spinner, items);
        occupantType.setAdapter(adapter);

        final String[] items2 = new String[]{"House","Flat"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),R.layout.item_spinner, items2);
        propertyType.setAdapter(adapter2);

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
                    submitComplaints.setBackground(states);
                } else if (hexCodeVal.contentEquals("90BBC9")) {
                    resetDetails.setBackground(states);
                } else if (hexCodeVal.contentEquals("DBDBDB")) {
                    discardOrder.setBackground(states);
                }
            } else {
                if(hexCodeVal.contentEquals("7B9FAD")) {
                    submitComplaints.setBackgroundDrawable(states);
                } else if (hexCodeVal.contentEquals("90BBC9")) {
                    resetDetails.setBackgroundDrawable(states);
                } else if (hexCodeVal.contentEquals("DBDBDB")) {
                    discardOrder.setBackgroundDrawable(states);
                }
            }
        }

        editTextAnimation();

        dayTimeAvailability = "";
        prevValHolder = "";

        cbMonAm.setOnCheckedChangeListener(new myCheckBoxChangeClicker());
        cbTueAm.setOnCheckedChangeListener(new myCheckBoxChangeClicker());
        cbWedAm.setOnCheckedChangeListener(new myCheckBoxChangeClicker());
        cbThuAm.setOnCheckedChangeListener(new myCheckBoxChangeClicker());
        cbFriAm.setOnCheckedChangeListener(new myCheckBoxChangeClicker());
        cbMonPm.setOnCheckedChangeListener(new myCheckBoxChangeClicker());
        cbTuePm.setOnCheckedChangeListener(new myCheckBoxChangeClicker());
        cbWedPm.setOnCheckedChangeListener(new myCheckBoxChangeClicker());
        cbThuPm.setOnCheckedChangeListener(new myCheckBoxChangeClicker());
        cbFriPm.setOnCheckedChangeListener(new myCheckBoxChangeClicker());

        JSONObject obj = null;
        try {

            obj = new JSONObject(session.getUserRevGeocodedLocation());
            postcode.setText(obj.getString("mPostalCode"));
            JSONObject geoObject = obj.getJSONObject("mAddressLines");
            address.setText(geoObject.getString("0") + " " + geoObject.getString("1") + " " + geoObject.getString("2") + " " + geoObject.getString("3")) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }

                discardOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder
                                .setTitle("Cancel complaints submission")
                                .setMessage("Do you want to cancel complaints submission?")
                                .setCancelable(true)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent myIntent = new Intent(getActivity(), SelectCategoryActivity.class);
                                        startActivityForResult(myIntent, 0);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        Button posB = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        Button negB = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        posB.setBackgroundResource(R.drawable.blue_dark_blue_highlight);
                        posB.setTextColor(Color.WHITE);
                        posB.setTransformationMethod(null);
                        negB.setTransformationMethod(null);

        }
    });

        resetDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName.setText("");
                address.setText("");
                postcode.setText("");
                dayTelNum.setText("");
                workNum.setText("");
                mobNum.setText("");
                email.setText("");
                additionalInfo.setText("");
                occupantType.setSelection(0);
                propertyType.setSelection(0);
                cbMonAm.setChecked(false);
                cbTueAm.setChecked(false);
                cbWedAm.setChecked(false);
                cbThuAm.setChecked(false);
                cbFriAm.setChecked(false);
                cbMonPm.setChecked(false);
                cbTuePm.setChecked(false);
                cbWedPm.setChecked(false);
                cbThuPm.setChecked(false);
                cbFriPm.setChecked(false);

            }
        });

        submitComplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(session.getLatitude() != null) {

                    if (!isValidEmail(email.getText().toString())) {
                        Toast.makeText(getActivity(), "Please enter a Valid Email Address", Toast.LENGTH_SHORT).show();
                    } else if (userName.getText().toString().trim().isEmpty() ||
                            address.getText().toString().trim().isEmpty() ||
                            postcode.getText().toString().trim().isEmpty() ||
                            dayTelNum.getText().toString().trim().isEmpty() ||
                            email.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getActivity(), "Some of the required fields are empty. Please try again.", Toast.LENGTH_LONG).show();
                    } else {
                            new PushAllComplaintsToServer().execute();
                    }
                }  else {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Could not get your location. Make sure location setting is enabled on your device and try again.")
                            .show();
                }

            }
        });

        return rootView;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    class PushAllComplaintsToServer extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Registering Complaints, Please wait ..");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();

            myIssueDAO = new MyIssueDAO();
            basketComplaintDAO = new BasketComplaintDAO(getActivity());
            basketComplaints = basketComplaintDAO.getAllBasketComplaints();

            for (BasketComplaint basketComplaint : basketComplaints ){

                serverComplaint = new ServerComplaint();
                serverComplaint.setCategoryId(Integer.valueOf(basketComplaint.getCategoryId()));
                serverComplaint.setIssueDetail(basketComplaint.getIssueDetail());
                serverComplaint.setIssueParent(basketComplaint.getIssueParent());
                serverComplaint.setIssueParentColor(basketComplaint.getIssueParentColor());
                serverComplaint.setIssueParentImageUrl(basketComplaint.getIssueParentImageUrl());
                serverComplaint.setQuantity(Integer.valueOf(basketComplaint.getQuantity()));
                serverComplaint.setDescription(basketComplaint.getDescription());
                serverComplaint.setHomeLocation(address.getText().toString());
                serverComplaint.setLatitude(String.valueOf(session.getLatitude()));
                serverComplaint.setLongitude(String.valueOf(session.getLongitude()));
                serverComplaint.setReporterName(userName.getText().toString());
                serverComplaint.setPostcode(postcode.getText().toString());
                serverComplaint.setOccupantType((String) occupantType.getSelectedItem());
                serverComplaint.setPropertyType((String) propertyType.getSelectedItem());
                serverComplaint.setDayTelNum(dayTelNum.getText().toString());
                serverComplaint.setWorkTelNum(workNum.getText().toString());
                serverComplaint.setMobNum(mobNum.getText().toString());
                serverComplaint.setEmail(email.getText().toString());
                serverComplaint.setAdditionalInfo(additionalInfo.getText().toString());
                serverComplaint.setDayTimeAvailability(dayTimeAvailability);
                serverComplaints.add(serverComplaint);
            }


        }

        protected String doInBackground(String... args) {

            try {
                pushedToServer = myIssueDAO.saveMyIssues(serverComplaints);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.cancel();

            if(pushedToServer){

                try{
                    BasketComplaintDAO basketComplaintDAO = new BasketComplaintDAO(getActivity());
                    ArrayList<BasketComplaint> basketComplaints = new ArrayList<>();
                    basketComplaints = basketComplaintDAO.getAllBasketComplaints();

                    for (int i = 0; i < basketComplaints.size(); i++) {
                        BasketComplaint basketComplaint = basketComplaints.get(i);
                        basketComplaintDAO.removeBasketComplaint(basketComplaint);
                    }

                    Intent i = new Intent(getActivity(), MyIssuesListActivity.class);
                    startActivity(i);

                } catch( Exception e){
                    e.printStackTrace();
                }

            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.push_to_server:
                if(session.getLatitude() != null) {
                    if (!isValidEmail(email.getText().toString())) {
                        Toast.makeText(getActivity(), "Please enter a Valid Email Address", Toast.LENGTH_SHORT).show();
                    } else if (userName.getText().toString().trim().isEmpty() ||
                            address.getText().toString().trim().isEmpty() ||
                            postcode.getText().toString().trim().isEmpty() ||
                            dayTelNum.getText().toString().trim().isEmpty() ||
                            email.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getActivity(), "Some of the required fields are empty. Please try again.", Toast.LENGTH_LONG).show();
                    } else {
                        new PushAllComplaintsToServer().execute();
                    }
                }  else {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Could not get your location. Make sure location setting is enabled on your device and try again.")
                            .show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void sendEvent() {
        ComplaintSubmitOrDiscardEvent event = new ComplaintSubmitOrDiscardEvent();
        event.setSuccess(true);
        eventBus.post(event);
    }

    public void onEventMainThread(ComplaintSaveResponseEvent event) {
        if(event.isSuccess()) {
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
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            submitComplaints();
                        }
                    })
                    .show();
        }
    }


    class myCheckBoxChangeClicker implements CheckBox.OnCheckedChangeListener
    {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub

            checkboxChangeEvent = true;

            // Toast.makeText(CheckBoxCheckedDemo.this, &quot;Checked =&gt; &quot;+isChecked, Toast.LENGTH_SHORT).show();
            if ( checkboxChangeEvent == true ) {
                int id = buttonView.getId();

                if (id == R.id.cbMonAM) {
                    if (isChecked) {
                        prevValHolder = dayTimeAvailability;
                        dayTimeAvailability = dayTimeAvailability + "Mon AM\t";
                    } else {
                        dayTimeAvailability = prevValHolder;
                    }
                } else if (id == R.id.cbTueAM) {
                    if (isChecked) {
                        prevValHolder = dayTimeAvailability;
                        dayTimeAvailability = dayTimeAvailability + "Tue AM\t";
                    } else {
                        dayTimeAvailability = prevValHolder;
                    }
                } else if (id == R.id.cbWedAM) {
                    if (isChecked) {
                        prevValHolder = dayTimeAvailability;
                        dayTimeAvailability = dayTimeAvailability + "Wed AM\t";
                    } else {
                        dayTimeAvailability = prevValHolder;
                    }
                } else if (id == R.id.cbThuAM) {
                    if (isChecked) {
                        prevValHolder = dayTimeAvailability;
                        dayTimeAvailability = dayTimeAvailability + "Thu AM\t";
                    } else {
                        dayTimeAvailability = prevValHolder;
                    }
                } else if (id == R.id.cbFriAM) {
                    if (isChecked) {
                        prevValHolder = dayTimeAvailability;
                        dayTimeAvailability = dayTimeAvailability + "Fri AM\t";
                    } else {
                        dayTimeAvailability = prevValHolder;
                    }
                } else if (id == R.id.cbMonPM) {
                    if (isChecked) {
                        prevValHolder = dayTimeAvailability;
                        dayTimeAvailability = dayTimeAvailability + "Mon PM\t";
                    } else {
                        dayTimeAvailability = prevValHolder;
                    }
                } else if (id == R.id.cbTuePM) {
                    if (isChecked) {
                        prevValHolder = dayTimeAvailability;
                        dayTimeAvailability = dayTimeAvailability + "Tue PM\t";
                    } else {
                        dayTimeAvailability = prevValHolder;
                    }
                } else if (id == R.id.cbWedPM) {
                    if (isChecked) {
                        prevValHolder = dayTimeAvailability;
                        dayTimeAvailability = dayTimeAvailability + "Wed PM\t";
                    } else {
                        dayTimeAvailability = prevValHolder;
                    }
                } else if (id == R.id.cbThuPM) {
                    if (isChecked) {
                        prevValHolder = dayTimeAvailability;
                        dayTimeAvailability = dayTimeAvailability + "Thu PM\t";
                    } else {
                        dayTimeAvailability = prevValHolder;
                    }
                } else if (id == R.id.cbFriPM) {
                    if (isChecked) {
                        prevValHolder = dayTimeAvailability;
                        dayTimeAvailability = dayTimeAvailability + "Fri PM\t";
                    } else {
                        dayTimeAvailability = prevValHolder;
                    }
                }
            }
        }
    }



    private void editTextAnimation() {


        if( !userName.getText().toString().trim().isEmpty() ) {
            userNameLabel.setVisibility(View.VISIBLE);
        }
        if( !address.getText().toString().trim().isEmpty() ) {
            addressLabel.setVisibility(View.VISIBLE);
        }
        if( !postcode.getText().toString().trim().isEmpty() ) {
            postcodeLabel.setVisibility(View.VISIBLE);
        }
        if( !additionalInfo.getText().toString().trim().isEmpty() ) {
            additionalInfoLabel.setVisibility(View.VISIBLE);
        }
        if( !dayTelNum.getText().toString().trim().isEmpty() ) {
            dayTelNumLabel.setVisibility(View.VISIBLE);
        }
        if( !workNum.getText().toString().trim().isEmpty() ) {
            workNumLabel.setVisibility(View.VISIBLE);
        }
        if( !mobNum.getText().toString().trim().isEmpty() ) {
            mobNumLabel.setVisibility(View.VISIBLE);
        }
        if( !email.getText().toString().trim().isEmpty() ) {
            emailLabel.setVisibility(View.VISIBLE);
        }


        userName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (userName.getText().toString().trim().length() == 0) {
                    userNameLabel.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userNameLabel.setVisibility(View.VISIBLE);
            }

        });

        address.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (address.getText().toString().trim().length() == 0) {
                    addressLabel.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addressLabel.setVisibility(View.VISIBLE);
            }

        });

        postcode.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (postcode.getText().toString().trim().length() == 0) {
                    postcodeLabel.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                postcodeLabel.setVisibility(View.VISIBLE);
            }

        });

        dayTelNum.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (dayTelNum.getText().toString().trim().length() == 0) {
                    dayTelNumLabel.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dayTelNumLabel.setVisibility(View.VISIBLE);
            }

        });

        workNum.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (workNum.getText().toString().trim().length() == 0) {
                    workNumLabel.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                workNumLabel.setVisibility(View.VISIBLE);
            }

        });

        mobNum.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (mobNum.getText().toString().trim().length() == 0) {
                    mobNumLabel.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mobNumLabel.setVisibility(View.VISIBLE);
            }

        });


        email.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (email.getText().toString().trim().length() == 0) {
                    emailLabel.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailLabel.setVisibility(View.VISIBLE);
            }

        });

        additionalInfo.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (additionalInfo.getText().toString().trim().length() == 0) {
                    additionalInfoLabel.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                additionalInfoLabel.setVisibility(View.VISIBLE);
            }

        });


    };


}
