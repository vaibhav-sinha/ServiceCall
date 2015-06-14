package com.servicecall.app.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.servicecall.app.R;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseActivity;
import com.servicecall.app.fragment.SelectCategoryFragment;

import butterknife.ButterKnife;

public class SelectCategoryActivity extends BaseActivity {

    private SelectCategoryFragment selectCategoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);
        ButterKnife.inject(this);
        ServiceCallApplication.getApplication().getComponent().inject(this);
        eventBus.register(this);

        selectCategoryFragment = new SelectCategoryFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.sc_fl_container, selectCategoryFragment).commit();
    }

    @Override
    protected void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }
}
