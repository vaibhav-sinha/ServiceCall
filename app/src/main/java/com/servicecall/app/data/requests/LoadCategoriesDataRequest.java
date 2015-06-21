package com.servicecall.app.data.requests;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.servicecall.app.application.ServiceCallApplication;
import com.servicecall.app.config.Constants;
import com.servicecall.app.event.GetCategoriesDataEvent;
import com.servicecall.app.model.ErrorDto;
import com.servicecall.app.util.NetworkAccessHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Created by Vaibhav on 6/21/2015.
 */
public class LoadCategoriesDataRequest {

    @Inject
    EventBus eventBus;
    @Inject
    NetworkAccessHelper networkAccessHelper;

    public LoadCategoriesDataRequest() {
        ServiceCallApplication.getApplication().getComponent().inject(this);
    }

    public void processRequest(Context context) {
        StringRequest request = new StringRequest(Constants.GET_CATEGORIES_URL, createSuccessListener(context), createErrorListener(context));
        this.networkAccessHelper.submitNetworkRequest("GetCategories", request);
    }

    private Response.ErrorListener createErrorListener(final Context context) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorDto errorDto = null;
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null) {
                    errorDto = new Gson().fromJson(new String(response.data), ErrorDto.class);
                }
                GetCategoriesDataEvent event = new GetCategoriesDataEvent();
                event.setSuccess(false);
                if(errorDto == null) {
                    event.setErrorMessage(error.toString());
                }
                else {
                    event.setErrorMessage(errorDto.getMessage());
                }
                eventBus.post(event);
            }
        };
    }

    private Response.Listener<String> createSuccessListener(final Context context) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                Gson gson = new Gson();
                try {
                    List<CategoryWithChildCategoryDto> categoryDtoList;
                    GetCategoriesDataEvent event = new GetCategoriesDataEvent();
                    categoryDtoList = gson.fromJson(json, new TypeToken<ArrayList<CategoryWithChildCategoryDto>>(){}.getType());
                    if(categoryDtoList == null || categoryDtoList.size() == 0) {
                        event.setSuccess(false);
                        eventBus.post(event);
                        return;
                    }
                    event.setSuccess(true);
                    event.setCategoryWithChildCategoryDtoList(categoryDtoList);
                    eventBus.post(event);
                } catch (JsonParseException e) {
                    GetCategoriesDataEvent event = new GetCategoriesDataEvent();
                    event.setSuccess(false);
                    event.setErrorMessage("Invalid data from server");
                    eventBus.postSticky(event);
                }
            }
        };
    }
}

