package com.servicecall.app.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.servicecall.app.R;
import com.servicecall.app.activity.BasketComplaintListActivity;
import com.servicecall.app.activity.EditDetailsActivity;
import com.servicecall.app.activity.SelectCategoryActivity;
import com.servicecall.app.helper.BasketComplaintDAO;
import com.servicecall.app.model.BasketComplaint;

import java.util.List;

public class BasketComplaintListAdapter extends ArrayAdapter<BasketComplaint> {

    private Context context;
    List<BasketComplaint> basketComplaints;
    private BasketComplaintDAO basketComplaintDAO;
    BasketComplaint basketComplaint;
    private ProgressDialog progressDialog;
    Boolean removedFromBasket = false;

    public BasketComplaintListAdapter(Context context, List<BasketComplaint> basketComplaints) {
        super(context, R.layout.fragment_single_complaint_in_queue, basketComplaints);
        this.context = context;
        this.basketComplaints = basketComplaints;

    }

    private class ViewHolder {
        TextView basketComplaintDetails;
        TextView basketComplaintParent;
        TextView basketComplaintQuantity;
        TextView basketComplaintDescription;
        TextView basketComplaintDescriptionLabel;
        ImageView removeBasketComplaint;
        ImageView editBasketComplaint;
        ImageView basketComplaintParentIcon;
    }

    @Override
    public int getCount() {
        return basketComplaints.size();
    }

    @Override
    public BasketComplaint getItem(int position) {
        return basketComplaints.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_single_complaint_in_queue, null);
            holder = new ViewHolder();
            basketComplaintDAO = new BasketComplaintDAO(getContext());
            holder.basketComplaintParent = (TextView) convertView
                    .findViewById(R.id.basketComplaintParent);
            holder.basketComplaintDetails = (TextView) convertView
                    .findViewById(R.id.basketComplaintDetails);
            holder.basketComplaintQuantity = (TextView) convertView
                    .findViewById(R.id.basketComplaintQuantity);
            holder.basketComplaintDescription = (TextView) convertView
                    .findViewById(R.id.basketComplaintDescription);
            holder.basketComplaintDescriptionLabel = (TextView) convertView
                    .findViewById(R.id.basketComplaintDescriptionLabel);
            holder.editBasketComplaint = (ImageView) convertView
                    .findViewById(R.id.editBasketComplaint);
            holder.removeBasketComplaint = (ImageView) convertView
                    .findViewById(R.id.removeBasketComplaint);
            holder.basketComplaintParentIcon = (ImageView) convertView.
                    findViewById(R.id.fsc_iv_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final BasketComplaint basketComplaint = (BasketComplaint) getItem(position);
        holder.basketComplaintDetails.setText(basketComplaint.getIssueDetail());
        holder.basketComplaintParent.setText(basketComplaint.getIssueParent());
        holder.basketComplaintQuantity.setText("" + basketComplaint.getQuantity());
        String bgColor = "#" + basketComplaint.getIssueParentColor();

        int drawableResourceId = getContext().getResources().getIdentifier(basketComplaint.getIssueParentImageUrl().split("\\.")[0], "drawable", getContext().getPackageName());
        try{
            holder.basketComplaintParentIcon.setBackgroundColor(Color.parseColor(bgColor));
            holder.basketComplaintParentIcon.setImageDrawable(getContext().getResources().getDrawable(drawableResourceId));
        } catch (Exception e){
            holder.basketComplaintParentIcon.setBackgroundColor(Color.parseColor("#0099cc"));
            holder.basketComplaintParentIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.category4));
        }

        if(basketComplaint.getDescription().trim().isEmpty()){
            holder.basketComplaintDescription.setVisibility(View.GONE);
            holder.basketComplaintDescriptionLabel.setVisibility(View.GONE);
        } else {
            holder.basketComplaintDescription.setText(basketComplaint.getDescription());
        }

        holder.removeBasketComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder
                        .setTitle("Remove from Basket")
                        .setMessage("Are you sure you want to remove the complaint from Basket?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                remove(basketComplaint);
                                BasketComplaintDAO basketComplaintDAO;
                                basketComplaintDAO = new BasketComplaintDAO(getContext());
                                // Use AsyncTask to delete from database
                                basketComplaintDAO.removeBasketComplaint(basketComplaint);

                                if(context instanceof BasketComplaintListActivity){

                                    TextView basketComplaintsCount = (TextView) ((BasketComplaintListActivity)context).findViewById(R.id.basketComplaintsCount);
                                    basketComplaintsCount.setText("Total Complaints in Basket : " + basketComplaints.size());

                                }


                                if (basketComplaints.size() == 0) {
                                    final Toast toast = Toast.makeText(getContext(), "No Complaints in Basket", Toast.LENGTH_LONG);
                                    toast.show();

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            toast.cancel();
                                            Intent myIntent = new Intent(getContext(), SelectCategoryActivity.class);
                                            getContext().startActivity(myIntent);
                                        }
                                    }, 500);

                                } else {
//                                    Toast.makeText(getContext(), "" + basketComplaints.size(), Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
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

                if(context instanceof BasketComplaintListActivity){
                    TextView basketComplaintsCount = (TextView) ((BasketComplaintListActivity)context).findViewById(R.id.basketComplaintsCount);
                    basketComplaintsCount.setText("Total Complaints in Basket : " + String.valueOf(basketComplaints.size()));
                }

        }
        });

        holder.editBasketComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(context, EditDetailsActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("complaint", basketComplaint);
                myIntent.putExtras(mBundle);
                getContext().startActivity(myIntent);
            }
        });

        return convertView;
    }

    @Override
    public void add(BasketComplaint basketComplaint) {
        basketComplaints.add(basketComplaint);
        notifyDataSetChanged();
        super.add(basketComplaint);
    }

    @Override
    public void remove(BasketComplaint basketComplaint) {
        basketComplaints.remove(basketComplaint);
        notifyDataSetChanged();
        super.remove(basketComplaint);
    }

}