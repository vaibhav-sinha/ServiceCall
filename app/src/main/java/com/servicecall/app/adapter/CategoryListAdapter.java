package com.servicecall.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.servicecall.app.R;
import com.servicecall.app.model.ComplaintCounter;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Vaibhav on 6/14/2015.
 */
public class CategoryListAdapter extends ArrayAdapter<CategoryWithChildCategoryDto> {

    private Context context;
    private int layoutResourceId;
    private List<CategoryWithChildCategoryDto> categoryList;
    private List<ComplaintCounter> complaintCounterList;
    private Long totalComplaints;
    private Map<Long, Integer> colorMap;

    public CategoryListAdapter(Context context, int layoutResourceId, List<CategoryWithChildCategoryDto> categoryList, List<ComplaintCounter> complaintCounterList, Map<Long, Integer> colorMap) {
        super(context, layoutResourceId, categoryList);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.categoryList = categoryList;
        this.complaintCounterList = complaintCounterList;
        this.colorMap = colorMap;

        totalComplaints = 0L;
        if(complaintCounterList != null) {
            for (ComplaintCounter complaintCounter : complaintCounterList) {
                totalComplaints += complaintCounter.getCount();
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CategoryDtoHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new CategoryDtoHolder(row);
            row.setTag(holder);
        }
        else
        {
            holder = (CategoryDtoHolder)row.getTag();
        }

        CategoryWithChildCategoryDto categoryDto = categoryList.get(position);
        String imageTitle = categoryDto.getImageUrl().split("\\.")[0];
        int drawableResourceId = context.getResources().getIdentifier(imageTitle, "drawable", context.getPackageName());
        holder.saTitle.setText(categoryDto.getName());
        //TODO: Fix this
        try {
            holder.saIcon.setImageDrawable(context.getResources().getDrawable(drawableResourceId));
        } catch (Exception e){
            holder.saIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.category4));
        }
        if (categoryDto.getColor() != null) {
            //holder.saWrapper.setBackgroundColor(Color.parseColor("#" + categoryDto.getColor()));
            switch (categoryDto.getId().intValue()){
                case 0:
                    holder.saWrapper.setBackgroundColor(Color.parseColor("#ED6F6A"));
                    break;
                case 1:
                    holder.saWrapper.setBackgroundColor(Color.parseColor("#44A3C8"));
                    break;
                case 2:
                    holder.saWrapper.setBackgroundColor(Color.parseColor("#CB9416"));
                    break;
                case 3:
                    holder.saWrapper.setBackgroundColor(Color.parseColor("#5A8658"));
                    break;
                case 4:
                    holder.saWrapper.setBackgroundColor(Color.parseColor("#986E84"));
                    break;
                case 5:
                    holder.saWrapper.setBackgroundColor(Color.parseColor("#FF496C"));
                    break;
                case 6:
                    holder.saWrapper.setBackgroundColor(Color.parseColor("#538195"));
                    break;
                case 7:
                    holder.saWrapper.setBackgroundColor(Color.parseColor("#FB8F3C"));
                    break;
                case 8:
                    holder.saWrapper.setBackgroundColor(Color.parseColor("#BBAA33"));
                    break;
                case 9:
                    holder.saWrapper.setBackgroundColor(Color.parseColor("#8C97F0"));
                    break;
                case 10:
                    holder.saWrapper.setBackgroundColor(Color.parseColor("#44A3C8"));
                    break;
                case 11:
                    holder.saWrapper.setBackgroundColor(Color.parseColor("#ED6F6A"));
                    break;
                case 12:
                    holder.saWrapper.setBackgroundColor(Color.parseColor("#5A8658"));
                    break;
                case 13:
                    holder.saWrapper.setBackgroundColor(Color.parseColor("#CB9416"));
                    break;
                case 14:
                    holder.saWrapper.setBackgroundColor(Color.parseColor("#FF496C"));
                    break;
                case 15:
                    holder.saWrapper.setBackgroundColor(Color.parseColor("#986E84"));
                    break;
                case 16:
                    holder.saWrapper.setBackgroundColor(Color.parseColor("#FB8F3C"));
                    break;
                default:
                    holder.saWrapper.setBackgroundColor(Color.parseColor("#B39F48"));
                    break;
            }
        } else {
            //holder.saWrapper.setBackgroundColor(colorMap.get(categoryDto.getId()));
            holder.saWrapper.setBackgroundColor(Color.parseColor("#0099cc"));
        }
        if(complaintCounterList != null) {
            for(ComplaintCounter complaintCounter : complaintCounterList) {
                if(complaintCounter.getId().equals(categoryDto.getId())) {
                    if(totalComplaints > 0) {
                        holder.saStats.setText(new DecimalFormat("#.##").format(complaintCounter.getCount().doubleValue() * 100 / totalComplaints) + "%");
                    }
                    else {
                        holder.saStats.setText(new DecimalFormat("#.##").format(0) + "%");
                    }
                    holder.saStats.setVisibility(View.VISIBLE);
                }
            }
        }

        return row;
    }

    static class CategoryDtoHolder
    {
        @InjectView(R.id.sc_iv_icon)
        ImageView saIcon;
        @InjectView(R.id.sc_tv_title)
        TextView saTitle;
        @InjectView(R.id.sc_tv_stats)
        TextView saStats;
        @InjectView(R.id.sc_rl_wrapper)
        RelativeLayout saWrapper;

        public CategoryDtoHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}

