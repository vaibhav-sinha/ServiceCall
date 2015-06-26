package com.servicecall.app.model;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by Vaibhav on 6/15/2015.
 */
@Data
public class Complaint implements Serializable {

    private long categoryId;
    private String issueParent;
    private String issueDetail;
    private int quantity;
    private String description;
}
