package com.servicecall.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

/**
 * Created by Vaibhav on 6/15/2015.
 */
@Data
public class BasketComplaint implements Parcelable {

    private int id;
    private String categoryId;
    private String issueDetail;
    private String issueParent;
    private String issueParentColor;
    private String issueParentImageUrl;
    private String quantity;
    private String description;

    public BasketComplaint() {
        super();
    }

    private BasketComplaint(Parcel in) {
        super();
        this.id = in.readInt();
        this.categoryId = in.readString();
        this.issueDetail = in.readString();
        this.issueParent = in.readString();
        this.issueParentColor = in.readString();
        this.issueParentImageUrl = in.readString();
        this.quantity = in.readString();
        this.description = in.readString();
    }

    @Override
    public String toString() {
        return "BasketComplaint [id=" + id + ", categoryId=" + categoryId + "," +
                " issueDetail=" + issueDetail + "," +
                " issueParent=" + issueParent + "," +
                " issueParentColor=" + issueParentColor + "," +
                " issueParentImageUrl=" + issueParentImageUrl + "," +
                " quantity=" + quantity + "," +
                " description=" + description + "]";
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
        BasketComplaint other = (BasketComplaint) obj;
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
        parcel.writeString(getCategoryId());
        parcel.writeString(getIssueDetail());
        parcel.writeString(getIssueParent());
        parcel.writeString(getIssueParentColor());
        parcel.writeString(getIssueParentImageUrl());
        parcel.writeString(getQuantity());
        parcel.writeString(getDescription());
    }

    public static final Parcelable.Creator<BasketComplaint> CREATOR = new Parcelable.Creator<BasketComplaint>() {
        public BasketComplaint createFromParcel(Parcel in) {
            return new BasketComplaint(in);
        }

        public BasketComplaint[] newArray(int size) {
            return new BasketComplaint[size];
        }
    };

}