package com.servicecall.app.model;

import com.eswaraj.web.dto.BaseDto;

import lombok.Data;

/**
 * Created by Vaibhav on 6/14/2015.
 */
@Data
public class ComplaintCounter extends BaseDto {

    private String name;
    private Long count;

}
