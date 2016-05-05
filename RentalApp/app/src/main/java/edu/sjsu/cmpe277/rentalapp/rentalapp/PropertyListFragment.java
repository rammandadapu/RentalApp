package edu.sjsu.cmpe277.rentalapp.rentalapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.view.MenuInflater;

import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.dummy.DummyContent;
/**
 * A simple {@link Fragment} subclass.
 */
public class PropertyListFragment extends Fragment
        implements SearchView.OnQueryTextListener {
    private RecyclerView mRecycleView;
    SearchView searchView;
    private SimpleItemRecyclerViewAdapter mSimpleItemRecyclerViewAdapter;
    public PropertyListFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.property_list, container, false);
        setHasOptionsMenu(true);
        mRecycleView = (RecyclerView)view.findViewById(R.id.property_list);
        //modify this according to the search implementation
        //mSimpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(getActivity(), DummyContent.ITEMS);
        mRecycleView.setAdapter(mSimpleItemRecyclerViewAdapter);
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
            searchView = (SearchView) v;

            searchView.setIconifiedByDefault(false);
            //searchView.setSubmitButtonEnabled(true);
            searchView.setQueryHint("Search here");
            //searchView.setOnQueryTextListener(this);

            searchView.setMaxWidth(Integer.MAX_VALUE);

            searchView.setQuery("test", false);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        //searchTerm = query;
        //new RestaurantsSearchTask(NavActivity.this, list).execute(query, location, latLngString, sortOption);

        //InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        return true;
    }
}