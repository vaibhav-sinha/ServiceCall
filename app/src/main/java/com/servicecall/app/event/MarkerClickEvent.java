package com.servicecall.app.event;

import com.eswaraj.web.dto.ComplaintDto;
import com.servicecall.app.base.BaseEvent;

import lombok.Data;

/**
 * Created by Vaibhav on 6/14/2015.
 */
@Data
public class MarkerClickEvent extends BaseEvent {
    private ComplaintDto complaintDto;
}
