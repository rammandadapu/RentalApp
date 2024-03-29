package edu.sjsu.cmpe277.rentalapp.rentalapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SearchView;
import android.view.MenuInflater;
import android.widget.TextView;

import android.location.LocationManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.Geocoder;

import java.util.HashMap;
import java.util.Locale;

import android.location.Address;

import java.util.List;
import java.io.IOException;

import android.widget.Toast;

import org.apache.commons.codec.binary.StringUtils;
import org.json.JSONException;
import org.w3c.dom.Text;

import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.dummy.DummyContent;
import edu.sjsu.cmpe277.rentalapp.pojo.GlobalPojo;

/**
 * A simple {@link Fragment} subclass.
 */
public class PropertyListFragment extends Fragment
        implements SearchView.OnQueryTextListener {
    private RecyclerView mRecycleView;
    private TextView emptyView;

    SearchView location;
    EditText keyword;
    CheckBox apartment;
    CheckBox house;
    CheckBox condo;
    CheckBox townhouse;
    RangeSeekBar price;

    String keywordFilter;
    String locationFilter;
    String priceLowFilter;
    String priceHighFilter;
    boolean condoFilter;
    boolean houseFilter;
    boolean apartmentFilter;
    boolean townhouseFilter;

    boolean filtersInitialized;
    boolean isDialogOpen;

    LocationManager locationManager;

    Button filterButton;
    Button saveSearchButton;
    Button dummy;

    TextView savedSearchName;
    TextView savedSearchFilter;
    CheckBox notificationsCheckbox;

    GlobalPojo globalPojo;


    private SimpleItemRecyclerViewAdapter mSimpleItemRecyclerViewAdapter;

    public PropertyListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.property_list, container, false);
        setHasOptionsMenu(true);
        mRecycleView = (RecyclerView) view.findViewById(R.id.property_list);
        emptyView = (TextView) view.findViewById(R.id.empty_view);
        if (view.findViewById(R.id.property_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            NavActivity.mTwoPane = true;
        } else {
            NavActivity.mTwoPane = false;
        }

       /*if(filtersInitialized == false) {
            setDefaultFilterValues();
       }*/

        filterButton = (Button) view.findViewById(R.id.filter_button);
        filterButton.setBackgroundResource(R.drawable.white_image);
        filterButton.setVisibility(View.VISIBLE);

        filterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog();
            }
        });

        saveSearchButton = (Button) view.findViewById(R.id.save_search_button);
        saveSearchButton.setBackgroundResource(R.drawable.white_image);
        saveSearchButton.setVisibility(View.VISIBLE);

        dummy = (Button) view.findViewById(R.id.dummy);
        if(dummy != null) {
            dummy.setBackgroundResource(R.drawable.white_image);
            dummy.setVisibility(View.VISIBLE);
        }

        saveSearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showSaveSearchDialog();
            }
        });

        globalPojo=(GlobalPojo)getActivity().getApplicationContext();

        return view;
    }

    //Not required - will work even if this code is removed - START
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (isDialogOpen) {
            saveFilterValues();
        }
        outState.putString("location", locationFilter);
        outState.putString("keyword", keywordFilter);
        outState.putBoolean("condo", condoFilter);
        outState.putBoolean("house", houseFilter);
        outState.putBoolean("apartment", apartmentFilter);
        outState.putBoolean("townhouse", townhouseFilter);
        outState.putString("pricelow", priceLowFilter);
        outState.putString("pricehigh", priceHighFilter);
        outState.putBoolean("dialog", isDialogOpen);
        outState.putBoolean("filtersinitialized", filtersInitialized);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null) {
            locationFilter = savedInstanceState.getString("location");
            keywordFilter = savedInstanceState.getString("keyword");
            condoFilter = savedInstanceState.getBoolean("condo");
            houseFilter = savedInstanceState.getBoolean("house");
            apartmentFilter = savedInstanceState.getBoolean("apartment");
            townhouseFilter = savedInstanceState.getBoolean("townhouse");
            priceLowFilter = savedInstanceState.getString("pricelow");
            priceHighFilter = savedInstanceState.getString("pricehigh");
            filtersInitialized = savedInstanceState.getBoolean("filtersinitialized");
            if(savedInstanceState.getBoolean("dialog")) {
                showDialog();
            }
        }
        else {
            if(!filtersInitialized)
                setDefaultFilterValues();
        }
    }
    //Not required - will work even if this code is removed - END

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.property_list_nav, menu);

        /** Get the action view of the menu item whose id is search */
        MenuItem searchViewItem = menu.findItem(R.id.search_view);
        View v = (View) searchViewItem.getActionView();

        /** Get the edit text from the action view */
        //SearchView searchView1 = ( SearchView ) v.findViewById(R.id.search_view);
        if(v instanceof SearchView) {
            location = (SearchView) v;

            location.setIconifiedByDefault(false);
            //searchView.setSubmitButtonEnabled(true);
            location.setQueryHint("City/Zip");
            location.setOnQueryTextListener(this);

            location.setMaxWidth(Integer.MAX_VALUE);
            location.setQuery(locationFilter, true);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        locationFilter = newText;
        if(newText.equals("")){
            //this.onQueryTextSubmit("");
            //setLocationFilterToCurrentCity();
            //location.setQuery(locationFilter,false);
        }
        return true;
    }



    @Override
    public boolean onQueryTextSubmit(String query)
    {
        //searchTerm = query;
        //locationFilter = query;
        new PropertySearchTask(((AppCompatActivity) getActivity()), mRecycleView, emptyView).execute(keywordFilter, locationFilter, priceLowFilter, priceHighFilter, String.valueOf(condoFilter), String.valueOf(apartmentFilter), String.valueOf(houseFilter), String.valueOf(townhouseFilter), "");

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(location.getWindowToken(), 0);

        location.clearFocus();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.curr_location) {
            setLocationFilterToCurrentCity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View searchFilterDialog = inflater.inflate(R.layout.search_filter_dialog, null, false);

        keyword = (EditText) searchFilterDialog.findViewById(R.id.keyword_filter);
        house = (CheckBox) searchFilterDialog.findViewById(R.id.house_checkbox);
        townhouse = (CheckBox) searchFilterDialog.findViewById(R.id.townhouse_checkbox);
        apartment = (CheckBox) searchFilterDialog.findViewById(R.id.apartment_checkbox);
        condo = (CheckBox) searchFilterDialog.findViewById(R.id.condo_checkbox);
        price = (RangeSeekBar) searchFilterDialog.findViewById(R.id.price_range_filter);

        setViewFromFilterValues();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setView(searchFilterDialog);

        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton("View results", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                isDialogOpen = false;
                saveFilterValues();
                locationFilter = location.getQuery().toString();
                if(TextUtils.isEmpty(locationFilter)) {
                    Toast.makeText(getContext(), "Please enter a location", Toast.LENGTH_LONG).show();
                }
                else {
                    startSearch();
                }
            }
        });

        alertDialogBuilder.setNeutralButton("Reset filters", null);

        alertDialogBuilder.setCancelable(false);

        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        setDefaultFilterValues();
                        setViewFromFilterValues();
                    }
                });
            }
        });

        alertDialog.show();
        isDialogOpen = true;
    }

    private void setDefaultFilterValues() {
        setLocationFilterToCurrentCity();
        keywordFilter = "";
        priceLowFilter = "20";
        priceHighFilter = "15000";
        condoFilter = true;
        houseFilter = true;
        apartmentFilter = true;
        townhouseFilter = true;
        filtersInitialized = true;
        //setFilterValues();
    }

    private void saveFilterValues() {
        locationFilter = location.getQuery().toString();
        keywordFilter = keyword.getText().toString();
        condoFilter = condo.isChecked();
        apartmentFilter = apartment.isChecked();
        townhouseFilter = townhouse.isChecked();
        houseFilter = house.isChecked();
        priceLowFilter = price.getSelectedMinValue().toString();
        priceHighFilter = price.getSelectedMaxValue().toString();
    }

    private void startSearch() {
        new PropertySearchTask(((AppCompatActivity) getActivity()), mRecycleView, emptyView).execute(keywordFilter, locationFilter, priceLowFilter, priceHighFilter,
                String.valueOf(condoFilter), String.valueOf(apartmentFilter), String.valueOf(houseFilter), String.valueOf(townhouseFilter), "");
        System.out.println("keyword filter" + keywordFilter);
    }

    private void setViewFromFilterValues() {
        if(location != null) {
            location.setQuery(locationFilter, false);
        }
        keyword.setText(keywordFilter);
        condo.setChecked(condoFilter);
        apartment.setChecked(apartmentFilter);
        house.setChecked(houseFilter);
        townhouse.setChecked(townhouseFilter);
        price.setSelectedMinValue(Integer.parseInt(priceLowFilter));
        price.setSelectedMaxValue(Integer.parseInt(priceHighFilter));
    }

    private String getCityNameFromLocation(Location loc) {
        String cityName = null;
        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }

    private void setLocationFilterToCurrentCity() {
        locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        else {
            if(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
                locationFilter = getCityNameFromLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                if(location != null) {
                    location.setQuery(locationFilter,false);
                }
                System.out.println("LOCATIONNNNNNNN:" + locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).toString());
                //Toast.makeText(getContext(),locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).toString(),Toast.LENGTH_LONG).show();
            }
            else {
                locationFilter = "San Jose";
                if(location != null) {
                    location.setQuery(locationFilter,false);
                }
                /*locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 10, 2, locationListener);*/
                //Toast.makeText(getContext(),"location not available, waiting for location manager",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showSaveSearchDialog() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View saveSearchDialog = inflater.inflate(R.layout.save_search_dialog, null, false);

        savedSearchName = (TextView) saveSearchDialog.findViewById(R.id.search_name_dialog);
        savedSearchFilter = (TextView) saveSearchDialog.findViewById(R.id.filter_dialog);
        notificationsCheckbox = (CheckBox) saveSearchDialog.findViewById(R.id.notification_checkbox);

        savedSearchName.setText(locationFilter);
        String keywordText = (TextUtils.isEmpty(keywordFilter)?"":"Keyword: "+keywordFilter);
        String priceRangeText = "Price range: $"+priceLowFilter+"-"+priceHighFilter;
        String houseText = (houseFilter?"House":"");
        String apartmentText = (apartmentFilter?"Apartment":"");
        String condoText = (condoFilter?"Condo":"");
        String townhouseText = (townhouseFilter?"Townhouse":"");

        if(!TextUtils.isEmpty(keywordText)) {
            keywordText += ", ";
        }

        if(!TextUtils.isEmpty(houseText)) {
            priceRangeText += ", ";
        }

        if(!TextUtils.isEmpty(apartmentText)) {
            houseText += ", ";
        }

        if(!TextUtils.isEmpty(condoText)) {
            apartmentText += ", ";
        }

        if(!TextUtils.isEmpty(townhouseText)) {
            condoText += ", ";
        }

        String filterText = keywordText + priceRangeText + houseText + apartmentText + condoText + townhouseText;

        savedSearchFilter.setText(filterText);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setView(saveSearchDialog);

        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                saveSearch(notificationsCheckbox.isChecked());
            }
        });

        alertDialogBuilder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        alertDialogBuilder.setCancelable(false);

        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    public void saveSearch(boolean notify) {
        new AsyncTask<String, String, String>() {

            @Override
            protected String doInBackground(String... params) {
                WebService ws = new WebService();
                String result = ws.addSavedSearch(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8],params[9],params[10]);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if(TextUtils.equals(result,"successful")) {
                    Toast.makeText(getContext(), "Search saved", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getContext(), "Error saving search", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(globalPojo.getEmail(),locationFilter,keywordFilter,locationFilter,priceLowFilter,priceHighFilter,String.valueOf(condoFilter),String.valueOf(apartmentFilter),String.valueOf(houseFilter),String.valueOf(townhouseFilter),String.valueOf(notify));
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            System.out.println("Location changed: Lat: " + loc.getLatitude() + " Lng: " + loc.getLongitude());
            String longitude = "Longitude: " + loc.getLongitude();
            String latitude = "Latitude: " + loc.getLatitude();

            System.out.println("LOCATIONNNNNNNN:" + loc.toString());
            //Toast.makeText(getContext(),loc.toString(),Toast.LENGTH_LONG).show();
            locationFilter = getCityNameFromLocation(loc);
            if(location != null)
                location.setQuery(locationFilter,false);

        /*------- To get city name from coordinates -------- */
            /*String cityName = null;
            Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    cityName = addresses.get(0).getLocality();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                    + cityName;*/
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }
}

