package com.servicecall.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;

import com.servicecall.app.R;
import com.servicecall.app.base.BaseActivity;
import com.servicecall.app.fragment.MyIssuesListFragment;

public class MyIssuesListActivity extends BaseActivity {


    private Fragment contentFragment;
    private MyIssuesListFragment myissuesListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_issues);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            if (fragmentManager.findFragmentByTag(MyIssuesListFragment.ARG_ITEM_ID) != null) {
                myissuesListFragment = (MyIssuesListFragment) fragmentManager
                        .findFragmentByTag(MyIssuesListFragment.ARG_ITEM_ID);
                contentFragment = myissuesListFragment;
            }
        } else {
            myissuesListFragment = new MyIssuesListFragment();
            switchContent(myissuesListFragment, MyIssuesListFragment.ARG_ITEM_ID);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_compressed, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("content", MyIssuesListFragment.ARG_ITEM_ID);
        super.onSaveInstanceState(outState);
    }

    public void switchContent(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.popBackStackImmediate())
            ;

        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager
                    .beginTransaction();
            transaction.replace(R.id.content_frame, fragment, tag);
            if (!(fragment instanceof MyIssuesListFragment)) {
                transaction.addToBackStack(tag);
            }
            transaction.commit();
            contentFragment = fragment;
        }
    }

}

