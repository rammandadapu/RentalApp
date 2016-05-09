package edu.sjsu.cmpe277.rentalapp.createpost;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.scribe.model.Response;

import java.util.HashMap;

import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.localdbmanager.DBHandler;
import edu.sjsu.cmpe277.rentalapp.pojo.Property;
import edu.sjsu.cmpe277.rentalapp.rentalapp.PropertyDetailActivity;
import edu.sjsu.cmpe277.rentalapp.rentalapp.PropertyDetailFragment;
import edu.sjsu.cmpe277.rentalapp.rentalapp.WebService;

/**
 * Created by ram.mandadapu on 5/7/16.
 */
public class CreatePostTask extends AsyncTask<Property,String,String> {

    private Context context;

    public  CreatePostTask(Context context){
        this.context=context;
    }

    @Override
    protected String doInBackground(Property... propertyList) {
        WebService webService=new WebService();
        return webService.postProperty(propertyList[0]);

    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if(null!=response) {
            Toast.makeText(context, R.string.post_success, Toast.LENGTH_SHORT).show();
            /*Intent intent = new Intent(context, PropertyDetailFragment.class);
            intent.putExtra(PropertyDetailFragment.ARG_ITEM_ID, response);
            context.startActivity(intent);*/

        }
        else
            Toast.makeText(context,R.string.post_fail,Toast.LENGTH_SHORT).show();
    }
}
