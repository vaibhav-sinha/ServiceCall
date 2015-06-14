package com.servicecall.app.event;

import com.servicecall.app.base.BaseEvent;

import lombok.Data;

/**
 * Created by Vaibhav on 6/14/2015.
 */
@Data
public class RevGeocodeEvent extends BaseEvent {

    private String revGeocodedLocation;
    private String revGeocodedFullData;

}
