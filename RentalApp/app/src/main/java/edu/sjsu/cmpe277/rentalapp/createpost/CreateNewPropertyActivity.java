package edu.sjsu.cmpe277.rentalapp.createpost;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import edu.sjsu.cmpe277.rentalapp.R;

public class CreateNewPropertyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_property);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Edit Property");
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle extras=getIntent().getExtras();
            String propertyId=null;
            if(null!=extras) {
                 propertyId = extras.getString("_id");

            }


            CreateNewPropertyFragment fragment =  CreateNewPropertyFragment.newInstance(propertyId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.new_property_container, fragment)
                    .commit();
        }
    }


}
