package com.servicecall.app.activity;

import android.content.Intent;
import android.os.Bundle;

import com.servicecall.app.R;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseActivity;
import com.servicecall.app.event.SetupDoneEvent;
import com.servicecall.app.fragment.SplashFragment;

import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    private SplashFragment splashFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        ServiceCallApplication.getApplication().getComponent().inject(this);
        eventBus.register(this);

        splashFragment = new SplashFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.splash_fl_container, splashFragment).commit();
    }

    @Override
    protected void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

    public void onEventMainThread(SetupDoneEvent event) {
        Intent i = new Intent(this, SelectCategoryActivity.class);
        startActivity(i);
        finish();
    }

}
