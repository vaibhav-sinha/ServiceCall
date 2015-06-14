package com.servicecall.app.config;

import com.eswaraj.web.dto.CategoryWithChildCategoryDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaibhav on 6/14/2015.
 */
public class LocalData {

    private static List<CategoryWithChildCategoryDto> categoryWithChildCategoryDtoList;

    public static List<CategoryWithChildCategoryDto> getCategoryData() {
        createCategoryData();
        return categoryWithChildCategoryDtoList;
    }

    private static void createCategoryData() {
        categoryWithChildCategoryDtoList = new ArrayList<>();
        CategoryWithChildCategoryDto category = new CategoryWithChildCategoryDto();

        category.setId(0L);
        category.setName("Baths and Showers");
        category.setParentCategoryId(null);
        category.setRoot(true);
        category.setImageUrl("category0.jpg");
        category.setHeaderImageUrl("category0.h.jpg");
        category.setColor("AA00FF");
        category.setChildCategories(null);
        categoryWithChildCategoryDtoList.add(category);

        category = new CategoryWithChildCategoryDto();
        category.setId(1L);
        category.setName("Communal");
        category.setParentCategoryId(null);
        category.setRoot(true);
        category.setImageUrl("category1.jpg");
        category.setHeaderImageUrl("category1.h.jpg");
        category.setColor("FF00AA");
        category.setChildCategories(null);
        categoryWithChildCategoryDtoList.add(category);
    }
}
