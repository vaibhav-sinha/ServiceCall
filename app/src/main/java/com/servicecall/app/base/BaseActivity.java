package com.servicecall.app.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.servicecall.app.R;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.event.DummyEvent;

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEventMainThread(DummyEvent event) {

    }
}
