package com.servicecall.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.servicecall.app.R;
import com.servicecall.app.base.BaseActivity;
import com.servicecall.app.fragment.BasketComplaintListFragment;

public class BasketComplaintListActivity extends BaseActivity {


    private Fragment contentFragment;
    private BasketComplaintListFragment basketComplaintListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_details);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            if (fragmentManager.findFragmentByTag(BasketComplaintListFragment.ARG_ITEM_ID) != null) {
                basketComplaintListFragment = (BasketComplaintListFragment) fragmentManager
                        .findFragmentByTag(BasketComplaintListFragment.ARG_ITEM_ID);
                contentFragment = basketComplaintListFragment;
            }
        } else {
            basketComplaintListFragment = new BasketComplaintListFragment();
            switchContent(basketComplaintListFragment, BasketComplaintListFragment.ARG_ITEM_ID);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_compressed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("content", BasketComplaintListFragment.ARG_ITEM_ID);
        super.onSaveInstanceState(outState);
    }

    /*
     * We consider EmpListFragment as the home fragment and it is not added to
     * the back stack.
     */
    public void switchContent(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.popBackStackImmediate())
            ;

        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager
                    .beginTransaction();
            transaction.replace(R.id.content_frame, fragment, tag);
            // Only EmpAddFragment is added to the back stack.
            if (!(fragment instanceof BasketComplaintListFragment)) {
                transaction.addToBackStack(tag);
            }
            transaction.commit();
            contentFragment = fragment;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

