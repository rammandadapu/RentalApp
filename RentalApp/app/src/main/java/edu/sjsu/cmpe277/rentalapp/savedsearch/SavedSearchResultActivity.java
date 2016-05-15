package edu.sjsu.cmpe277.rentalapp.savedsearch;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.pojo.GlobalPojo;
import edu.sjsu.cmpe277.rentalapp.rentalapp.NavActivity;
import edu.sjsu.cmpe277.rentalapp.rentalapp.PropertySearchTask;
import edu.sjsu.cmpe277.rentalapp.rentalapp.RangeSeekBar;
import edu.sjsu.cmpe277.rentalapp.rentalapp.SimpleItemRecyclerViewAdapter;
import edu.sjsu.cmpe277.rentalapp.rentalapp.WebService;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedSearchResultActivity extends AppCompatActivity {
    private RecyclerView mRecycleView;
    private TextView emptyView;

    String keywordFilter;
    String locationFilter;
    String priceLowFilter;
    String priceHighFilter;
    String condoFilter;
    String houseFilter;
    String apartmentFilter;
    String townhouseFilter;

    public SavedSearchResultActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getIntent().getStringExtra("name"));
        }

        mRecycleView = (RecyclerView) findViewById(R.id.property_list);
        emptyView = (TextView) findViewById(R.id.empty_view);

        keywordFilter = getIntent().getStringExtra("keyword");
        locationFilter = getIntent().getStringExtra("location");
        priceLowFilter = getIntent().getStringExtra("pricelow");
        priceHighFilter = getIntent().getStringExtra("pricehigh");
        condoFilter = getIntent().getStringExtra("condo");
        houseFilter = getIntent().getStringExtra("house");
        townhouseFilter = getIntent().getStringExtra("townhouse");
        apartmentFilter = getIntent().getStringExtra("apartment");
        startSearch();
    }

    private void startSearch() {
        new PropertySearchTask(this, mRecycleView, emptyView).execute(keywordFilter, locationFilter, priceLowFilter, priceHighFilter,
                String.valueOf(condoFilter), String.valueOf(apartmentFilter), String.valueOf(houseFilter), String.valueOf(townhouseFilter), "");
        System.out.println("keyword filter" + keywordFilter);
    }
}

