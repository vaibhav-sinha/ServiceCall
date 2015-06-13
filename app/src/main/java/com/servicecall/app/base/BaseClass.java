package com.servicecall.app.base;

import com.servicecall.app.application.ServiceCallApplication;

/**
 * Created by Vaibhav on 6/14/2015.
 */
public class BaseClass {
    public BaseClass() {
        ServiceCallApplication.getApplication().getComponent().inject(this);
    }
}
