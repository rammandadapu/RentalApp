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

    private static final int DB_VERSION = 1;
    private static final String DB = "rentalapp.db";
    private static final String TABLE = "favorites";
    private static final String TABLE_PROPERTY_NAME = "name";
    private static final String TABLE_PROPERTY_ID = "_id";

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
                + TABLE_PROPERTY_NAME + " TEXT"
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
        values.put(TABLE_PROPERTY_ID, property.getPropertyId());
        values.put(TABLE_PROPERTY_NAME, property.getName());
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
            property.put("propertyId", cursor.getString(cursor.getColumnIndex(TABLE_PROPERTY_ID)));
            property.put("propertyName", cursor.getString(cursor.getColumnIndex(TABLE_PROPERTY_NAME)));

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

    public static void main(String args[]) {
        DBHandler dbHandler = new DBHandler(null,null,null,0);
        RentalProperty property = new RentalProperty();
        /*property.setPropertyId("1");
        property.setName("house 1");
        dbHandler.addProperty(property);
        property.setPropertyId("2");
        property.setName("house 2");
        dbHandler.addProperty(property);
        property.setPropertyId("3");
        property.setName("house 3");
        dbHandler.addProperty(property);
        property.setPropertyId("4");
        property.setName("house 4");
        dbHandler.addProperty(property);
        property.setPropertyId("5");
        property.setName("house 5");
        dbHandler.addProperty(property);
        property.setPropertyId("6");
        property.setName("house 6");
        dbHandler.addProperty(property);
        property.setPropertyId("7");
        property.setName("house 7");
        dbHandler.addProperty(property);
        property.setPropertyId("8");
        property.setName("house 8");
        dbHandler.addProperty(property);
        property.setPropertyId("9");
        property.setName("house 9");
        dbHandler.addProperty(property);
        property.setPropertyId("10");
        property.setName("house 10");
        dbHandler.addProperty(property);
        property.setPropertyId("11");
        property.setName("house 11");
        dbHandler.addProperty(property);
        property.setPropertyId("12");
        property.setName("house 12");
        dbHandler.addProperty(property);*/

        System.out.println(dbHandler.getAllProperties());
    }
}
