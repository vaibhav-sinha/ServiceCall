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
import com.servicecall.app.data.api.DataApi;
import com.servicecall.app.event.GetCategoriesDataEvent;
import com.servicecall.app.event.SetupDoneEvent;
import com.servicecall.app.util.Session;

import javax.inject.Inject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SplashFragment extends BaseFragment {

    @Inject
    Session session;
    @Inject
    DataApi dataApi;

    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceCallApplication.getApplication().getComponent().inject(this);
        eventBus.register(this);
        dataApi.loadCategoriesData(getActivity());

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

    /*@Override
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
    }*/

    public void onEventMainThread(GetCategoriesDataEvent event) {
        if(event.isSuccess()) {
            session.setCategoryWithChildCategoryDtoList(event.getCategoryWithChildCategoryDtoList());
            SetupDoneEvent eventToSend = new SetupDoneEvent();
            eventToSend.setSuccess(true);
            eventBus.post(eventToSend);
        }
        else {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Could not get app setup data from internet. Check if your internet connection works")
                    .show();
        }
    }
}
