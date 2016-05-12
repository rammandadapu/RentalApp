package edu.sjsu.cmpe277.rentalapp.rentalapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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

import org.apache.commons.codec.binary.StringUtils;
import org.w3c.dom.Text;

import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.dummy.DummyContent;
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


    private SimpleItemRecyclerViewAdapter mSimpleItemRecyclerViewAdapter;
    public PropertyListFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.property_list, container, false);
        setHasOptionsMenu(true);
        mRecycleView = (RecyclerView)view.findViewById(R.id.property_list);
        emptyView = (TextView)view.findViewById(R.id.empty_view);
        if (view.findViewById(R.id.property_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            NavActivity.mTwoPane = true;
        }
        else {
            NavActivity.mTwoPane = false;
        }

       /*if(filtersInitialized == false) {
            setDefaultFilterValues();
       }*/

        return view;
    }



    //Not required - will work even if this code is removed - START
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(isDialogOpen) {
            saveFilterValues();
        }
        outState.putString("keyword",keywordFilter);
        outState.putBoolean("condo", condoFilter);
        outState.putBoolean("house", houseFilter);
        outState.putBoolean("apartment", apartmentFilter);
        outState.putBoolean("townhouse", townhouseFilter);
        outState.putString("pricelow", priceLowFilter);
        outState.putString("pricehigh",priceHighFilter);
        outState.putBoolean("dialog",isDialogOpen);
        outState.putBoolean("filtersinitialized",filtersInitialized);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
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
        inflater.inflate(R.menu.nav, menu);

        /** Get the action view of the menu item whose id is search */
        MenuItem searchViewItem = menu.findItem(R.id.search_view);
        View v = (View) searchViewItem.getActionView();

        /** Get the edit text from the action view */
        //SearchView searchView1 = ( SearchView ) v.findViewById(R.id.search_view);
        if(v instanceof SearchView) {
            location = (SearchView) v;

            location.setIconifiedByDefault(false);
            //searchView.setSubmitButtonEnabled(true);
            location.setQueryHint("Location");
            location.setOnQueryTextListener(this);

            location.setMaxWidth(Integer.MAX_VALUE);
            location.setQuery(locationFilter, true);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        if(newText.equals("")){
            this.onQueryTextSubmit("");
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        //searchTerm = query;
        locationFilter = query;
        new PropertySearchTask(((AppCompatActivity) getActivity()), mRecycleView, emptyView).execute(keywordFilter, locationFilter, priceLowFilter, priceHighFilter, String.valueOf(condoFilter), String.valueOf(apartmentFilter), String.valueOf(houseFilter), String.valueOf(townhouseFilter),"");

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(location.getWindowToken(), 0);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sort) {
            showDialog();
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
                startSearch();
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
        locationFilter = "San Jose";
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
        keyword.setText(keywordFilter);
        condo.setChecked(condoFilter);
        apartment.setChecked(apartmentFilter);
        house.setChecked(houseFilter);
        townhouse.setChecked(townhouseFilter);
        price.setSelectedMinValue(Integer.parseInt(priceLowFilter));
        price.setSelectedMaxValue(Integer.parseInt(priceHighFilter));
    }
}