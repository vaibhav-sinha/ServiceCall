package com.servicecall.app.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.servicecall.app.R;
import com.servicecall.app.activity.BasketComplaintListActivity;
import com.servicecall.app.activity.EditDetailsActivity;
import com.servicecall.app.activity.SelectCategoryActivity;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.data.api.DataApi;
import com.servicecall.app.helper.BasketComplaintDAO;
import com.servicecall.app.helper.BitmapWorkerTask;
import com.servicecall.app.helper.CameraHelper;
import com.servicecall.app.model.BasketComplaint;
import com.servicecall.app.util.Session;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class AddDetailsFragment extends CameraHelper.CameraUtilFragment  {

    @Inject
    Session session;
    @Inject
    DataApi dataApi;
    @Inject
    CameraHelper cameraHelper;

    SweetAlertDialog pDialog;

    private CategoryWithChildCategoryDto categoryWithChildCategoryDto;
    private CategoryWithChildCategoryDto parentCategoryDto;

    @InjectView(R.id.adSelected)
    TextView complaintName;
    @InjectView(R.id.adAmenity)
    TextView issueCategory;
    @InjectView(R.id.adDescription)
    EditText description;
    @InjectView(R.id.adDescriptionPlaceholder)
    TextView descriptionPlaceholder;
    @InjectView(R.id.ad_s_count)
    Spinner count;
    @InjectView(R.id.ad_s_count_placeholder)
    TextView countPlaceholder;
    @InjectView(R.id.ad_b_another)
    Button another;
    @InjectView(R.id.ad_b_basket)
    Button submit;
    @InjectView(R.id.ad_b_discard)
    Button discard;
    @InjectView(R.id.editBasketComplaint)
    ImageView editBasketComplaintDetails;

    @InjectView(R.id.adTakePhoto)
    Button takePhoto;
    @InjectView(R.id.adAttachPhoto)
    Button attachPhoto;
    @InjectView(R.id.adDeletePhoto)
    Button deletePhoto;
    @InjectView(R.id.adRetakePhoto)
    Button retakePhoto;
    @InjectView(R.id.adPhotoDisplay)
    ImageView photoDisplay;
    @InjectView(R.id.ivDelReset)
    LinearLayout delResetContainer;


    int colorId;
    int colorIdPressed;

    // Progress Dialog
    private ProgressDialog progressDialog;
    BasketComplaint complaint;
    Boolean addedToBasket = false;
    Boolean alreadyInBasket = false;
    Boolean alreadyInSharedPref = false;
    Boolean singleIemCheckTrue = false;

    BasketComplaintDAO basketComplaintDAO;
    private ViewGroup takePhotoContainer;
    private ViewGroup photoTakenContainer;

    public AddDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceCallApplication.getApplication().getComponent().inject(this);
        cameraHelper.setFragment(this);
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
        takePhotoContainer = (ViewGroup) rootView.findViewById(R.id.take_photo_container_ref);
        photoTakenContainer = (ViewGroup) rootView.findViewById(R.id.photo_taken_container_ref);
        categoryWithChildCategoryDto = (CategoryWithChildCategoryDto) getActivity().getIntent().getSerializableExtra("complaint");
        parentCategoryDto = (CategoryWithChildCategoryDto) getActivity().getIntent().getSerializableExtra("complaintParentCategory");
        complaintName.setText(categoryWithChildCategoryDto.getName());
        issueCategory.setText(parentCategoryDto.getName());
        //Initial state

        final Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(),R.layout.item_spinner, items);
        count.setAdapter(adapter);

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

        new CheckBasketForAlreadyAddedComplaint().execute();

        editBasketComplaintDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent myIntent = new Intent(getActivity(), EditDetailsActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable("complaint", basketComplaintDAO.getBasketComplaintObjectByCategoryId(String.valueOf(categoryWithChildCategoryDto.getId())));
                    myIntent.putExtras(mBundle);
                    getActivity().startActivity(myIntent);
                } catch (Exception e){
                    Toast.makeText(getActivity(), "Unable to edit complaint", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

    @OnClick(R.id.ad_b_basket)
    public void onSubmit() {

        if(!alreadyInBasket) {
            try {
                new CreateNewBasketComplaint().execute();
                submit.setText("My Basket");
                alreadyInBasket = true;
                getActivity().invalidateOptionsMenu();
            } catch (Exception e) {
                submit.setText("+Basket");
                alreadyInBasket = false;
                e.printStackTrace();
                Toast.makeText(getActivity(), "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Intent myIntent = new Intent(getActivity(), BasketComplaintListActivity.class);
            startActivityForResult(myIntent, 0);
        }

    }

    @OnClick(R.id.ad_b_discard)
    public void onDiscard() {
        session.getComplaints().clear();
        Intent myIntent = new Intent(getActivity(), SelectCategoryActivity.class);
        startActivityForResult(myIntent, 0);
    }

    @OnClick(R.id.ad_b_another)
    public void onAnother() {
        if(!alreadyInBasket) {
            try {
                new CreateNewBasketComplaint().execute();
                submit.setText("My Basket");
                alreadyInBasket = true;
                getActivity().invalidateOptionsMenu();
                Intent myIntent = new Intent(getActivity(), SelectCategoryActivity.class);
                startActivityForResult(myIntent, 0);
            } catch (Exception e) {
                submit.setText("+Basket");
                alreadyInBasket = false;
                e.printStackTrace();
                Toast.makeText(getActivity(), "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Intent myIntent = new Intent(getActivity(), SelectCategoryActivity.class);
            startActivityForResult(myIntent, 0);
        }
    }

    @OnClick(R.id.adTakePhoto)
        public void onTakePhoto() {
            cameraHelper.openOnlyCameraIntent();
        }

    @OnClick(R.id.adAttachPhoto)
    public void onAttachPhoto() {
            cameraHelper.openOnlyGalleryIntent();
        }

    @OnClick(R.id.adDeletePhoto)
    public void onDeletePhoto() {
        cameraHelper.setImageName(null);
        resetIssueImageView();
    }

    @OnClick(R.id.adRetakePhoto)
    public void onRetakePhoto() {
        cameraHelper.openImageIntent();
    }

    private void resetIssueImageView() {
        if(TextUtils.isEmpty(cameraHelper.getImageName())) {
            takePhotoContainer.setVisibility(View.VISIBLE);
            photoTakenContainer.setVisibility(View.GONE);
        } else {
            takePhotoContainer.setVisibility(View.GONE);
            photoTakenContainer.setVisibility(View.VISIBLE);
        }
    }

    private void displayImageIfAvailable() {
        resetIssueImageView();
        if(!TextUtils.isEmpty(cameraHelper.getImageName())) {
            new BitmapWorkerTask(photoDisplay, 200).execute(cameraHelper.getImageName());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        cameraHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cameraHelper.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cameraHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume(){
        super.onResume();
        new CheckBasketForAlreadyAddedComplaint().execute();
    }

    private void addComplaintDetails() {
        complaint = new BasketComplaint();
        complaint.setCategoryId(String.valueOf(categoryWithChildCategoryDto.getId()));
        complaint.setIssueDetail(categoryWithChildCategoryDto.getName());
        complaint.setIssueParent(parentCategoryDto.getName());
        complaint.setIssueParentColor(parentCategoryDto.getColor());
        complaint.setIssueParentImageUrl(parentCategoryDto.getImageUrl());
        complaint.setQuantity(String.valueOf(count.getSelectedItem()));
        complaint.setDescription(description.getText().toString());
        complaint.setIssueImagePath(cameraHelper.getImageName());
    }

    @Override
    public void onCameraPicTaken() {
        displayImageIfAvailable();
    }

    @Override
    public void onGalleryPicChosen() {
        displayImageIfAvailable();
    }

    class CreateNewBasketComplaint extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            addComplaintDetails();
        }

        protected String doInBackground(String... args) {
            basketComplaintDAO = new BasketComplaintDAO(getActivity());
            try {
                basketComplaintDAO.saveBasketComplaint(complaint);
                addedToBasket = true;
            } catch (Exception e){
                e.printStackTrace();
            }

            if(!addedToBasket){
                alreadyInBasket = false;
            } else {
                alreadyInBasket = true;
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            Toast.makeText(getActivity(), "Added to Basket", Toast.LENGTH_SHORT).show();
            if(alreadyInBasket){
                editBasketComplaintDetails.setVisibility(View.VISIBLE);
                submit.setText("My Basket");
                description.setVisibility(View.GONE);
                descriptionPlaceholder.setText(description.getText().toString());
                descriptionPlaceholder.setVisibility(View.VISIBLE);
                count.setVisibility(View.GONE);
                countPlaceholder.setVisibility(View.VISIBLE);
                countPlaceholder.setText(count.getSelectedItem().toString());
                delResetContainer.setVisibility(View.GONE);
            } else {
                editBasketComplaintDetails.setVisibility(View.GONE);
                description.setVisibility(View.VISIBLE);
                descriptionPlaceholder.setVisibility(View.GONE);
                submit.setText("+Basket");
                count.setVisibility(View.VISIBLE);
                countPlaceholder.setVisibility(View.GONE);
                delResetContainer.setVisibility(View.VISIBLE);
            }
        }

    }

    class CheckBasketForAlreadyAddedComplaint extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            basketComplaintDAO = new BasketComplaintDAO(getActivity());
            try {
                singleIemCheckTrue = basketComplaintDAO.getBasketComplaintByCategoryId(String.valueOf(categoryWithChildCategoryDto.getId()));
            } catch (Exception e){
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            try {
                if (singleIemCheckTrue) {
                    submit.setText("My Basket");
                    editBasketComplaintDetails.setVisibility(View.VISIBLE);
                    alreadyInBasket = true;
                    description.setVisibility(View.GONE);
                    if(description.getText().toString().trim().isEmpty()){
                        descriptionPlaceholder.setText(description.getText().toString());
                    } else {
                        descriptionPlaceholder.setText(description.getText().toString());
                    }
                    descriptionPlaceholder.setVisibility(View.VISIBLE);
                    count.setVisibility(View.GONE);
                    countPlaceholder.setVisibility(View.VISIBLE);
                    countPlaceholder.setText(count.getSelectedItem().toString());
                    displayImageIfAvailable();
                    if(!TextUtils.isEmpty(cameraHelper.getImageName())) {
                        delResetContainer.setVisibility(View.GONE);
                    }
                } else {
                    submit.setText("+Basket");
                    editBasketComplaintDetails.setVisibility(View.GONE);
                    alreadyInBasket = false;
                    description.setVisibility(View.VISIBLE);
                    descriptionPlaceholder.setVisibility(View.GONE);
                    count.setVisibility(View.VISIBLE);
                    countPlaceholder.setVisibility(View.GONE);
                    delResetContainer.setVisibility(View.VISIBLE);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
