package edu.sjsu.cmpe277.rentalapp.createpost;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import edu.sjsu.cmpe277.rentalapp.Manifest;
import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.imageutility.ImageUtility;
import edu.sjsu.cmpe277.rentalapp.localdbmanager.DBHandler;
import edu.sjsu.cmpe277.rentalapp.pojo.GlobalPojo;
import edu.sjsu.cmpe277.rentalapp.pojo.Property;
import edu.sjsu.cmpe277.rentalapp.rentalapp.PropertyDetailFragment;
import edu.sjsu.cmpe277.rentalapp.rentalapp.WebService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * Use the {@link CreateNewPropertyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNewPropertyFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PROPERTY_ID = "param1";
    private static final int RESULT_IMAGE=007;
    private static final int MY_PERMISSIONS_REQUEST_READ_FILES=33;

    // TODO: Rename and change types of parameters
    private String propertyId;




    private EditText titleEditText;
    private EditText detailsEditText;
    private EditText areaSftEditText;
    private Spinner apartmentTypeSpinner;
    private Spinner noOfBedRoomsSpinner;
    private Spinner noOfBathRoomsSpinner;
    private EditText rentAmountEditText;
    private EditText phoneNoEditText;
    private EditText emailEditText;
    private EditText addressLineEditText;
    private EditText cityEditText;
    private EditText stateEditText;
    private EditText zipCodeEditText;
    private ProgressDialog mProgressDialog;
    private Button submitButton;
    private Button attachImageButton;
    private ImageView imageView;
    private String uniqueUserID;
    private int noOfViews=0;
    private String imageUrl;
    private Bitmap bitmap;

    AwesomeValidation mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

    public CreateNewPropertyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment CreateNewPropertyFragment.
     */

    public static CreateNewPropertyFragment newInstance(String param1) {
        CreateNewPropertyFragment fragment = new CreateNewPropertyFragment();
        Bundle args = new Bundle();
        args.putString(PROPERTY_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            propertyId = getArguments().getString(PROPERTY_ID);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("bitmap", bitmap);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(null!=savedInstanceState) {
            bitmap=savedInstanceState.getParcelable("bitmap");
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_new_property, container, false);
        getUIReferance(view);
        setUIValidation();
        uniqueUserID = ((GlobalPojo) getActivity().getApplicationContext()).getEmail();
        submitButton.setOnClickListener(this);
        attachImageButton.setOnClickListener(this);
        if (!TextUtils.isEmpty(propertyId))
            setUIValues();
        return view;
    }

    private void setUIValues() {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                return new WebService().getPropertyDetails(params[0]);
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                try {
                    processJson(response);
                } catch (JSONException ex) {
                    Log.d("Create New Property", ex.getMessage());
                    System.out.print(ex);
                }


            }
        }.execute(propertyId);
    }

    public void processJson(String jsonStuff) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonStuff);
        JSONObject json = jsonArray.getJSONObject(0);

        try {

            titleEditText.setText(json.getString(DBHandler.TABLE_PROPERTY_TITLE));
            detailsEditText.setText(json.getString(DBHandler.TABLE_PROPERTY_DESC));
            areaSftEditText.setText(json.getString(DBHandler.TABLE_PROPERTY_SIZE));
            apartmentTypeSpinner.setSelection(((ArrayAdapter) apartmentTypeSpinner.getAdapter()).getPosition(json.getString(DBHandler.TABLE_PROPERTY_TYPE)));
            noOfBedRoomsSpinner.setSelection(((ArrayAdapter) noOfBedRoomsSpinner.getAdapter()).getPosition(json.getString(DBHandler.TABLE_PROPERTY_BED)));
            noOfBathRoomsSpinner.setSelection(((ArrayAdapter) noOfBathRoomsSpinner.getAdapter()).getPosition(json.getString(DBHandler.TABLE_PROPERTY_BATH)));
            rentAmountEditText.setText(json.getString(DBHandler.TABLE_PROPERTY_PRICE));
            phoneNoEditText.setText(json.getString(DBHandler.TABLE_PROPERTY_PHONE));
            emailEditText.setText(json.getString(DBHandler.TABLE_PROPERTY_EMAIL));
            addressLineEditText.setText(json.getJSONObject(DBHandler.TABLE_PROPERTY_ADDRESS).getString(DBHandler.TABLE_PROPERTY_ADDRESSLINE1));
            cityEditText.setText(json.getJSONObject(DBHandler.TABLE_PROPERTY_ADDRESS).getString(DBHandler.TABLE_PROPERTY_ADDRESSCITY));
            stateEditText.setText(json.getJSONObject(DBHandler.TABLE_PROPERTY_ADDRESS).getString(DBHandler.TABLE_PROPERTY_ADDRESSSTATE));
            zipCodeEditText.setText(json.getJSONObject(DBHandler.TABLE_PROPERTY_ADDRESS).getString(DBHandler.TABLE_PROPERTY_ADDRESSZIP));
            noOfViews=json.getInt(DBHandler.TABLE_PROPERTY_VIEWCOUNT);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getUIReferance(View view) {
        //start: getting UI elements ref
        titleEditText = (EditText) view.findViewById(R.id.post_name);
        detailsEditText = (EditText) view.findViewById(R.id.post_description);
        areaSftEditText = (EditText) view.findViewById(R.id.post_area_units);
        apartmentTypeSpinner = (Spinner) view.findViewById(R.id.post_apartment_type);
        noOfBedRoomsSpinner = (Spinner) view.findViewById(R.id.post_noofbedrooms);
        noOfBathRoomsSpinner = (Spinner) view.findViewById(R.id.post_noofbathrooms);
        rentAmountEditText = (EditText) view.findViewById(R.id.post_rent);
        phoneNoEditText = (EditText) view.findViewById(R.id.post_contactnumber);
        emailEditText = (EditText) view.findViewById(R.id.post_email);
        addressLineEditText = (EditText) view.findViewById(R.id.post_line);
        cityEditText = (EditText) view.findViewById(R.id.post_city);
        stateEditText = (EditText) view.findViewById(R.id.post_state);
        zipCodeEditText = (EditText) view.findViewById(R.id.post_zip);
        attachImageButton=(Button)view.findViewById(R.id.attach_image_button);
        imageView=(ImageView)view.findViewById(R.id.propertyImage);
        submitButton = (Button) view.findViewById(R.id.post_submit);
        //end UI ref
    }


    private void setUIValidation() {
        final String ERROR_NOTEMPTY = "Can't be blank";
        final String ERROR_INVALIDPHONE = "INVALID CONTACT";
        final String ERROR_INVALIDEMAIL = "INVALID EMAIL";


        mAwesomeValidation.addValidation(titleEditText, RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);
        mAwesomeValidation.addValidation(detailsEditText, RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);
        mAwesomeValidation.addValidation(areaSftEditText, RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);
        mAwesomeValidation.addValidation(rentAmountEditText, RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);
        mAwesomeValidation.addValidation(phoneNoEditText, RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);
        mAwesomeValidation.addValidation(emailEditText, RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);
        mAwesomeValidation.addValidation(emailEditText, android.util.Patterns.EMAIL_ADDRESS, ERROR_INVALIDEMAIL);
        mAwesomeValidation.addValidation(phoneNoEditText, RegexTemplate.TELEPHONE, ERROR_INVALIDPHONE);
        mAwesomeValidation.addValidation(addressLineEditText, RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);
        mAwesomeValidation.addValidation(cityEditText, RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);
        mAwesomeValidation.addValidation(stateEditText, RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);
        mAwesomeValidation.addValidation(zipCodeEditText, RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_submit:
                submitToServer();
                break;
            case R.id.attach_image_button:
                attachImage();
                break;
        }
    }

    private void attachImage(){
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_FILES);
        }
        else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, RESULT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        System.out.print(requestCode);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_FILES:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    attachImage();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                attachImage();
                } else {
                    Toast.makeText(getContext(), "You may need to accept the permission to upload a picture", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            break;
            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_IMAGE==requestCode && Activity.RESULT_OK==resultCode && null!=data){

            try {
                ImageUtility imageUtility=new ImageUtility(getActivity());
                imageUrl= imageUtility.getPath(data.getData());
                bitmap = imageUtility.getBitmap(imageUrl, 256, 256);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException ex){
                Toast.makeText(getContext(),"Can not read file location",Toast.LENGTH_LONG);
                System.out.print(ex);
            }

        }
    }



    /**
     * Send data to server
     */
    private void submitToServer() {
        if (mAwesomeValidation.validate()) {
            final Property property = getProperty();
            if (null == propertyId) {
                new WebService().postProperty(getContext(),property);
                //new CreatePostTask(getContext()).execute(property);
            } else {
                new AsyncTask<Property, String, String>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        showProgressDialog();

                    }

                    @Override
                    protected String doInBackground(Property... params) {
                        new WebService().updateProperty(params[0], propertyId);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        hideProgressDialog();
                    }
                }.execute(property);

            }

            Intent intent = new Intent(getContext(), PostSuccessscreenActivity.class);
            clearForm();
            startActivity(intent);

        }
    }

    private void clearForm() {
        titleEditText.setText("");
        detailsEditText.setText("");
        areaSftEditText.setText("");
        apartmentTypeSpinner.setSelection(0);
        noOfBedRoomsSpinner.setSelection(0);
        noOfBathRoomsSpinner.setSelection(0);
        rentAmountEditText.setText("");
        phoneNoEditText.setText("");
        emailEditText.setText("");
        addressLineEditText.setText("");
        cityEditText.setText("");
        stateEditText.setText("");
        zipCodeEditText.setText("");

    }

    /**
     * To populate property object
     *
     * @return
     */
    private Property getProperty() {
        Property property = new Property();
        property.setName(titleEditText.getText().toString());
        property.setDescription(detailsEditText.getText().toString());
        String sft = areaSftEditText.getText().toString();
        if (!TextUtils.isEmpty(sft))
            property.setSize(Integer.parseInt(sft));
        property.setType(apartmentTypeSpinner.getSelectedItem().toString());
        property.setNoOfBedRooms(noOfBedRoomsSpinner.getSelectedItemPosition() + 1);
        property.setNoOfBathRooms(noOfBathRoomsSpinner.getSelectedItemPosition() + 1);
        String rentAmount = rentAmountEditText.getText().toString();
        if (!TextUtils.isEmpty(rentAmount))
            property.setPrice(Double.parseDouble(rentAmount));
        property.setPhone(phoneNoEditText.getText().toString());
        property.setUserEmail(emailEditText.getText().toString());
        property.getAddress().setLine(addressLineEditText.getText().toString());
        property.getAddress().setCity(cityEditText.getText().toString());
        property.getAddress().setState(stateEditText.getText().toString());
        property.getAddress().setZip(zipCodeEditText.getText().toString());
        property.setUniqueUserId(uniqueUserID);
        property.setNoOfViwes(noOfViews);
        property.setStatus(PropertyDetailFragment.STATUS_AVAILABLE);
        property.setImageUrl(imageUrl);
        return property;
    }



    private void showProgressDialog() {
        //Looper.prepare();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getActivity().getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


}
