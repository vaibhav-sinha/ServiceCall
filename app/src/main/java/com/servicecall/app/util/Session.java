package com.servicecall.app.util;

import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.servicecall.app.application.ServiceCallApplication;

import java.util.List;

import lombok.Data;

/**
 * Created by Vaibhav on 6/14/2015.
 */
@Data
public class Session {

    public Session() {
        ServiceCallApplication.getApplication().getComponent().inject(this);
    }

    private List<CategoryWithChildCategoryDto> categoryWithChildCategoryDtoList;
    private String userRevGeocodedLocation;
}
