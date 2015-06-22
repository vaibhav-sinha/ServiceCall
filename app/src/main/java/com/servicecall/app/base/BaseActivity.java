package com.servicecall.app.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.servicecall.app.R;
import com.servicecall.app.activity.HomeActivity;
import com.servicecall.app.activity.SelectCategoryActivity;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.event.DummyEvent;

import java.lang.reflect.Method;

import javax.inject.Inject;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Vaibhav on 6/14/2015.
 */
public class BaseActivity extends AppCompatActivity {

    @Inject
    protected EventBus eventBus;

    private boolean isStarted = false;

    Intent i;

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
                return true;
            case R.id.menu_aboutus:
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
