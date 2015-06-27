package com.servicecall.app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Button;

import com.servicecall.app.R;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.base.BaseActivity;
import com.servicecall.app.event.CategorySelectEvent;
import com.servicecall.app.fragment.SelectCategoryFragment;

import butterknife.ButterKnife;

public class SelectCategoryActivity extends BaseActivity {

    private SelectCategoryFragment selectCategoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);
        ButterKnife.inject(this);
        ServiceCallApplication.getApplication().getComponent().inject(this);
        eventBus.register(this);

        selectCategoryFragment = new SelectCategoryFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.sc_fl_container, selectCategoryFragment).commit();
    }

    @Override
    protected void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

    public void onEventMainThread(CategorySelectEvent event) {
        if (event.isSuccess()) {
            Intent i;
            if (event.getCategoryWithChildCategoryDto().getChildCategories().get(0).getChildCategories() != null) {
                i = new Intent(this, SelectSubCategoryActivity.class);
                i.putExtra("subCategory", event.getCategoryWithChildCategoryDto());
            } else {
                i = new Intent(this, SelectComplaintActivity.class);
                i.putExtra("complaintList", event.getCategoryWithChildCategoryDto());
            }
            startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitFromApp();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                exitFromApp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

public void exitFromApp(){
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setTitle("Close Interfinder!");
    alertDialogBuilder
            .setMessage("Do you want to exit?")
            .setCancelable(false)
            .setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
    Button posB = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
    Button negB = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
    posB.setBackgroundResource(R.drawable.blue_dark_blue_highlight);
    posB.setTextColor(Color.WHITE);
    posB.setTransformationMethod(null);
    negB.setTransformationMethod(null);

}
}

