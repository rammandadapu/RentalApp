package edu.sjsu.cmpe277.rentalapp.localdbmanager;

/**
 * Created by divya.chittimalla on 4/26/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;


public class DBHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 8;
    private static final String DB = "rentalapp.db";
    private static final String TABLE = "favorites";
    public static final String TABLE_PROPERTY_ID = "_id";
    public static final String TABLE_PROPERTY_NAME = "name";
    public static final String TABLE_PROPERTY_ADDRESS = "address";
    public static final String TABLE_PROPERTY_ADDRESSLINE1 = "line1";
    public static final String TABLE_PROPERTY_ADDRESSCITY = "city";
    public static final String TABLE_PROPERTY_ADDRESSSTATE = "state";
    public static final String TABLE_PROPERTY_ADDRESSZIP = "zip";
    public static final String TABLE_PROPERTY_TITLE="name";
    public static final String TABLE_PROPERTY_PRICE = "price";
    public static final String TABLE_PROPERTY_BEDBATH = "bed_bath";
    public static final String TABLE_PROPERTY_BED = "bedNo";
    public static final String TABLE_PROPERTY_BATH = "bathNo";
    public static final String TABLE_PROPERTY_IMAGE_URL = "image_url";
    public static final String TABLE_PROPERTY_CREATEDBY = "createdBy";
    public static final String TABLE_PROPERTY_STATUS="status";


    public static final String TABLE_PROPERTY_SIZE = "size";
    public static final String TABLE_PROPERTY_TYPE = "type";
    public static final String TABLE_PROPERTY_DESC = "desc";
    public static final String TABLE_PROPERTY_EMAIL = "email";
    public static final String TABLE_PROPERTY_PHONE = "phone";
    public static final String TABLE_PROPERTY_VIEWCOUNT = "viewCount";


    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB, factory, DB_VERSION);
    }

    /**
     * Creates the DB
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + TABLE + " ( "
                + TABLE_PROPERTY_ID + " TEXT PRIMARY KEY,"
                + TABLE_PROPERTY_ADDRESS + " TEXT,"
                + TABLE_PROPERTY_PRICE + " TEXT,"
                + TABLE_PROPERTY_BEDBATH + " TEXT,"
                + TABLE_PROPERTY_IMAGE_URL + " TEXT,"
                + TABLE_PROPERTY_CREATEDBY + " TEXT"
                + ")";
        db.execSQL(query);
    }

    /**
     * Creates new table if version number changes
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE);
        onCreate(db);
    }

    /**
     * Add the property
     *
     * @param property
     */
    public void addProperty(RentalProperty property) {

        ContentValues values = new ContentValues();
        values.put(TABLE_PROPERTY_ID, property.get_id());
        values.put(TABLE_PROPERTY_ADDRESS, property.getAddress());
        values.put(TABLE_PROPERTY_PRICE, property.getPrice());
        values.put(TABLE_PROPERTY_BEDBATH, property.getBedBath());
        values.put(TABLE_PROPERTY_IMAGE_URL, property.getImage_url());
        values.put(TABLE_PROPERTY_CREATEDBY, property.getCreatedBy());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE, null, values);
        db.close();
    }

    public void deleteProperty(String _id) {

        String query = "delete  from " + TABLE + " where " + TABLE_PROPERTY_ID + " like '" + _id + "'";
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(query);

    }

    /**
     * Return list of all favourite properties. If nothing exist returns empty list.
     *
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllProperties() {

        String query = "select * from " + TABLE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        ArrayList<HashMap<String, String>> propertyList = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            HashMap<String, String> property = new HashMap<>();
            property.put(TABLE_PROPERTY_ID, cursor.getString(cursor.getColumnIndex(TABLE_PROPERTY_ID)));
            property.put(TABLE_PROPERTY_ADDRESS, cursor.getString(cursor.getColumnIndex(TABLE_PROPERTY_ADDRESS)));
            property.put(TABLE_PROPERTY_PRICE, cursor.getString(cursor.getColumnIndex(TABLE_PROPERTY_PRICE)));
            property.put(TABLE_PROPERTY_BEDBATH, cursor.getString(cursor.getColumnIndex(TABLE_PROPERTY_BEDBATH)));
            property.put(TABLE_PROPERTY_IMAGE_URL, cursor.getString(cursor.getColumnIndex(TABLE_PROPERTY_IMAGE_URL)));
            property.put(TABLE_PROPERTY_CREATEDBY, cursor.getString(cursor.getColumnIndex(TABLE_PROPERTY_CREATEDBY)));
            cursor.moveToNext();
            propertyList.add(property);
        }
        cursor.close();
        db.close();
        return propertyList;
    }

    /**
     * To check if already favourite
     *
     * @param _id
     * @return
     */
    public boolean isFavourite(String _id) {
        String query = "select * from " + TABLE + " where " + TABLE_PROPERTY_ID + " like '" + _id + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        return !cursor.isAfterLast();
    }
}
