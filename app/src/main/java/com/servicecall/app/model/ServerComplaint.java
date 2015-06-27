package com.servicecall.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

/**
 * Created by Vaibhav on 6/15/2015.
 */
@Data
public class ServerComplaint implements Parcelable {

    private int id;
    private int categoryId;
    private String issueDetail;
    private String issueParent;
    private String issueParentColor;
    private String issueParentImageUrl;
    private int quantity;
    private String description;
    private String homeLocation;
    private String latitude;
    private String longitude;
    private String reporterName;
    private String postcode;
    private String occupantType;
    private String propertyType;
    private String dayTelNum;
    private String workTelNum;
    private String email;
    private String mobNum;
    private String dayTimeAvailability;
    private String additionalInfo;

    public ServerComplaint() {
        super();
    }

    private ServerComplaint(Parcel in) {
        super();
        this.id = in.readInt();
        this.categoryId = in.readInt();
        this.issueDetail = in.readString();
        this.issueParent = in.readString();
        this.issueParentColor = in.readString();
        this.issueParentImageUrl = in.readString();
        this.quantity = in.readInt();
        this.description = in.readString();
        this.homeLocation = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.reporterName = in.readString();
        this.postcode = in.readString();
        this.occupantType = in.readString();
        this.propertyType = in.readString();
        this.dayTelNum = in.readString();
        this.workTelNum = in.readString();
        this.mobNum = in.readString();
        this.email = in.readString();
        this.dayTimeAvailability = in.readString();
        this.additionalInfo = in.readString();

    }

    @Override
    public String toString() {
        return "ServerComplaint [id=" + id + ", categoryId=" + categoryId + "," +
                " issueDetail=" + issueDetail + "," +
                " issueParent=" + issueParent + "," +
                " issueParentColor=" + issueParentColor + "," +
                " issueParentImageUrl=" + issueParentImageUrl + "," +
                " quantity=" + quantity + "," +
                " description=" + description + "," +
                " homeLocation=" + homeLocation + "," +
                " latitude=" + latitude + "," +
                " longitude=" + longitude + "," +
                " reporterName=" + reporterName + "," +
                " postcode=" + postcode + "," +
                " occupantType=" + occupantType + "," +
                " propertyType=" + propertyType + "," +
                " dayTelNum=" + dayTelNum + "," +
                " workTelNum=" + workTelNum + "," +
                " mobNum=" + mobNum + "," +
                " email=" + email + "," +
                " dayTimeAvailability=" + dayTimeAvailability + "," +
                " additionalInfo=" + additionalInfo + "]";

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServerComplaint other = (ServerComplaint) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(getId());
        parcel.writeInt(getCategoryId());
        parcel.writeString(getIssueDetail());
        parcel.writeString(getIssueParent());
        parcel.writeString(getIssueParentColor());
        parcel.writeString(getIssueParentImageUrl());
        parcel.writeInt(getQuantity());
        parcel.writeString(getDescription());
        parcel.writeString(getHomeLocation());
        parcel.writeString(getLatitude());
        parcel.writeString(getLongitude());
        parcel.writeString(getReporterName());
        parcel.writeString(getPostcode());
        parcel.writeString(getOccupantType());
        parcel.writeString(getPropertyType());
        parcel.writeString(getDayTelNum());
        parcel.writeString(getWorkTelNum());
        parcel.writeString(getMobNum());
        parcel.writeString(getEmail());
        parcel.writeString(getDayTimeAvailability());
        parcel.writeString(getAdditionalInfo());
    }

    public static final Parcelable.Creator<ServerComplaint> CREATOR = new Parcelable.Creator<ServerComplaint>() {
        public ServerComplaint createFromParcel(Parcel in) {
            return new ServerComplaint(in);
        }

        public ServerComplaint[] newArray(int size) {
            return new ServerComplaint[size];
        }
    };

}