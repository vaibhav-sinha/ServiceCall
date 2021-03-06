package com.servicecall.app.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.servicecall.app.R;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseFragment;
import com.servicecall.app.data.api.DataApi;
import com.servicecall.app.event.GetCategoriesDataEvent;
import com.servicecall.app.event.SetupDoneEvent;
import com.servicecall.app.services.InternetServiceManager;
import com.servicecall.app.util.Session;

import javax.inject.Inject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SplashFragment extends BaseFragment {

    @Inject
    Session session;
    @Inject
    DataApi dataApi;

    Boolean isInternetPresent =  false, screenVisible = false;
    private Toast toast1, toast2;

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
            getActivity().findViewById(R.id.progressBar).setVisibility(View.GONE);
        }
        else {
            getActivity().findViewById(R.id.progressBar).setVisibility(View.GONE);
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Could not get app setup data from internet. Check if your internet connection works")
                    .show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        screenVisible = true;
        isInternetPresent = new NetworkChangeReceiver().checkInternet(getActivity());
        if (isInternetPresent) {
            toast1 = Toast.makeText(getActivity(), "Loading Categories, Please wait ...", Toast.LENGTH_LONG);
            toast1.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(screenVisible)
                        toast1.cancel();
                }
            }, 4000);
            dataApi.loadCategoriesData(getActivity());
        } else {
            getActivity().findViewById(R.id.progressBar).setVisibility(View.GONE);
            final Toast toast = Toast.makeText(getActivity(), "Internet Connection Unavailable. Please connect to Internet to proceed further", Toast.LENGTH_SHORT);
            toast.show();
            getActivity().registerReceiver(
                    new NetworkChangeReceiver(),
                    new IntentFilter(
                            ConnectivityManager.CONNECTIVITY_ACTION));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 3000);
        }
    }

    private class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            if(checkInternet(context))
            {
                toast2 = Toast.makeText(getActivity(), "Loading Categories, Please wait ...", Toast.LENGTH_LONG);
                toast2.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(screenVisible)
                            toast2.cancel();
                    }
                }, 4000);
                dataApi.loadCategoriesData(getActivity());
            }
        }

        boolean checkInternet(Context context) {
            InternetServiceManager internetServiceManager = new InternetServiceManager(context);
            if (internetServiceManager.isNetworkAvailable()) {
                return true;
            } else {
                return false;
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        screenVisible = false;
        try {
            if(toast1 != null)
                toast1.cancel();
            if(toast2 != null)
                toast2.cancel();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
