package edu.sjsu.cmpe277.rentalapp.savedsearch;

/**
 * Created by divya.chittimalla on 3/20/16.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.localdbmanager.DBHandler;
import edu.sjsu.cmpe277.rentalapp.rentalapp.DividerItemDecoration;
import edu.sjsu.cmpe277.rentalapp.rentalapp.SimpleItemRecyclerViewAdapter;
import edu.sjsu.cmpe277.rentalapp.rentalapp.WebService;

public class SavedSearchListTask extends  AsyncTask<String, String, ArrayList> {
    private AppCompatActivity activity;
    RecyclerView recyclerView;
    TextView emptyView;
    ArrayList<HashMap<String, String>> oslist;
    ArrayList<String>  busineesIdList;
    ArrayList<String> businessNamesList;

    boolean connectionFailed;
    private ProgressDialog mProgressDialog;

    MySavedSearchRecyclerViewAdapter mySavedSearchRecyclerViewAdapter;

    public SavedSearchListTask(AppCompatActivity activity, RecyclerView recyclerView, TextView emptyView) {
        this.activity = activity;
        this.recyclerView = recyclerView;
        this.emptyView = emptyView;
        oslist = new ArrayList<HashMap<String, String>>();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                showProgressDialog();

            }
        });
    }

    protected ArrayList doInBackground(String... params) {


        WebService ws = new WebService();
        String properties = ws.getSavedSearches(params[0]);
        System.out.println("lallalalalaaa "+properties);
        if(properties != "connection failed") {
            try {
                return processJson(properties);
            } catch (JSONException e) {
                return null;
            }
        }
        else
            connectionFailed = true;
            return null;
    }

    @Override
    protected void onPostExecute(ArrayList list) {
        if(list == null) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            if(connectionFailed) {
                emptyView.setText("Connection to server failed. Please check your internet connection and try again!");
            }
            else {
                emptyView.setText("No Matching Listings");
            }
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            mySavedSearchRecyclerViewAdapter = new MySavedSearchRecyclerViewAdapter(activity, list);
            recyclerView.setAdapter(mySavedSearchRecyclerViewAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST));
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                hideProgressDialog();

            }
        });

    }

    ArrayList processJson(String jsonStuff) throws JSONException {
        if("no results".equalsIgnoreCase(jsonStuff))
            return null;
        try {
                JSONArray result = new JSONArray(jsonStuff);
                for (int i = 0; i < result.length(); i++) {
                    JSONObject c = result.getJSONObject(i);

                    // Storing  JSON item in a Variable
                    String _id = c.getString(DBHandler.TABLE_PROPERTY_ID);
                    String name = c.getString("name");
                    String keyword;
                    if(c.has("keyword")) {
                        keyword = c.getString("keyword");
                    }
                    else {
                        keyword = "";
                    }
                    String location = c.getString("location");
                    String pricelow = c.getString("pricelow");
                    String pricehigh = c.getString("pricehigh");
                    String condo = c.getString("condo");
                    String apartment = c.getString("apartment");
                    String house = c.getString("house");
                    String townhouse = c.getString("townhouse");

                    // Adding value HashMap key => value

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(DBHandler.TABLE_PROPERTY_ID, _id);
                    map.put("name",name);
                    map.put("location",location);
                    map.put("keyword",keyword);
                    map.put("pricelow",pricelow);
                    map.put("pricehigh",pricehigh);
                    map.put("condo",condo);
                    map.put("apartment",apartment);
                    map.put("house",house);
                    map.put("townhouse",townhouse);

                    System.out.println("MAP: " + map.toString());
                    oslist.add(map);
                }
            return oslist;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void showProgressDialog() {
        //Looper.prepare();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setMessage(activity.getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
}
