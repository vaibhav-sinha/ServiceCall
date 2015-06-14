package com.servicecall.app.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.servicecall.app.R;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseActivity;
import com.servicecall.app.event.CategorySelectEvent;
import com.servicecall.app.event.SubCategorySelectEvent;
import com.servicecall.app.fragment.SelectCategoryFragment;
import com.servicecall.app.fragment.SelectSubCategoryFragment;

import butterknife.ButterKnife;

public class SelectSubCategoryActivity extends BaseActivity {

    SelectSubCategoryFragment selectSubCategoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sub_category);
        ButterKnife.inject(this);
        ServiceCallApplication.getApplication().getComponent().inject(this);
        eventBus.register(this);

        selectSubCategoryFragment = new SelectSubCategoryFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.ssc_fl_container, selectSubCategoryFragment).commit();
    }

    @Override
    protected void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

    public void onEventMainThread(SubCategorySelectEvent event) {
        if(event.isSuccess()) {
            Intent i;
            if(event.getCategoryWithChildCategoryDto().getChildCategories().get(0).getChildCategories() != null) {
                i = new Intent(this, SelectSubCategoryActivity.class);
                i.putExtra("subCategory", event.getCategoryWithChildCategoryDto());
            }
            else {
                i = new Intent(this, SelectComplaintActivity.class);
                i.putExtra("complaintList", event.getCategoryWithChildCategoryDto());
            }
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
