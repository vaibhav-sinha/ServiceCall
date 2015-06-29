package com.servicecall.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.servicecall.app.R;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseActivity;
import com.servicecall.app.event.ComplaintSubmitOrDiscardEvent;
import com.servicecall.app.fragment.EditDetailsFragment;

import butterknife.ButterKnife;

public class EditDetailsActivity extends BaseActivity {

    private EditDetailsFragment editDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);
        ButterKnife.inject(this);
        ServiceCallApplication.getApplication().getComponent().inject(this);
        eventBus.register(this);

        editDetailsFragment = new EditDetailsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.ad_fl_container, editDetailsFragment).commit();
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
        editDetailsFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            editDetailsFragment.askUserToSaveChanges(BasketComplaintListActivity.class);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                editDetailsFragment.askUserToSaveChanges(BasketComplaintListActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
