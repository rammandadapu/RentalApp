package edu.sjsu.cmpe277.rentalapp.favorites;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.dummy.DummyContent;
import edu.sjsu.cmpe277.rentalapp.localdbmanager.DBHandler;
import edu.sjsu.cmpe277.rentalapp.rentalapp.DividerItemDecoration;
import edu.sjsu.cmpe277.rentalapp.rentalapp.NavActivity;
import edu.sjsu.cmpe277.rentalapp.rentalapp.SimpleItemRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesListFragment extends Fragment {
    private RecyclerView mRecycleView;
    private SimpleItemRecyclerViewAdapterFavorites mSimpleItemRecyclerViewAdapter;
    DBHandler dbHandler;
    public FavoritesListFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbHandler = new DBHandler(getActivity(),null,null,0);
        View view = inflater.inflate(R.layout.property_list, container, false);
        mRecycleView = (RecyclerView)view.findViewById(R.id.property_list);
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
        /*mSimpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapterFavorites(getActivity(), dbHandler.getAllProperties());
        mRecycleView.setAdapter(mSimpleItemRecyclerViewAdapter);
        mRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        System.out.println(dbHandler.getAllProperties());*/
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSimpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapterFavorites(getActivity(), dbHandler.getAllProperties());
        mRecycleView.setAdapter(mSimpleItemRecyclerViewAdapter);
        mRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        System.out.println(dbHandler.getAllProperties());
    }
}