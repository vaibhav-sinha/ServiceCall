package com.servicecall.app.helper;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BasketComplaintsHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 11;

    public static final String BASKET_COMPLAINTS_DATABASE_NAME = "MyBasketComplaintList.db";
    public static final String BASKET_COMPLAINTS_TABLE_NAME = "complaints";
    public static final String BASKET_COMPLAINTS_COLUMN_ID = "id";
    public static final String BASKET_COMPLAINTS_COLUMN_CATEGORY_ID = "categoryId";
    public static final String BASKET_COMPLAINTS_COLUMN_ISSUE_DETAIL = "issueDetail";
    public static final String BASKET_COMPLAINTS_COLUMN_ISSUE_PARENT = "issueParent";
    public static final String BASKET_COMPLAINTS_COLUMN_ISSUE_PARENT_COLOR = "issueParentColor";
    public static final String BASKET_COMPLAINTS_COLUMN_ISSUE_PARENT_IMAGE_URL = "issueParentImageUrl";
    public static final String BASKET_COMPLAINTS_COLUMN_QUANTITY = "quantity";
    public static final String BASKET_COMPLAINTS_COLUMN_DESCRIPTION = "decsription";

    public static final String CREATE_BASKET_COMPLAINTS_TABLE = "CREATE TABLE "
            + BASKET_COMPLAINTS_TABLE_NAME + "(" + BASKET_COMPLAINTS_COLUMN_ID + " INTEGER PRIMARY KEY, "
            + BASKET_COMPLAINTS_COLUMN_CATEGORY_ID + " TEXT, "
            + BASKET_COMPLAINTS_COLUMN_ISSUE_DETAIL + " TEXT, "
            + BASKET_COMPLAINTS_COLUMN_ISSUE_PARENT + " TEXT, "
            + BASKET_COMPLAINTS_COLUMN_ISSUE_PARENT_COLOR + " TEXT, "
            + BASKET_COMPLAINTS_COLUMN_ISSUE_PARENT_IMAGE_URL + " TEXT, "
            + BASKET_COMPLAINTS_COLUMN_QUANTITY + " TEXT, "
            + BASKET_COMPLAINTS_COLUMN_DESCRIPTION + " TEXT" + ")";

    private static BasketComplaintsHelper instance;

    public static synchronized BasketComplaintsHelper getHelper(Context context) {
        if (instance == null)
            instance = new BasketComplaintsHelper(context);
        return instance;
    }

    public BasketComplaintsHelper(Context context) {
        super(context, BASKET_COMPLAINTS_DATABASE_NAME, null, DATABASE_VERSION);
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, BASKET_COMPLAINTS_TABLE_NAME);
        return numRows;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BASKET_COMPLAINTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}