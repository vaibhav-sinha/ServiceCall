package com.servicecall.app.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.servicecall.app.R;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseFragment;
import com.servicecall.app.config.LocalData;
import com.servicecall.app.event.SetupDoneEvent;
import com.servicecall.app.util.Session;

import javax.inject.Inject;

public class SplashFragment extends BaseFragment {

    @Inject
    Session session;

    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceCallApplication.getApplication().getComponent().inject(this);
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_splash, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        session.setCategoryWithChildCategoryDtoList(LocalData.getCategoryData());

        Runnable setupDoneEventSender = new Runnable() {
            @Override
            public void run() {
                SetupDoneEvent event = new SetupDoneEvent();
                event.setSuccess(true);
                eventBus.post(event);
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(setupDoneEventSender, 3000);
    }
}
