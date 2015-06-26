package com.servicecall.app.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.servicecall.app.R;
import com.servicecall.app.activity.BasketComplaintListActivity;
import com.servicecall.app.model.BasketComplaint;

import java.util.ArrayList;

/**
 * Created by Shailendra on 6/25/2015.
 */
public class UpdateBasketComplaintCount {

    ArrayList<BasketComplaint> basketComplaints;
    BasketComplaintDAO basketComplaintDAO;
    int basketComplaintSize = 0;
    int hot_number = 0;
    TextView ui_hot;
    ImageView hotlist_bell;

    private Context mContext;
    Menu currentMenu;

    public UpdateBasketComplaintCount(Context context, Menu menu){
        super();
        this.mContext = context;
        this.currentMenu = menu;
        new GetBasketComplaints().execute();
    }

    public class GetBasketComplaints extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                basketComplaintDAO = new BasketComplaintDAO(mContext);
                basketComplaints = basketComplaintDAO.getAllBasketComplaints();
                basketComplaintSize = basketComplaints.size();
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            updateHotCount(basketComplaintSize);
        }
    }

    public void updateHotCount(final int new_hot_number) {

        MenuItem item = currentMenu.findItem(R.id.menu_basket);
        MenuItemCompat.setActionView(item, R.layout.action_bar_basket_icon);
        View view = MenuItemCompat.getActionView(item);
//        view.setPadding(0,0,0,0);
//        view.setMinimumWidth(0);
        ui_hot = (TextView) view.findViewById(R.id.hotlist_hot);
        hotlist_bell = (ImageView) view.findViewById(R.id.hotlist_bell);

        hot_number = new_hot_number;
        if (ui_hot == null) return;
        if (new_hot_number == 0) {
            ui_hot.setVisibility(View.INVISIBLE);
        } else {
            ui_hot.setVisibility(View.VISIBLE);
            ui_hot.setText(Integer.toString(new_hot_number));
        }

        new MyMenuItemStuffListener(view, basketComplaintSize + " complaints in Basket") {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, BasketComplaintListActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);

            }
        };

    }

    public class MyMenuItemStuffListener implements View.OnClickListener, View.OnLongClickListener {
        private String hint;
        private View view;

        public MyMenuItemStuffListener(View view, String hint) {
            this.view = view;
            this.hint = hint;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override public boolean onLongClick(View v) {
            final int[] screenPos = new int[2];
            final Rect displayFrame = new Rect();
            view.getLocationOnScreen(screenPos);
            view.getWindowVisibleDisplayFrame(displayFrame);
            final Context context = view.getContext();
            final int width = view.getWidth();
            final int height = view.getHeight();
            final int midy = screenPos[1] + height / 2;
            final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            Toast cheatSheet = Toast.makeText(context, hint, Toast.LENGTH_SHORT);
            if (midy < displayFrame.height()) {
                cheatSheet.setGravity(Gravity.TOP | Gravity.RIGHT,
                        screenWidth - screenPos[0] - width / 2, height);
            } else {
                cheatSheet.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, height);
            }
            cheatSheet.show();
            return true;
        }
    }


}
