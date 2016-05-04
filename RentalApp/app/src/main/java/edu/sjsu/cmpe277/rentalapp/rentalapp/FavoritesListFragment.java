package edu.sjsu.cmpe277.rentalapp.rentalapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.dummy.DummyContent;
import edu.sjsu.cmpe277.rentalapp.localdbmanager.DBHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesListFragment extends Fragment {
    private RecyclerView mRecycleView;
    private SimpleItemRecyclerViewAdapter mSimpleItemRecyclerViewAdapter;
    DBHandler dbHandler;
    public FavoritesListFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbHandler = new DBHandler(getActivity(),null,null,0);
        View view = inflater.inflate(R.layout.property_list, container, false);
        mRecycleView = (RecyclerView)view.findViewById(R.id.property_list);
        mSimpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(getActivity(), dbHandler.getAllProperties());
        mRecycleView.setAdapter(mSimpleItemRecyclerViewAdapter);
        return view;
    }
}