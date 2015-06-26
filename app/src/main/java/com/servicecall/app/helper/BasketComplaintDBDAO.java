package com.servicecall.app.helper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BasketComplaintDBDAO {

    protected SQLiteDatabase database;
    private BasketComplaintsHelper dbHelper;
    private Context mContext;

    public BasketComplaintDBDAO(Context context) {
        this.mContext = context;
        dbHelper = BasketComplaintsHelper.getHelper(mContext);
        open();

    }

    public void open() throws SQLException {
        if(dbHelper == null)
            dbHelper = BasketComplaintsHelper.getHelper(mContext);
        database = dbHelper.getWritableDatabase();
    }
	
	/*public void close() {
		dbHelper.close();
		database = null;
	}*/
}
