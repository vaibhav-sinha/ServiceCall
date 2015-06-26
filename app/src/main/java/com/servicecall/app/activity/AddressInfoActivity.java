package com.servicecall.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.servicecall.app.R;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseActivity;
import com.servicecall.app.event.ComplaintSubmitOrDiscardEvent;
import com.servicecall.app.fragment.AddressInfoFragment;

import butterknife.ButterKnife;

public class AddressInfoActivity extends BaseActivity {

    private AddressInfoFragment addressInfoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_info);
        ButterKnife.inject(this);
        ServiceCallApplication.getApplication().getComponent().inject(this);
        eventBus.register(this);

        addressInfoFragment = new AddressInfoFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.address_container, addressInfoFragment).commit();
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
        inflater.inflate(R.menu.menu_submit, menu);
        return true;
    }

}
