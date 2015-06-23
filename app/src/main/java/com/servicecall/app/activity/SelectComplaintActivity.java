package com.servicecall.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.servicecall.app.R;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseActivity;
import com.servicecall.app.event.ComplaintSelectEvent;
import com.servicecall.app.fragment.SelectComplaintFragment;

import butterknife.ButterKnife;

public class SelectComplaintActivity extends BaseActivity {

    private SelectComplaintFragment selectComplaintFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_complaint);
        ButterKnife.inject(this);
        ServiceCallApplication.getApplication().getComponent().inject(this);
        eventBus.register(this);

        selectComplaintFragment = new SelectComplaintFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.scom_fl_container, selectComplaintFragment).commit();
    }

    @Override
    protected void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

    public void onEventMainThread(ComplaintSelectEvent event) {
        if(event.isSuccess()) {
            Intent i = new Intent(this, AddDetailsActivity.class);
            i.putExtra("complaint", event.getCategoryWithChildCategoryDto());
            CategoryWithChildCategoryDto parentCategory = (CategoryWithChildCategoryDto) getIntent().getSerializableExtra("complaintList");
            i.putExtra("complaintParentCategory", parentCategory);
            startActivity(i);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
