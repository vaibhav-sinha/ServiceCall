package com.servicecall.app.config;

import com.eswaraj.web.dto.CategoryWithChildCategoryDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaibhav on 6/14/2015.
 */
public class LocalData {

    private static List<CategoryWithChildCategoryDto> categoryWithChildCategoryDtoList = new ArrayList<>();

    public static List<CategoryWithChildCategoryDto> getCategoryData() {
        createCategoryData();
        return categoryWithChildCategoryDtoList;
    }

    private static void createCategoryData() {
        CategoryWithChildCategoryDto category;

        //Baths list
        List<CategoryWithChildCategoryDto> bathsList = new ArrayList<>();
        category = new CategoryWithChildCategoryDto();
        category.setId(0L);
        category.setName("Water seeping between bath and wall");
        category.setParentCategoryId(4L);
        category.setRoot(false);
        category.setChildCategories(null);
        bathsList.add(category);

        category = new CategoryWithChildCategoryDto();
        category.setId(1L);
        category.setName("Timber end panel broken");
        category.setParentCategoryId(4L);
        category.setRoot(false);
        category.setChildCategories(null);
        bathsList.add(category);
        //Baths list ends

        //Showers list
        List<CategoryWithChildCategoryDto> showersList = new ArrayList<>();
        category = new CategoryWithChildCategoryDto();
        category.setId(2L);
        category.setName("Electric shower needs repair");
        category.setParentCategoryId(5L);
        category.setRoot(false);
        category.setChildCategories(null);
        showersList.add(category);

        category = new CategoryWithChildCategoryDto();
        category.setId(3L);
        category.setName("Shower spray head needs repair");
        category.setParentCategoryId(5L);
        category.setRoot(false);
        category.setChildCategories(null);
        showersList.add(category);
        //Showers list ends

        //Baths and Showers list
        List<CategoryWithChildCategoryDto> bathsAndShowersList = new ArrayList<>();
        category = new CategoryWithChildCategoryDto();
        category.setId(4L);
        category.setName("Baths");
        category.setParentCategoryId(6L);
        category.setRoot(false);
        category.setImageUrl("category4.jpg");
        category.setHeaderImageUrl("category4.h.jpg");
        category.setColor("BBCCDD");
        category.setChildCategories(bathsList);
        bathsAndShowersList.add(category);

        category = new CategoryWithChildCategoryDto();
        category.setId(5L);
        category.setName("Showers");
        category.setParentCategoryId(6L);
        category.setRoot(false);
        category.setImageUrl("category5.jpg");
        category.setHeaderImageUrl("category5.h.jpg");
        category.setColor("ABCDEF");
        category.setChildCategories(showersList);
        bathsAndShowersList.add(category);
        //Baths and Showers list ends

        category.setId(6L);
        category.setName("Baths and Showers");
        category.setParentCategoryId(null);
        category.setRoot(true);
        category.setImageUrl("category6.jpg");
        category.setHeaderImageUrl("category6.h.jpg");
        category.setColor("AA00FF");
        category.setChildCategories(bathsAndShowersList);
        categoryWithChildCategoryDtoList.add(category);

        /*category = new CategoryWithChildCategoryDto();
        category.setId(1L);
        category.setName("Communal");
        category.setParentCategoryId(null);
        category.setRoot(true);
        category.setImageUrl("category1.jpg");
        category.setHeaderImageUrl("category1.h.jpg");
        category.setColor("FF00AA");
        category.setChildCategories(null);
        categoryWithChildCategoryDtoList.add(category);*/

    }
}
