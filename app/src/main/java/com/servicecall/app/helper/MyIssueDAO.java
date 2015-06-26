package com.servicecall.app.helper;

import com.servicecall.app.model.ServerComplaint;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shailendra on 6/24/2015.
 */
public class MyIssueDAO {

    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static String url_create_complaint = "https://interfinderdemo-bbagentapp.rhcloud.com/create_complaint.php";
    private static final String url_get_complaint_details = "https://interfinderdemo-bbagentapp.rhcloud.com/get_complaint_details.php";
    private static String url_get_all_complaints = "https://interfinderdemo-bbagentapp.rhcloud.com/get_all_complaints.php";
    private static final String TAG_COMPLAINTS = "complaints";
    private static final String TAG_PID = "pid";
    private static final String TAG_CATEGORY_ID = "categoryId";
    private static final String TAG_ISSUE_DETAIL = "issueDetail";
    private static final String TAG_ISSUE_PARENT = "issueParent";
    private static final String TAG_ISSUE_PARENT_COLOR = "issueParentColor";
    private static final String TAG_ISSUE_PARENT_IMAGE_URL = "issueParentImageUrl";
    private static final String TAG_QUANTITY = "quantity";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_HOME_LOCATION = "homeLocation";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_REPORTER_NAME = "reporterName";
    private static final String TAG_POSTCODE = "postcode";
    private static final String TAG_OCCUPANT_TYPE = "occupantType";
    private static final String TAG_PROPERTY_TYPE = "propertyType";
    private static final String TAG_DAY_TEL_NUM = "dayTelNum";
    private static final String TAG_WORK_TEL_NUM = "workTelNum";
    private static final String TAG_MOB_NUM = "mobNum";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_DAY_TIME_AVAILABILITY = "dayTimeAvailability";
    private static final String TAG_ADDITIONAL_INFO = "additionalInfo";

    JSONArray complaints = null;
    String id;
    String categoryId;
    String issueDetail;
    String issueParent;
    String issueParentColor;
    String issueParentImageUrl;
    String quantity;
    String description;
    String homeLocation;
    String latitude;
    String longitude;
    String postcode;
    String occupantType;
    String propertyType;
    String dayTelNum;
    String workTelNum;
    String mobNum;
    String email;
    String dayTimeAvailability;
    String additionalInfo;


    int success;
    ArrayList<ServerComplaint> myIssuesList = new ArrayList<>();

    public boolean saveMyIssues(ArrayList<ServerComplaint> myIssues) {
        for(ServerComplaint myIssue : myIssues) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("categoryId", String.valueOf(myIssue.getCategoryId())));
            params.add(new BasicNameValuePair("issueDetail", myIssue.getIssueDetail()));
            params.add(new BasicNameValuePair("issueParent", myIssue.getIssueParent()));
            params.add(new BasicNameValuePair("issueParentColor", myIssue.getIssueParentColor()));
            params.add(new BasicNameValuePair("issueParentImageUrl", myIssue.getIssueParentImageUrl()));
            params.add(new BasicNameValuePair("quantity", String.valueOf(myIssue.getQuantity())));
            params.add(new BasicNameValuePair("description", myIssue.getDescription()));
            params.add(new BasicNameValuePair("homeLocation", myIssue.getDescription()));
            params.add(new BasicNameValuePair("latitude", myIssue.getLatitude()));
            params.add(new BasicNameValuePair("longitude", myIssue.getLongitude()));
            params.add(new BasicNameValuePair("postcode", myIssue.getPostcode()));
            params.add(new BasicNameValuePair("occupantType", myIssue.getOccupantType()));
            params.add(new BasicNameValuePair("propertyType", myIssue.getPropertyType()));
            params.add(new BasicNameValuePair("dayTelNum", myIssue.getDayTelNum()));
            params.add(new BasicNameValuePair("workTelNum", myIssue.getWorkTelNum()));
            params.add(new BasicNameValuePair("mobNum", myIssue.getMobNum()));
            params.add(new BasicNameValuePair("email", myIssue.getEmail()));
            params.add(new BasicNameValuePair("dayTimeAvailability", myIssue.getDayTimeAvailability()));
            params.add(new BasicNameValuePair("additionalInfo", myIssue.getAdditionalInfo()));

            JSONObject json = jsonParser.makeHttpRequest(url_create_complaint,
                    "POST", params);
            //Log.d("Create Response", json.toString());
            try {
                success = json.getInt(TAG_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (success == 1) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<ServerComplaint> getAllMyIssues() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(url_get_all_complaints, "GET", params);

        // Check your log cat for JSON reponse
        // Log.d("All Products: ", json.toString());

        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // products found
                // Getting Array of Products
                complaints = json.getJSONArray(TAG_COMPLAINTS);

                // looping through All Products
                for (int i = 0; i < complaints.length(); i++) {
                    JSONObject c = complaints.getJSONObject(i);

                    // Storing each json item in variable
                    id = c.getString(TAG_PID);
                    categoryId = c.getString(TAG_CATEGORY_ID);
                    issueDetail = c.getString(TAG_ISSUE_DETAIL);
                    issueParent = c.getString(TAG_ISSUE_PARENT);
                    issueParentColor = c.getString(TAG_ISSUE_PARENT_COLOR);
                    issueParentImageUrl = c.getString(TAG_ISSUE_PARENT_IMAGE_URL);
                    quantity = c.getString(TAG_QUANTITY);
                    description = c.getString(TAG_DESCRIPTION);
                    homeLocation = c.getString(TAG_HOME_LOCATION);
                    latitude = c.getString(TAG_LATITUDE);
                    longitude = c.getString(TAG_LONGITUDE);
                    postcode = c.getString(TAG_POSTCODE);
                    occupantType = c.getString(TAG_OCCUPANT_TYPE);
                    propertyType = c.getString(TAG_PROPERTY_TYPE);
                    dayTelNum = c.getString(TAG_DAY_TEL_NUM);
                    workTelNum = c.getString(TAG_WORK_TEL_NUM);
                    mobNum = c.getString(TAG_MOB_NUM);
                    email = c.getString(TAG_EMAIL);
                    dayTimeAvailability = c.getString(TAG_DAY_TIME_AVAILABILITY);
                    additionalInfo = c.getString(TAG_ADDITIONAL_INFO);

                    ServerComplaint serverComplaint = new ServerComplaint();
                    serverComplaint.setCategoryId(Integer.valueOf(categoryId));
                    serverComplaint.setIssueDetail(issueDetail);
                    serverComplaint.setIssueParent(issueParent);
                    serverComplaint.setIssueParentColor(issueParentColor);
                    serverComplaint.setIssueParentImageUrl(issueParentImageUrl);
                    serverComplaint.setQuantity(Integer.valueOf(quantity));
                    serverComplaint.setDescription(description);
                    serverComplaint.setHomeLocation(homeLocation);
                    serverComplaint.setLatitude(latitude);
                    serverComplaint.setLongitude(longitude);
                    serverComplaint.setPostcode(postcode);
                    serverComplaint.setOccupantType(occupantType);
                    serverComplaint.setPropertyType(propertyType);
                    serverComplaint.setDayTelNum(dayTelNum);
                    serverComplaint.setWorkTelNum(workTelNum);
                    serverComplaint.setMobNum(mobNum);
                    serverComplaint.setEmail(email);
                    serverComplaint.setDayTimeAvailability(dayTimeAvailability);
                    serverComplaint.setAdditionalInfo(additionalInfo);
                    myIssuesList.add(serverComplaint);
                }
            } else {
                // no products found
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return myIssuesList;
    }

}
