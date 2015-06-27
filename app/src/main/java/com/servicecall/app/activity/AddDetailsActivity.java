package com.servicecall.app.activity;

import android.content.Intent;
import android.os.Bundle;

import com.servicecall.app.R;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseActivity;
import com.servicecall.app.event.ComplaintSubmitOrDiscardEvent;
import com.servicecall.app.fragment.AddDetailsFragment;

import butterknife.ButterKnife;

public class AddDetailsActivity extends BaseActivity {

    private AddDetailsFragment addDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);
        ButterKnife.inject(this);
        ServiceCallApplication.getApplication().getComponent().inject(this);
        eventBus.register(this);

        addDetailsFragment = new AddDetailsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.ad_fl_container, addDetailsFragment).commit();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        addDetailsFragment.onActivityResult(requestCode, resultCode, data);
    }

}
