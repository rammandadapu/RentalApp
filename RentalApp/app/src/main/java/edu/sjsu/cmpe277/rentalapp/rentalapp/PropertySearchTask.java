package edu.sjsu.cmpe277.rentalapp.rentalapp;

/**
 * Created by divya.chittimalla on 3/20/16.
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import edu.sjsu.cmpe277.rentalapp.R;

import edu.sjsu.cmpe277.rentalapp.localdbmanager.DBHandler;

public class PropertySearchTask extends  AsyncTask<String, String, ArrayList> {
    private Context context;
    RecyclerView recyclerView;
    TextView emptyView;
    ArrayList<HashMap<String, String>> oslist;
    ArrayList<String>  busineesIdList;
    ArrayList<String> businessNamesList;

    SimpleItemRecyclerViewAdapter mSimpleItemRecyclerViewAdapter;

    public PropertySearchTask(Context context, RecyclerView recyclerView, TextView emptyView) {
        this.context = context.getApplicationContext();
        this.recyclerView = recyclerView;
        this.emptyView = emptyView;
        oslist = new ArrayList<HashMap<String, String>>();
        busineesIdList=new ArrayList<>();
        businessNamesList=new ArrayList<>();
    }

    protected ArrayList doInBackground(String... params) {
        WebService ws = new WebService();
        String properties = ws.searchProperties(params[0],params[1],params[2],params[3],params[4], params[5], params[6], params[7],params[8]);
        System.out.println("lallalalalaaa "+properties);
        try {
            return processJson(properties);
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList list) {
        if(list == null) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            mSimpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(context, list);
            recyclerView.setAdapter(mSimpleItemRecyclerViewAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        }
    }

    ArrayList processJson(String jsonStuff) throws JSONException {
        JSONArray result = new JSONArray(jsonStuff);

        try {
                for (int i = 0; i < result.length(); i++) {
                    JSONObject c = result.getJSONObject(i);

                    // Storing  JSON item in a Variable
                    String _id = c.getString(DBHandler.TABLE_PROPERTY_ID);
                    String rent = c.getString(DBHandler.TABLE_PROPERTY_PRICE);
                    String bath = c.getString(DBHandler.TABLE_PROPERTY_BATH);
                    String bed = c.getString(DBHandler.TABLE_PROPERTY_BED);
                    String address = c.getJSONObject(DBHandler.TABLE_PROPERTY_ADDRESS).getString(DBHandler.TABLE_PROPERTY_ADDRESSLINE1);
                    address += ", "+ c.getJSONObject(DBHandler.TABLE_PROPERTY_ADDRESS).getString(DBHandler.TABLE_PROPERTY_ADDRESSCITY);
                    address += ", "+ c.getJSONObject(DBHandler.TABLE_PROPERTY_ADDRESS).getString(DBHandler.TABLE_PROPERTY_ADDRESSSTATE);
                    address += " "+ c.getJSONObject(DBHandler.TABLE_PROPERTY_ADDRESS).getString(DBHandler.TABLE_PROPERTY_ADDRESSZIP);

                    // Adding value HashMap key => value

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(DBHandler.TABLE_PROPERTY_ID, _id);
                    map.put(DBHandler.TABLE_PROPERTY_PRICE, rent);
                    map.put(DBHandler.TABLE_PROPERTY_BATH, bath);
                    map.put(DBHandler.TABLE_PROPERTY_BED, bed);
                    map.put(DBHandler.TABLE_PROPERTY_ADDRESS, address);

                    System.out.println("MAP: " + map.toString());
                    oslist.add(map);
                }
            return oslist;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
