package edu.sjsu.cmpe277.rentalapp.savedsearch;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.pojo.GlobalPojo;
import edu.sjsu.cmpe277.rentalapp.rentalapp.PropertySearchTask;
import edu.sjsu.cmpe277.rentalapp.rentalapp.dummy.DummyContent;
import edu.sjsu.cmpe277.rentalapp.rentalapp.dummy.DummyContent.DummyItem;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SavedSearchFragment extends Fragment {

    private RecyclerView mRecycleView;
    private TextView emptyView;

    private GlobalPojo globalPojo;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SavedSearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_savedsearch_list, container, false);

        mRecycleView = (RecyclerView) view.findViewById(R.id.saved_search_recycle_view);
        emptyView = (TextView) view.findViewById(R.id.empty_view);

        globalPojo=(GlobalPojo)getActivity().getApplicationContext();

        new SavedSearchListTask(((AppCompatActivity) getActivity()), mRecycleView, emptyView).execute(globalPojo.getEmail());
        return view;
    }
}
