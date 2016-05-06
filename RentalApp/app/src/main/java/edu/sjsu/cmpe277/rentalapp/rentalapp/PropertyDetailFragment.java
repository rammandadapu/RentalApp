package edu.sjsu.cmpe277.rentalapp.rentalapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.localdbmanager.DBHandler;
import edu.sjsu.cmpe277.rentalapp.localdbmanager.RentalProperty;


public class PropertyDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "_id";
    public RentalProperty rentalProperty;

    TextView rentView;
    TextView bedBathView;

    public PropertyDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            //appBarLayout.setTitle(mItem.content);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.property_detail, container, false);

        rentView = (TextView) rootView.findViewById(R.id.rent_detail);
        bedBathView = (TextView) rootView.findViewById(R.id.bed_bath_detail);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            //fetching code - START
            final String propertyId = getArguments().getString(ARG_ITEM_ID);
            //Toast.makeText(getContext(),"id:"+businessId,Toast.LENGTH_LONG).show();
            if (propertyId != null) {
                new AsyncTask<String, String, HashMap>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        //progressDialog = new ProgressDialog(getActivity());
                        //progressDialog.setMessage("Loading...");
                        //progressDialog.show();
                    }

                    @Override
                    protected HashMap doInBackground(String... params) {
                        WebService ws = new WebService();
                        String propertyDetails = ws.getPropertyDetails(propertyId);
                        System.out.println(propertyDetails);
                        try {
                            return processJson(propertyDetails);
                        } catch (JSONException e) {
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(HashMap map) {
                        super.onPostExecute(map);
                        //progressDialog.dismiss();
                        try {
                            System.out.println("Hiiiiiiiiii");
                            rentView.setText(map.get("rent").toString());
                            bedBathView.setText(map.get("bed").toString());
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }.execute();
            }
        }
        return rootView;
    }

    public HashMap processJson(String jsonStuff) throws JSONException {
        JSONObject json = new JSONObject(jsonStuff);

        try {
            // Storing  JSON item in a Variable
            String rent = json.getString("price");
            //String address = json.getJSONObject(TAG_RES_LOCATION).getJSONArray(TAG_RES_ADDRESS).getString(0);
            //address += ", "+ json.getJSONObject(TAG_RES_LOCATION).getString(TAG_RES_CITY);
            String bed = json.getString("bathNo");

            //String image_url = c.getString("image_url");

            // Adding value HashMap key => value

            HashMap<String, String> map = new HashMap<String, String>();

            map.put("rent", rent);
            map.put("bed", bed);
            //map.put("image_url", image_url);

            System.out.println("MAP: " + map.toString());
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
