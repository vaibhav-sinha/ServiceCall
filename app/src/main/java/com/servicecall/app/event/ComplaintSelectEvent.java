package com.servicecall.app.event;

import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.servicecall.app.base.BaseEvent;

import lombok.Data;

/**
 * Created by Vaibhav on 6/15/2015.
 */
@Data
public class ComplaintSelectEvent extends BaseEvent {
    private CategoryWithChildCategoryDto categoryWithChildCategoryDto;
}
