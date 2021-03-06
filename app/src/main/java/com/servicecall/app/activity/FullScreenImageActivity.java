package com.servicecall.app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.servicecall.app.R;
import com.servicecall.app.base.BaseActivity;
import com.servicecall.app.helper.BitmapWorkerTask;
import com.squareup.picasso.Picasso;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class FullScreenImageActivity extends BaseActivity {

    private ImageViewTouch fiImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);
        fiImage = (ImageViewTouch) findViewById(R.id.fiImage);
        fiImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        fiImage.setBackgroundColor(Color.parseColor("#00000000"));

        try {
            if (getIntent().getStringExtra("IMAGE").contains("https")) {
                Picasso.with(this).load(getIntent().getStringExtra("IMAGE")).into(fiImage);
            } else {
                new BitmapWorkerTask(fiImage, 500).execute(getIntent().getStringExtra("IMAGE"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
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

}
