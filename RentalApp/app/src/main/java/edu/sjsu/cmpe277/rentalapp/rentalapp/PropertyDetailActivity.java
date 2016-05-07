package edu.sjsu.cmpe277.rentalapp.rentalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.TextView;

import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.localdbmanager.DBHandler;
import edu.sjsu.cmpe277.rentalapp.localdbmanager.RentalProperty;

/**
 * An activity representing a single Property detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link PropertyListActivity}.
 */
public class PropertyDetailActivity extends AppCompatActivity {

    DBHandler dbHandler;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        dbHandler=new DBHandler(this,null,null,0);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        Bundle extras = getIntent().getExtras();
        String propertyId = extras.getString("_id");
        if(dbHandler.isFavourite(propertyId)) {
            toggleFavouriteImage(true);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                final String propertyId = extras.getString("_id");
                if (!dbHandler.isFavourite(propertyId)) {
                    RentalProperty rentalProperty = new RentalProperty();
                    rentalProperty.set_id(propertyId);
                    rentalProperty.setPrice(((TextView) findViewById(R.id.rent_detail)).getText().toString());
                    rentalProperty.setBedBath(((TextView) findViewById(R.id.bed_bath_detail)).getText().toString());
                    rentalProperty.setAddress(((TextView) findViewById(R.id.address_detail)).getText().toString());
                    dbHandler.addProperty(rentalProperty);
                    toggleFavouriteImage(true);
                    Toast.makeText(getApplicationContext(), "Added to saved houses", Toast.LENGTH_SHORT).show();
                } else {
                    dbHandler.deleteProperty(propertyId);
                    toggleFavouriteImage(false);
                    Toast.makeText(getApplicationContext(), "Removed from saved houses", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(PropertyDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(PropertyDetailFragment.ARG_ITEM_ID));
            PropertyDetailFragment fragment = new PropertyDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.property_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, PropertyListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleFavouriteImage(boolean flag){
        if(flag)
            fab.setImageResource(R.mipmap.gold_star);
        else
            fab.setImageResource(R.mipmap.white_star);
    }
}
