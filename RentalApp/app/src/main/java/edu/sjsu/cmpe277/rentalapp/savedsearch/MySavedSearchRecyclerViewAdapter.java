package edu.sjsu.cmpe277.rentalapp.savedsearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.dummy.DummyContent;
import edu.sjsu.cmpe277.rentalapp.localdbmanager.DBHandler;
import edu.sjsu.cmpe277.rentalapp.rentalapp.NavActivity;
import edu.sjsu.cmpe277.rentalapp.rentalapp.PropertyDetailActivity;
import edu.sjsu.cmpe277.rentalapp.rentalapp.PropertyDetailFragment;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MySavedSearchRecyclerViewAdapter extends RecyclerView.Adapter<MySavedSearchRecyclerViewAdapter.ViewHolder> {

    private AppCompatActivity activity;
    private final ArrayList mValues;

    public MySavedSearchRecyclerViewAdapter(AppCompatActivity activity, ArrayList items) {
        this.activity = activity;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_savedsearch, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        HashMap<String, String> map = (HashMap<String, String>) mValues.get(position);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        holder.mNameView.setText(map.get("name"));

        String keywordText = (TextUtils.isEmpty(map.get("keyword"))?"":"Keyword: "+map.get("keyword"));
        String priceRangeText = "Price range: $"+map.get("pricelow")+"-"+map.get("pricehigh");
        String houseText = (Boolean.parseBoolean(map.get("house"))?"House":"");
        String apartmentText = (Boolean.parseBoolean(map.get("apartment"))?"Apartment":"");
        String condoText = (Boolean.parseBoolean(map.get("condo"))?"Condo":"");
        String townhouseText = (Boolean.parseBoolean(map.get("townhouse"))?"Townhouse":"");

        if(!TextUtils.isEmpty(keywordText)) {
            keywordText += ", ";
        }

        if(!TextUtils.isEmpty(houseText)) {
            priceRangeText += ", ";
        }

        if(!TextUtils.isEmpty(apartmentText)) {
            houseText += ", ";
        }

        if(!TextUtils.isEmpty(condoText)) {
            apartmentText += ", ";
        }

        if(!TextUtils.isEmpty(townhouseText)) {
            condoText += ", ";
        }

        String filterText = keywordText + priceRangeText + houseText + apartmentText + condoText + townhouseText;

        holder.mFilterView.setText(filterText);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NavActivity.mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(PropertyDetailFragment.ARG_ITEM_ID, ((HashMap<String, String>) mValues.get(position)).get(DBHandler.TABLE_PROPERTY_ID));
                    PropertyDetailFragment fragment = new PropertyDetailFragment();
                    fragment.setArguments(arguments);
                    (activity).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.property_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, PropertyDetailActivity.class);
                    intent.putExtra(PropertyDetailFragment.ARG_ITEM_ID, ((HashMap<String, String>) mValues.get(position)).get(DBHandler.TABLE_PROPERTY_ID));

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mFilterView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.saved_search_name_list);
            mFilterView = (TextView) view.findViewById(R.id.filters_list);
        }
    }
}
