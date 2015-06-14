package com.servicecall.app.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.event.DummyEvent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Vaibhav on 6/14/2015.
 */
public class BaseFragment extends Fragment {

    @Inject
    protected EventBus eventBus;

    private boolean isStarted = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        //eventBus.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        isStarted = true;
    }

    @Override
    public void onStop() {
        isStarted = false;
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onEventMainThread(DummyEvent event) {

    }
}
