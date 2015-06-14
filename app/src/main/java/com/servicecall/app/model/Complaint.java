package com.servicecall.app.model;

import lombok.Data;

/**
 * Created by Vaibhav on 6/15/2015.
 */
@Data
public class Complaint {

    private Long categoryId;
    private Integer count;
    private String description;
    private String homeLocation;
    private Double latitude;
    private Double longitude;
}
