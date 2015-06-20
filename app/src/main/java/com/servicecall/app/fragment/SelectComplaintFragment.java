package com.servicecall.app.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.servicecall.app.R;
import com.servicecall.app.adapter.CategoryListAdapter;
import com.servicecall.app.adapter.TemplateListAdapter;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseFragment;
import com.servicecall.app.event.ComplaintSelectEvent;
import com.servicecall.app.event.SubCategorySelectEvent;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SelectComplaintFragment extends BaseFragment {

    private CategoryWithChildCategoryDto categoryWithChildCategoryDto;

    @InjectView(R.id.scom_iv_himage)
    ImageView headerImage;
    @InjectView(R.id.scom_tv_sel_category)
    TextView selectedCategoryName;
    @InjectView(R.id.scom_lv_amenity_list)
    ListView lvAmenityList;

    public SelectComplaintFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceCallApplication.getApplication().getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_complaint, container, false);
        ButterKnife.inject(this, rootView);
        categoryWithChildCategoryDto = (CategoryWithChildCategoryDto) getActivity().getIntent().getSerializableExtra("complaintList");
        //int drawableResourceId = getActivity().getResources().getIdentifier(categoryWithChildCategoryDto.getImageUrl(), "drawable", getActivity().getPackageName());
        //TODO: Fix this
        //headerImage.setImageDrawable(getActivity().getResources().getDrawable(drawableResourceId));
        selectedCategoryName.setText(categoryWithChildCategoryDto.getName());
        TemplateListAdapter amenityListAdapter = new TemplateListAdapter(getActivity(), R.layout.item_template_list, categoryWithChildCategoryDto.getChildCategories());
        lvAmenityList.setAdapter(amenityListAdapter);
        lvAmenityList.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                ComplaintSelectEvent event = new ComplaintSelectEvent();
                event.setSuccess(true);
                event.setCategoryWithChildCategoryDto((CategoryWithChildCategoryDto) lvAmenityList.getAdapter().getItem(pos));
                eventBus.post(event);
            }
        });
        return rootView;
    }


}
