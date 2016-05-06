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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import edu.sjsu.cmpe277.rentalapp.R;

public class PropertySearchTask extends  AsyncTask<String, String, ArrayList> {
    private Context context;
    RecyclerView recyclerView;
    ArrayList<HashMap<String, String>> oslist;
    ArrayList<String>  busineesIdList;
    ArrayList<String> businessNamesList;

    SimpleItemRecyclerViewAdapter mSimpleItemRecyclerViewAdapter;

    public PropertySearchTask(Context context, RecyclerView recyclerView) {
        this.context = context.getApplicationContext();
        this.recyclerView = recyclerView;
        oslist = new ArrayList<HashMap<String, String>>();
        busineesIdList=new ArrayList<>();
        businessNamesList=new ArrayList<>();
    }

    protected ArrayList doInBackground(String... params) {
        WebService ws = new WebService();
        String properties = ws.searchProperties("","","","","");
        System.out.println("lallalalalaaa "+properties);
        try {
            return processJson(properties);
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList list) {
        mSimpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(context, list);
        recyclerView.setAdapter(mSimpleItemRecyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));

    }

    ArrayList processJson(String jsonStuff) throws JSONException {
        JSONArray result = new JSONArray(jsonStuff);

        try {
                for (int i = 0; i < result.length(); i++) {
                    JSONObject c = result.getJSONObject(i);

                    // Storing  JSON item in a Variable
                    String _id = c.getString("_id");
                    String rent = c.getString("price");
                    String addressLine1 = c.getJSONObject("address").getString("line1");
                    String bath = c.getString("bathNo");
                    //String address = c.getJSONObject(TAG_RES_LOCATION).getJSONArray(TAG_RES_ADDRESS).getString(0);
                    //address += ", "+ c.getJSONObject(TAG_RES_LOCATION).getString(TAG_RES_CITY);

                    // Adding value HashMap key => value

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put("_id", _id);
                    map.put("price", rent);
                    map.put("addressLine1", addressLine1);
                    map.put("bath", bath);

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
