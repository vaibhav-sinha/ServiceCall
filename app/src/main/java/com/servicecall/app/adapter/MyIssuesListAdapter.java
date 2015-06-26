package com.servicecall.app.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.servicecall.app.R;
import com.servicecall.app.helper.MyIssueDAO;
import com.servicecall.app.model.ServerComplaint;

import java.util.List;

public class MyIssuesListAdapter extends ArrayAdapter<ServerComplaint> {

    private Context context;
    List<ServerComplaint> myIssues;
    private MyIssueDAO myIssueDAO;
    ServerComplaint myIssue;
    private ProgressDialog progressDialog;
    Boolean removedFromBasket = false;

    public MyIssuesListAdapter(Context context, List<ServerComplaint> myIssues) {
        super(context, R.layout.fragment_single_complaint_in_queue, myIssues);
        this.context = context;
        this.myIssues = myIssues;

    }

    private class ViewHolder {
        TextView myIssueDetails;
        TextView myIssueParent;
        TextView myIssueQuantity;
        TextView myIssueDescription;
        TextView myIssueDescriptionLabel;
        ImageView myIssueParentIcon;
    }

    @Override
    public int getCount() {
        return myIssues.size();
    }

    @Override
    public ServerComplaint getItem(int position) {
        return myIssues.get(position);
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
            convertView = inflater.inflate(R.layout.fragment_single_complaint_in_my_issues, null);
            holder = new ViewHolder();

            holder.myIssueParent = (TextView) convertView
                    .findViewById(R.id.myIssueParent);
            holder.myIssueDetails = (TextView) convertView
                    .findViewById(R.id.myIssueDetails);
            holder.myIssueQuantity = (TextView) convertView
                    .findViewById(R.id.myIssueQuantity);
            holder.myIssueDescription = (TextView) convertView
                    .findViewById(R.id.myIssueDescription);
            holder.myIssueDescriptionLabel = (TextView) convertView
                    .findViewById(R.id.myIssueDescriptionLabel);
            holder.myIssueParentIcon = (ImageView) convertView
                    .findViewById(R.id.myIssueParentIcon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        myIssue = (ServerComplaint) getItem(position);
        holder.myIssueDetails.setText(myIssue.getIssueDetail());
        holder.myIssueParent.setText(myIssue.getIssueParent());
        holder.myIssueQuantity.setText("" + myIssue.getQuantity());
        if(myIssue.getDescription().isEmpty()){
            holder.myIssueDescription.setVisibility(View.GONE);
            holder.myIssueDescriptionLabel.setVisibility(View.GONE);
        } else {
            holder.myIssueDescription.setText(myIssue.getDescription());
        }

        String bgColor = "#" + myIssue.getIssueParentColor();

        int drawableResourceId = getContext().getResources().getIdentifier(myIssue.getIssueParentImageUrl().split("\\.")[0], "drawable", getContext().getPackageName());
        try{
            holder.myIssueParentIcon.setBackgroundColor(Color.parseColor(bgColor));
            holder.myIssueParentIcon.setImageDrawable(getContext().getResources().getDrawable(drawableResourceId));
        } catch (Exception e){
            holder.myIssueParentIcon.setBackgroundColor(Color.parseColor("#0099cc"));
            holder.myIssueParentIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.category4));
        }

        return convertView;
    }

    @Override
    public void add(ServerComplaint myIssue) {
        myIssues.add(myIssue);
        notifyDataSetChanged();
        super.add(myIssue);
    }

    @Override
    public void remove(ServerComplaint myIssue) {
        myIssues.remove(myIssue);
        notifyDataSetChanged();
        super.remove(myIssue);
    }

}