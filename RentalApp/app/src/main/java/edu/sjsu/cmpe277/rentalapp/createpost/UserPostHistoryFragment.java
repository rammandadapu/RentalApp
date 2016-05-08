package edu.sjsu.cmpe277.rentalapp.createpost;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.rentalapp.PropertySearchTask;

/**
 * A fragment representing a list of Items.
 *
 */
public class UserPostHistoryFragment extends Fragment {

    public UserPostHistoryFragment() {
    }

    private RecyclerView mRecycleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.property_list, container, false);
        setHasOptionsMenu(true);
        mRecycleView = (RecyclerView)view.findViewById(R.id.property_list);
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        String uniqueUserID=((TextView) navigationView.getHeaderView(0).findViewById(R.id.email)).getText().toString();
        //TODO: NEED to add user ID in search terms
        new SearchUserPostsTask(getActivity(), mRecycleView).execute("", "san jose", "", "", "");



        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.user_post_menu, menu);

        /** Get the action view of the menu item whose id is search */
        MenuItem addNewButton = menu.findItem(R.id.new_post_icon);
        addNewButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new CreateNewPropertyFragment()).commit();
                return true;
            }
        });


    }
}
