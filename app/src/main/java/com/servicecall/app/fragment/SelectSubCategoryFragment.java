package com.servicecall.app.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.servicecall.app.R;
import com.servicecall.app.adapter.CategoryListAdapter;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseFragment;
import com.servicecall.app.event.CategorySelectEvent;
import com.servicecall.app.event.SubCategorySelectEvent;
import com.servicecall.app.util.Session;

import java.awt.font.TextAttribute;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SelectSubCategoryFragment extends BaseFragment {

    private CategoryWithChildCategoryDto categoryWithChildCategoryDto;

    @InjectView(R.id.ssc_iv_himage)
    ImageView headerImage;
    @InjectView(R.id.ssc_tv_sel_category)
    TextView selectedCategoryName;
    @InjectView(R.id.ssc_gv_amenity_list)
    GridView gvAmenityList;

    public SelectSubCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceCallApplication.getApplication().getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_sub_category, container, false);
        ButterKnife.inject(this, rootView);
        categoryWithChildCategoryDto = (CategoryWithChildCategoryDto) getActivity().getIntent().getSerializableExtra("subCategory");
        int drawableResourceId = getActivity().getResources().getIdentifier(categoryWithChildCategoryDto.getImageUrl(), "drawable", getActivity().getPackageName());
        //TODO: Fix this
        //headerImage.setImageDrawable(getActivity().getResources().getDrawable(drawableResourceId));
        selectedCategoryName.setText(categoryWithChildCategoryDto.getName());
        CategoryListAdapter amenityListAdapter = new CategoryListAdapter(getActivity(), R.layout.item_category_list, categoryWithChildCategoryDto.getChildCategories(), null, null);
        gvAmenityList.setAdapter(amenityListAdapter);
        gvAmenityList.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                SubCategorySelectEvent event = new SubCategorySelectEvent();
                event.setSuccess(true);
                event.setCategoryWithChildCategoryDto((CategoryWithChildCategoryDto) gvAmenityList.getAdapter().getItem(pos));
                eventBus.post(event);
            }
        });
        return rootView;
    }


}
