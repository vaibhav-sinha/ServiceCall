package com.servicecall.app.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.servicecall.app.R;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseActivity;
import com.servicecall.app.event.ComplaintSelectEvent;
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
            Intent i = new Intent(this, SelectCategoryActivity.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

}
