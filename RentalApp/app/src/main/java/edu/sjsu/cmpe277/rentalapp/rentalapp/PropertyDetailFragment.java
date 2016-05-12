package edu.sjsu.cmpe277.rentalapp.rentalapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;

import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.createpost.CreateNewPropertyActivity;
import edu.sjsu.cmpe277.rentalapp.createpost.CreateNewPropertyFragment;
import edu.sjsu.cmpe277.rentalapp.localdbmanager.DBHandler;
import edu.sjsu.cmpe277.rentalapp.localdbmanager.RentalProperty;
import edu.sjsu.cmpe277.rentalapp.pojo.GlobalPojo;


public class PropertyDetailFragment extends Fragment implements View.OnClickListener {

    public static final String ARG_ITEM_ID = "_id";
    public RentalProperty rentalProperty;

    TextView rentView;
    TextView bedBathView;
    TextView addressView;
    TextView sizeView;
    TextView descView;
    TextView emailView;
    TextView phoneView;
    TextView viewCountView;
    TextView typeView;
    TextView viewCountPrefixView;
    TextView viewCountPostfixView;

    Button editButton;
    Button soldOutButton;
    private GlobalPojo globalPojo;
    public  static final String STATUS_AVAILABLE="Available";
    public  static final String STATUS_SOLD="SOLD";
    private String propertStatus=STATUS_AVAILABLE;
    private String propertyIdentifier;
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
        sizeView = (TextView) rootView.findViewById(R.id.size_detail);
        typeView = (TextView) rootView.findViewById(R.id.type_detail);
        descView = (TextView) rootView.findViewById(R.id.desc_detail);
        emailView = (TextView) rootView.findViewById(R.id.email_detail);
        phoneView = (TextView) rootView.findViewById(R.id.phone_detail);
        viewCountView = (TextView) rootView.findViewById(R.id.view_count_detail);
        viewCountPrefixView=(TextView)rootView.findViewById(R.id.view_count_label1);
        viewCountPostfixView=(TextView)rootView.findViewById(R.id.view_count_label2);

        editButton=(Button)rootView.findViewById(R.id.button_post_edit);
        soldOutButton=(Button)rootView.findViewById(R.id.button_post_cancelled);
        soldOutButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            //fetching code - START
            final String propertyId = getArguments().getString(ARG_ITEM_ID);
            propertyIdentifier=propertyId;
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
                            sizeView.setText(map.get(DBHandler.TABLE_PROPERTY_SIZE).toString());
                            typeView.setText(map.get(DBHandler.TABLE_PROPERTY_TYPE).toString());
                            descView.setText(map.get(DBHandler.TABLE_PROPERTY_DESC).toString());
                            emailView.setText(map.get(DBHandler.TABLE_PROPERTY_EMAIL).toString());
                            phoneView.setText(map.get(DBHandler.TABLE_PROPERTY_PHONE).toString());

                            if(map.get(DBHandler.TABLE_PROPERTY_CREATEDBY).toString().equalsIgnoreCase(globalPojo.getEmail()))
                            {
                                viewCountView.setText(map.get(DBHandler.TABLE_PROPERTY_VIEWCOUNT).toString());
                                viewCountView.setVisibility(View.VISIBLE);
                                viewCountPrefixView.setVisibility(View.VISIBLE);
                                viewCountPostfixView.setVisibility(View.VISIBLE);
                                editButton.setVisibility(View.VISIBLE);
                                soldOutButton.setVisibility(View.VISIBLE);
                                //Always next two line one after another
                                propertStatus=map.get(DBHandler.TABLE_PROPERTY_STATUS).toString();
                                handleSoldAvailableButton();
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
            String size = json.getString(DBHandler.TABLE_PROPERTY_SIZE);
            String type = json.getString(DBHandler.TABLE_PROPERTY_TYPE);
            String email = json.getString(DBHandler.TABLE_PROPERTY_EMAIL);
            String phone = json.getString(DBHandler.TABLE_PROPERTY_PHONE);
            String desc = json.getString(DBHandler.TABLE_PROPERTY_DESC);
            String viewCount = json.getString(DBHandler.TABLE_PROPERTY_VIEWCOUNT);
            String status=json.getString(DBHandler.TABLE_PROPERTY_STATUS);
            //String image_url = c.getString("image_url");

            // Adding value HashMap key => value

            HashMap<String, String> map = new HashMap<String, String>();

            map.put(DBHandler.TABLE_PROPERTY_PRICE, rent);
            map.put(DBHandler.TABLE_PROPERTY_BED, bed);
            map.put(DBHandler.TABLE_PROPERTY_BATH, bath);
            map.put(DBHandler.TABLE_PROPERTY_ADDRESS, address);
            map.put(DBHandler.TABLE_PROPERTY_CREATEDBY, createdBy);
            map.put(DBHandler.TABLE_PROPERTY_SIZE, size);
            map.put(DBHandler.TABLE_PROPERTY_TYPE, type);
            map.put(DBHandler.TABLE_PROPERTY_EMAIL, email);
            map.put(DBHandler.TABLE_PROPERTY_PHONE, phone);
            map.put(DBHandler.TABLE_PROPERTY_DESC, desc);
            map.put(DBHandler.TABLE_PROPERTY_VIEWCOUNT, viewCount);

            map.put(DBHandler.TABLE_PROPERTY_STATUS, status);
            //map.put("image_url", image_url);

            System.out.println("MAP: " + map.toString());
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



    private void postChangeToDB(String propertStatus){
        new AsyncTask<String,String,String>(){
            @Override
            protected String doInBackground(String... params) {
                return new WebService().changePropetyStatus(params[0],propertyIdentifier);

            }
        }.execute(propertStatus);
    }

    private void setIcon(int icon){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            soldOutButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, icon, 0);
        }
        else{
            soldOutButton.setCompoundDrawables(null,null,null,null);

        }
    }
    private void handleSoldAvailableButton(){
        if(STATUS_AVAILABLE.equalsIgnoreCase(propertStatus)){
            soldOutButton.setText(R.string.post_cancelled);
            setIcon(R.drawable.ic_lock_white_24dp);
        }
        else {
            soldOutButton.setText(R.string.post_available);
            setIcon(R.drawable.ic_lock_open_white_24dp);
        }

    }
    private void togglePropertyStatus(){
        if(STATUS_AVAILABLE.equalsIgnoreCase(propertStatus)){
            propertStatus=STATUS_SOLD;
        }
        else{
            propertStatus=STATUS_AVAILABLE;
        }
        handleSoldAvailableButton();
        postChangeToDB(propertStatus);
    }

    private void showEditScreen(){
        Intent intent=new Intent(getContext(),CreateNewPropertyActivity.class);
        intent.putExtra("_id",propertyIdentifier);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_post_cancelled:
                togglePropertyStatus();
                Toast.makeText(getContext(),"Request Submitted",Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_post_edit:
                showEditScreen();
                break;

        }
    }
}
