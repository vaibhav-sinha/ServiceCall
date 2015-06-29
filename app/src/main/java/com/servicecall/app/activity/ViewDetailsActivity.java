package com.servicecall.app.activity;

import android.content.Intent;
import android.os.Bundle;

import com.servicecall.app.R;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseActivity;
import com.servicecall.app.event.ComplaintSubmitOrDiscardEvent;
import com.servicecall.app.fragment.ViewDetailsFragment;

import butterknife.ButterKnife;

public class ViewDetailsActivity extends BaseActivity {

    private ViewDetailsFragment viewDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
        ButterKnife.inject(this);
        ServiceCallApplication.getApplication().getComponent().inject(this);
        eventBus.register(this);

        viewDetailsFragment = new ViewDetailsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.av_fl_container, viewDetailsFragment).commit();
    }

    @Override
    protected void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

    public void onEventMainThread(ComplaintSubmitOrDiscardEvent event) {
        if(event.isSuccess()) {
            // Intent i = new Intent(this, SelectCategoryActivity.class);
            // startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

}


