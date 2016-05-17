package edu.sjsu.cmpe277.rentalapp.favorites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.dummy.DummyContent;
import edu.sjsu.cmpe277.rentalapp.localdbmanager.DBHandler;
import edu.sjsu.cmpe277.rentalapp.rentalapp.NavActivity;
import edu.sjsu.cmpe277.rentalapp.rentalapp.PropertyDetailActivity;
import edu.sjsu.cmpe277.rentalapp.rentalapp.PropertyDetailFragment;
import edu.sjsu.cmpe277.rentalapp.rentalapp.WebService;

/**
 * Created by divya.chittimalla on 4/27/16.
 */
public class SimpleItemRecyclerViewAdapterFavorites
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapterFavorites.ViewHolder> {

    private Context context;
    private final ArrayList mValues;

    public SimpleItemRecyclerViewAdapterFavorites(Context context, ArrayList items) {
        this.context = context;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.property_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        HashMap<String, String> map = (HashMap<String, String>) mValues.get(position);

        holder.mRentView.setText(map.get(DBHandler.TABLE_PROPERTY_PRICE));
        holder.mAddressView.setText(map.get(DBHandler.TABLE_PROPERTY_ADDRESS));
        holder.mBedBathView.setText(map.get(DBHandler.TABLE_PROPERTY_BEDBATH));
        if (null != map.get(DBHandler.TABLE_PROPERTY_IMAGE_URL))
            Picasso.with(context).load(WebService.baseURL + "download/" + map.get(DBHandler.TABLE_PROPERTY_IMAGE_URL))
                    .resize(256,256)
                    .into(holder.mImageView);
        else
            holder.mImageView.setImageResource(R.mipmap.buildings);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NavActivity.mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(PropertyDetailFragment.ARG_ITEM_ID, ((HashMap<String, String>) mValues.get(position)).get(DBHandler.TABLE_PROPERTY_ID));
                    PropertyDetailFragment fragment = new PropertyDetailFragment();
                    fragment.setArguments(arguments);
                    ((NavActivity) context).getSupportFragmentManager().beginTransaction()
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
        public final TextView mRentView;
        public final TextView mBedBathView;
        public final TextView mAddressView;
        public DummyContent.DummyItem mItem;
        public final ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mRentView = (TextView) view.findViewById(R.id.rent_list);
            mBedBathView = (TextView) view.findViewById(R.id.bed_bath_list);
            mAddressView = (TextView) view.findViewById(R.id.address_list);
            mImageView = (ImageView) view.findViewById(R.id.property_image_list);
        }

    }
}