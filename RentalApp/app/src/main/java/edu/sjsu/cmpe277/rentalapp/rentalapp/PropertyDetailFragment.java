package edu.sjsu.cmpe277.rentalapp.rentalapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;

import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.localdbmanager.DBHandler;
import edu.sjsu.cmpe277.rentalapp.localdbmanager.RentalProperty;
import edu.sjsu.cmpe277.rentalapp.pojo.GlobalPojo;


public class PropertyDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "_id";
    public RentalProperty rentalProperty;

    TextView rentView;
    TextView bedBathView;
    TextView addressView;

    Button editButton;
    Button soldOutButton;
    private GlobalPojo globalPojo;
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
        globalPojo=(GlobalPojo)getActivity().getApplicationContext();
        System.out.print("*****"+globalPojo.getEmail());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.property_detail, container, false);

        rentView = (TextView) rootView.findViewById(R.id.rent_detail);
        bedBathView = (TextView) rootView.findViewById(R.id.bed_bath_detail);
        addressView = (TextView) rootView.findViewById(R.id.address_detail);
        editButton=(Button)rootView.findViewById(R.id.button_post_edit);
        soldOutButton=(Button)rootView.findViewById(R.id.button_post_cancelled);

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
                            NumberFormat format = NumberFormat.getCurrencyInstance();
                            rentView.setText(format.format(Integer.parseInt(map.get(DBHandler.TABLE_PROPERTY_PRICE).toString())));
                            bedBathView.setText(map.get(DBHandler.TABLE_PROPERTY_BED).toString() + "bd   " + map.get(DBHandler.TABLE_PROPERTY_BATH).toString() + "ba");
                            addressView.setText(map.get(DBHandler.TABLE_PROPERTY_ADDRESS).toString());
                            if(map.get(DBHandler.TABLE_PROPERTY_CREATEDBY).toString().equalsIgnoreCase(globalPojo.getEmail()))
                            {
                                editButton.setVisibility(View.VISIBLE);
                                soldOutButton.setVisibility(View.VISIBLE);
                            }
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
        JSONArray jsonArray = new JSONArray(jsonStuff);
        JSONObject json = jsonArray.getJSONObject(0);

        try {
            // Storing  JSON item in a Variable
            String rent = json.getString(DBHandler.TABLE_PROPERTY_PRICE);
            //String address = json.getJSONObject(TAG_RES_LOCATION).getJSONArray(TAG_RES_ADDRESS).getString(0);
            //address += ", "+ json.getJSONObject(TAG_RES_LOCATION).getString(TAG_RES_CITY);
            String bed = json.getString(DBHandler.TABLE_PROPERTY_BED);
            String bath = json.getString(DBHandler.TABLE_PROPERTY_BATH);
            String address = json.getJSONObject(DBHandler.TABLE_PROPERTY_ADDRESS).getString(DBHandler.TABLE_PROPERTY_ADDRESSLINE1);
            address += ", "+ json.getJSONObject(DBHandler.TABLE_PROPERTY_ADDRESS).getString(DBHandler.TABLE_PROPERTY_ADDRESSCITY);
            address += ", "+ json.getJSONObject(DBHandler.TABLE_PROPERTY_ADDRESS).getString(DBHandler.TABLE_PROPERTY_ADDRESSSTATE);
            address += " "+ json.getJSONObject(DBHandler.TABLE_PROPERTY_ADDRESS).getString(DBHandler.TABLE_PROPERTY_ADDRESSZIP);
            String createdBy=json.getString(DBHandler.TABLE_PROPERTY_CREATEDBY);
            //String image_url = c.getString("image_url");

            // Adding value HashMap key => value

            HashMap<String, String> map = new HashMap<String, String>();

            map.put(DBHandler.TABLE_PROPERTY_PRICE, rent);
            map.put(DBHandler.TABLE_PROPERTY_BED, bed);
            map.put(DBHandler.TABLE_PROPERTY_BATH, bath);
            map.put(DBHandler.TABLE_PROPERTY_ADDRESS, address);
            map.put(DBHandler.TABLE_PROPERTY_CREATEDBY, createdBy);
            //map.put("image_url", image_url);

            System.out.println("MAP: " + map.toString());
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
