package edu.sjsu.cmpe277.rentalapp.createpost;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sjsu.cmpe277.rentalapp.localdbmanager.DBHandler;
import edu.sjsu.cmpe277.rentalapp.rentalapp.DividerItemDecoration;
import edu.sjsu.cmpe277.rentalapp.rentalapp.SimpleItemRecyclerViewAdapter;
import edu.sjsu.cmpe277.rentalapp.rentalapp.WebService;

/**
 * Created by ram.mandadapu on 5/8/16.
 */
public class SearchUserPostsTask extends AsyncTask<String, String, List> {
        private Context context;
        RecyclerView recyclerView;
        List<Map<String, String>> oslist;
        List<String> busineesIdList;
        List<String> businessNamesList;

        SimpleItemRecyclerViewAdapter mSimpleItemRecyclerViewAdapter;

        public SearchUserPostsTask(Context context, RecyclerView recyclerView) {
            this.context = context.getApplicationContext();
            this.recyclerView = recyclerView;
            oslist = new ArrayList<Map<String, String>>();
            busineesIdList=new ArrayList<>();
            businessNamesList=new ArrayList<>();
        }

    protected List doInBackground(String... params) {
        WebService ws = new WebService();
        String properties = ws.searchProperties(params[0],params[1],params[2],params[3],params[4], params[5], params[6], params[7]);
        try {
            return processJson(properties);
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(List list) {
        mSimpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(context, (ArrayList)list);
        recyclerView.setAdapter(mSimpleItemRecyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));

    }

    List processJson(String jsonStuff) throws JSONException {
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
