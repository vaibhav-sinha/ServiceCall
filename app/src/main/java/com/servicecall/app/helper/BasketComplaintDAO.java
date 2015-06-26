package com.servicecall.app.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.servicecall.app.model.BasketComplaint;

import java.util.ArrayList;

public class BasketComplaintDAO extends BasketComplaintDBDAO {

    private static final String WHERE_ID_EQUALS = BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_ID
            + " =?";

    private Context mContext;
    public BasketComplaintDAO(Context context) {
        super(context);
        this.mContext = context;
    }

    public long saveBasketComplaint(BasketComplaint basketComplaint) {
        ContentValues values = new ContentValues();

        values.put(BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_CATEGORY_ID, basketComplaint.getCategoryId());
        values.put(BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_ISSUE_DETAIL, basketComplaint.getIssueDetail());
        values.put(BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_ISSUE_PARENT, basketComplaint.getIssueParent());
        values.put(BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_ISSUE_PARENT_COLOR, basketComplaint.getIssueParentColor());
        values.put(BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_ISSUE_PARENT_IMAGE_URL, basketComplaint.getIssueParentImageUrl());
        values.put(BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_QUANTITY, basketComplaint.getQuantity());
        values.put(BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_DESCRIPTION, basketComplaint.getDescription());

        return database.insert(BasketComplaintsHelper.BASKET_COMPLAINTS_TABLE_NAME, null, values);
    }

    public long updateBasketComplaint(BasketComplaint basketComplaint) {
        ContentValues values = new ContentValues();

        values.put(BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_CATEGORY_ID, basketComplaint.getCategoryId());
        values.put(BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_ISSUE_DETAIL, basketComplaint.getIssueDetail());
        values.put(BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_ISSUE_PARENT, basketComplaint.getIssueParent());
        values.put(BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_ISSUE_PARENT_COLOR, basketComplaint.getIssueParentColor());
        values.put(BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_ISSUE_PARENT_IMAGE_URL, basketComplaint.getIssueParentImageUrl());
        values.put(BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_QUANTITY, basketComplaint.getQuantity());
        values.put(BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_DESCRIPTION, basketComplaint.getDescription());

        long result = database.update(BasketComplaintsHelper.BASKET_COMPLAINTS_TABLE_NAME, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(basketComplaint.getId()) });
        Log.d("Update Result:", "=" + result);
        //Toast.makeText(mContext, "" + result, Toast.LENGTH_LONG).show();

        return result;

    }

    public int removeBasketComplaint(BasketComplaint basketComplaint) {
        return database.delete(BasketComplaintsHelper.BASKET_COMPLAINTS_TABLE_NAME, WHERE_ID_EQUALS,
                new String[] { basketComplaint.getId() + "" });
    }

    //USING query() method
    public ArrayList<BasketComplaint> getAllBasketComplaints() {
        ArrayList<BasketComplaint> basketComplaints = new ArrayList<BasketComplaint>();

        Cursor cursor = database.query(BasketComplaintsHelper.BASKET_COMPLAINTS_TABLE_NAME,
                new String[] { BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_ID,
                        BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_CATEGORY_ID,
                        BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_ISSUE_DETAIL,
                        BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_ISSUE_PARENT,
                        BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_ISSUE_PARENT_COLOR,
                        BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_ISSUE_PARENT_IMAGE_URL,
                        BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_QUANTITY,
                        BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_DESCRIPTION }, null, null, null,
                null, null, null);

        while (cursor.moveToNext()) {
            BasketComplaint basketComplaint = new BasketComplaint ();
            basketComplaint.setId(cursor.getInt(0));
            basketComplaint.setCategoryId(cursor.getString(1));
            basketComplaint.setIssueDetail(cursor.getString(2));
            basketComplaint.setIssueParent(cursor.getString(3));
            basketComplaint.setIssueParentColor(cursor.getString(4));
            basketComplaint.setIssueParentImageUrl(cursor.getString(5));
            basketComplaint.setQuantity(cursor.getString(6));
            basketComplaint.setDescription(cursor.getString(7));
            basketComplaints.add(basketComplaint);
        }
        return basketComplaints;
    }

    //Retrieves a single employee record with the given id
    public BasketComplaint getBasketComplaint(long id) {
        BasketComplaint basketComplaint = null;

        String sql = "SELECT * FROM " + BasketComplaintsHelper.BASKET_COMPLAINTS_TABLE_NAME
                + " WHERE " + BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_ID + " = ?";

        Cursor cursor = database.rawQuery(sql, new String[] { id + "" });

        if (cursor.moveToNext()) {
            basketComplaint = new BasketComplaint ();
            basketComplaint.setId(cursor.getInt(0));
            basketComplaint.setId(cursor.getInt(0));
            basketComplaint.setCategoryId(cursor.getString(1));
            basketComplaint.setIssueDetail(cursor.getString(2));
            basketComplaint.setIssueParent(cursor.getString(3));
            basketComplaint.setIssueParentColor(cursor.getString(4));
            basketComplaint.setIssueParentImageUrl(cursor.getString(5));
            basketComplaint.setQuantity(cursor.getString(6));
            basketComplaint.setDescription(cursor.getString(7));
        }
        return basketComplaint;
    }

    //Retrieves a single employee record with the given id
    public BasketComplaint getBasketComplaintObjectByCategoryId(String categoryId) {
        BasketComplaint basketComplaint = null;

        String sql = "SELECT * FROM " + BasketComplaintsHelper.BASKET_COMPLAINTS_TABLE_NAME
                + " WHERE " + BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_CATEGORY_ID + " = ?";

        Cursor cursor = database.rawQuery(sql, new String[] { categoryId + "" });

        if (cursor.moveToNext()) {
            basketComplaint = new BasketComplaint ();
            basketComplaint.setId(cursor.getInt(0));
            basketComplaint.setId(cursor.getInt(0));
            basketComplaint.setCategoryId(cursor.getString(1));
            basketComplaint.setIssueDetail(cursor.getString(2));
            basketComplaint.setIssueParent(cursor.getString(3));
            basketComplaint.setIssueParentColor(cursor.getString(4));
            basketComplaint.setIssueParentImageUrl(cursor.getString(5));
            basketComplaint.setQuantity(cursor.getString(6));
            basketComplaint.setDescription(cursor.getString(7));
        }
        return basketComplaint;
    }

    //Retrieves a single employee record with the given id
    public boolean getBasketComplaintByCategoryId(String categoryId) {

        String sql = "SELECT * FROM " + BasketComplaintsHelper.BASKET_COMPLAINTS_TABLE_NAME
                + " WHERE " + BasketComplaintsHelper.BASKET_COMPLAINTS_COLUMN_CATEGORY_ID + " = ?";

        Cursor cursor = database.rawQuery(sql, new String[] { categoryId + "" });

        if (cursor != null && cursor.getCount()>0) {
            return true;
        } else {
            return false;
        }
    }

}
