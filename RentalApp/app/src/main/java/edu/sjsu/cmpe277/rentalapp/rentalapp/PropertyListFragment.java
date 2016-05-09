package edu.sjsu.cmpe277.rentalapp.rentalapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

        if(savedInstanceState == null) {
            //TODO: change this to default the current location
            locationFilter = "San Jose";
            keywordFilter = "";
            priceLowFilter = "0";
            priceHighFilter = "15000";
            condoFilter = true;
            houseFilter = true;
            apartmentFilter = true;
            townhouseFilter = true;
        }

        return view;
    }

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
            location.setQuery(locationFilter,true);
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
        new PropertySearchTask(getActivity(), mRecycleView, emptyView).execute(keywordFilter, locationFilter, priceLowFilter, priceHighFilter, String.valueOf(condoFilter), String.valueOf(apartmentFilter), String.valueOf(houseFilter), String.valueOf(townhouseFilter),"");

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

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View searchFilterDialog = inflater.inflate(R.layout.search_filter_dialog, null, false);

            keyword = (EditText) searchFilterDialog.findViewById(R.id.keyword_filter);
            house = (CheckBox) searchFilterDialog.findViewById(R.id.house_checkbox);
            townhouse = (CheckBox) searchFilterDialog.findViewById(R.id.townhouse_checkbox);
            apartment = (CheckBox) searchFilterDialog.findViewById(R.id.apartment_checkbox);
            condo = (CheckBox) searchFilterDialog.findViewById(R.id.condo_checkbox);
            price = (RangeSeekBar) searchFilterDialog.findViewById(R.id.price_range_filter);

            setFilterValues();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

            alertDialogBuilder.setView(searchFilterDialog);

            alertDialogBuilder.setCancelable(true);

            alertDialogBuilder.setPositiveButton("View results", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    getFilterValues();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getFilterValues() {
        keywordFilter = keyword.getText().toString();
        condoFilter = condo.isChecked();
        apartmentFilter = apartment.isChecked();
        townhouseFilter = townhouse.isChecked();
        houseFilter = house.isChecked();
        priceLowFilter = price.getSelectedMinValue().toString();
        priceHighFilter = price.getSelectedMaxValue().toString();

        new PropertySearchTask(getActivity(), mRecycleView, emptyView).execute(keywordFilter, locationFilter, priceLowFilter, priceHighFilter,
                String.valueOf(condoFilter), String.valueOf(apartmentFilter), String.valueOf(houseFilter), String.valueOf(townhouseFilter), "");
        System.out.println("keyword filter" + keywordFilter);
    }

    private void setFilterValues() {
        keyword.setText(keywordFilter);
        condo.setChecked(condoFilter);
        apartment.setChecked(apartmentFilter);
        house.setChecked(houseFilter);
        townhouse.setChecked(townhouseFilter);
        price.setSelectedMinValue(Integer.parseInt(priceLowFilter));
        price.setSelectedMaxValue(Integer.parseInt(priceHighFilter));
    }
}