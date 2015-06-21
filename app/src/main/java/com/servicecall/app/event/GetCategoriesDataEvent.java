package com.servicecall.app.event;

import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.servicecall.app.base.BaseEvent;

import java.util.List;

import lombok.Data;

/**
 * Created by Vaibhav on 6/21/2015.
 */
@Data
public class GetCategoriesDataEvent extends BaseEvent {
    private List<CategoryWithChildCategoryDto> categoryWithChildCategoryDtoList;
}
