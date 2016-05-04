package edu.sjsu.cmpe277.rentalapp.localdbmanager;

/**
 * Created by divya.chittimalla on 4/26/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sjsu.cmpe277.rentalapp.rentalapp.NavActivity;


public class DBHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 2;
    private static final String DB = "rentalapp.db";
    private static final String TABLE = "favorites";
    private static final String TABLE_PROPERTY_ID = "_id";
    private static final String TABLE_PROPERTY_ADDRESSLINE1 = "addressLine1";
    private static final String TABLE_PROPERTY_ADDRESSCITY = "addressCity";
    private static final String TABLE_PROPERTY_ADDRESSSTATE = "addressState";
    private static final String TABLE_PROPERTY_ADDRESSZIP = "addressZip";
    private static final String TABLE_PROPERTY_PRICE = "price";
    private static final String TABLE_PROPERTY_BED = "bed";
    private static final String TABLE_PROPERTY_BATH = "bath";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB, factory, DB_VERSION);
    }

    /**
     * Creates the DB
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + TABLE + " ( "
                + TABLE_PROPERTY_ID + " TEXT PRIMARY KEY,"
                + TABLE_PROPERTY_ADDRESSLINE1 + " TEXT,"
                + TABLE_PROPERTY_ADDRESSCITY + " TEXT,"
                + TABLE_PROPERTY_ADDRESSSTATE + " TEXT,"
                + TABLE_PROPERTY_ADDRESSZIP + " TEXT,"
                + TABLE_PROPERTY_PRICE + " TEXT,"
                + TABLE_PROPERTY_BED + " TEXT,"
                + TABLE_PROPERTY_BATH + " TEXT"
                + ")";
        db.execSQL(query);
    }

    /**
     * Creates new table if version number changes
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
     * Add the restaurant
     * @param property
     */
    public void addProperty(RentalProperty property) {

        ContentValues values = new ContentValues();
        values.put(TABLE_PROPERTY_ID, property.get_id());
        values.put(TABLE_PROPERTY_ADDRESSLINE1, property.getAddressLine1());
        values.put(TABLE_PROPERTY_ADDRESSCITY, property.getAddressCity());
        values.put(TABLE_PROPERTY_ADDRESSSTATE, property.getAddressState());
        values.put(TABLE_PROPERTY_ADDRESSZIP, property.getAddressZip());
        values.put(TABLE_PROPERTY_PRICE, property.getPrice());
        values.put(TABLE_PROPERTY_BATH, property.getBath());
        values.put(TABLE_PROPERTY_BED, property.getBed());
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
     * Return list of all favourite restaurants. If nothing exist returns empty list.
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
            property.put("_id", cursor.getString(cursor.getColumnIndex(TABLE_PROPERTY_ID)));
            property.put("addressLine1", cursor.getString(cursor.getColumnIndex(TABLE_PROPERTY_ADDRESSLINE1)));
            property.put("addressCity", cursor.getString(cursor.getColumnIndex(TABLE_PROPERTY_ADDRESSCITY)));
            property.put("addressState", cursor.getString(cursor.getColumnIndex(TABLE_PROPERTY_ADDRESSSTATE)));
            property.put("addressZip", cursor.getString(cursor.getColumnIndex(TABLE_PROPERTY_ADDRESSZIP)));
            property.put("price", cursor.getString(cursor.getColumnIndex(TABLE_PROPERTY_PRICE)));
            property.put("bed", cursor.getString(cursor.getColumnIndex(TABLE_PROPERTY_BED)));
            property.put("bath", cursor.getString(cursor.getColumnIndex(TABLE_PROPERTY_BATH)));

            cursor.moveToNext();
            propertyList.add(property);
        }
        cursor.close();
        db.close();
        return propertyList;
    }

    public ArrayList<String> getAllPropertyIds() {

        String query = "select * from " + TABLE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        ArrayList<String> restaurantIdsList = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            restaurantIdsList.add(cursor.getString(cursor.getColumnIndex(TABLE_PROPERTY_ID)));
            cursor.moveToNext();
            /*restaurant.setPhone(cursor.getString(cursor.getColumnIndex(TABLE_RESTAURANT_PHONE)));
            restaurant.setUrl(cursor.getString(cursor.getColumnIndex(TABLE_RESTAURANT_URL)));
            restaurant.setLocation(cursor.getString(cursor.getColumnIndex(TABLE_RESTAURANT_LOCATION)));*/

        }
        cursor.close();
        db.close();
        return restaurantIdsList;
    }

    /**
     * To check if already favourite
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
