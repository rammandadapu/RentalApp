package edu.sjsu.cmpe277.rentalapp.createpost;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.pojo.Property;
import edu.sjsu.cmpe277.rentalapp.rentalapp.WebService;

/**
 * Created by ram.mandadapu on 5/7/16.
 */
public class CreatePostTask extends AsyncTask<Property,String,Boolean> {

    private Context context;

    public  CreatePostTask(Context context){
        this.context=context;
    }

    @Override
    protected Boolean doInBackground(Property... propertyList) {
        WebService webService=new WebService();
        return webService.postProperty(propertyList[0]);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean)
            Toast.makeText(context, R.string.post_success,Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context,R.string.post_fail,Toast.LENGTH_SHORT).show();
    }
}
