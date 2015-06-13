package com.servicecall.app.base;

import lombok.Data;

/**
 * Created by Vaibhav on 6/14/2015.
 */
@Data
public class BaseEvent {

    private boolean success;
    private String errorMessage;
}
