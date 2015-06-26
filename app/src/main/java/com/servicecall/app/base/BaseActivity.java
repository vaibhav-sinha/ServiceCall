package com.servicecall.app.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.servicecall.app.R;
import com.servicecall.app.activity.BasketComplaintListActivity;
import com.servicecall.app.activity.MyIssuesListActivity;
import com.servicecall.app.activity.SelectCategoryActivity;
import com.servicecall.app.event.DummyEvent;
import com.servicecall.app.helper.BasketComplaintDAO;
import com.servicecall.app.helper.UpdateBasketComplaintCount;
import com.servicecall.app.model.Complaint;

import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Created by Vaibhav on 6/14/2015.
 */
public class BaseActivity extends AppCompatActivity {

    @Inject
    protected EventBus eventBus;

    private boolean isStarted = false;

    Intent i;

    int hot_number = 0;
    TextView ui_hot;
    ImageView hotlist_bell;
    ArrayList<Complaint> basketComplaints;
    BasketComplaintDAO basketComplaintDAO;

    int basketComplaintSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //eventBus.register(this);
        //ButterKnife.inject(this);
    }

    @Override
    protected void onDestroy() {
        //eventBus.unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isStarted = true;
    }

    @Override
    protected void onStop() {
        isStarted = false;
        super.onStop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);

        new UpdateBasketComplaintCount(this.getBaseContext(), menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_home:
                i = new Intent(getBaseContext(), SelectCategoryActivity.class);
                startActivity(i);
                return true;
            case R.id.menu_past_complaints:
                i = new Intent(getBaseContext(), MyIssuesListActivity.class);
                startActivity(i);
                return true;
            case R.id.menu_aboutus:
                return true;
            case R.id.menu_basket:
                i = new Intent(getBaseContext(), BasketComplaintListActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    public void onEventMainThread(DummyEvent event) {

    }
}
