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
        int drawableResourceId = context.getResources().getIdentifier(categoryDto.getImageUrl(), "drawable", context.getPackageName());
        holder.saTitle.setText(categoryDto.getName());
        //TODO: Fix this
        //holder.saIcon.setImageDrawable(context.getResources().getDrawable(drawableResourceId));
        if (categoryDto.getColor() != null) {
            holder.saWrapper.setBackgroundColor(Color.parseColor("#" + categoryDto.getColor()));
        } else {
            holder.saWrapper.setBackgroundColor(colorMap.get(categoryDto.getId()));
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

