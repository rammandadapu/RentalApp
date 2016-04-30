package edu.sjsu.cmpe277.rentalapp.rentalapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.dummy.DummyContent;
/**
 * A simple {@link Fragment} subclass.
 */
public class PropertyListFragment extends Fragment {
    private RecyclerView mRecycleView;
    private SimpleItemRecyclerViewAdapter mSimpleItemRecyclerViewAdapter;
    public PropertyListFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.property_list, container, false);
        mRecycleView = (RecyclerView)view.findViewById(R.id.property_list);
        //modify this according to the search implementation
        //mSimpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(getActivity(), DummyContent.ITEMS);
        mRecycleView.setAdapter(mSimpleItemRecyclerViewAdapter);
        return view;
    }
}