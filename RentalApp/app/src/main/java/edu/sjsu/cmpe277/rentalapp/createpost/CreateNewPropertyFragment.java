package edu.sjsu.cmpe277.rentalapp.createpost;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.scribe.model.Response;

import java.util.concurrent.ExecutionException;

import edu.sjsu.cmpe277.rentalapp.R;
import edu.sjsu.cmpe277.rentalapp.pojo.Property;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateNewPropertyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateNewPropertyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNewPropertyFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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

    private Button submitButton;
    private String uniqueUserID;

    AwesomeValidation mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
    public CreateNewPropertyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateNewPropertyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateNewPropertyFragment newInstance(String param1, String param2) {
        CreateNewPropertyFragment fragment = new CreateNewPropertyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_new_property, container, false);
        getUIReferance(view);
        setUIValidation();
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        uniqueUserID=((TextView) navigationView.getHeaderView(0).findViewById(R.id.email)).getText().toString();
        submitButton.setOnClickListener(this);


        return view;
    }

    private void getUIReferance(View view){
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
        submitButton = (Button) view.findViewById(R.id.post_submit);
        //end UI ref
    }


    private void setUIValidation(){
         final String ERROR_NOTEMPTY ="Can't be blank";
         final String ERROR_INVALIDPHONE ="INVALID CONTACT";
         final String ERROR_INVALIDEMAIL="INVALID EMAIL";


        mAwesomeValidation.addValidation(titleEditText, RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);
        mAwesomeValidation.addValidation(detailsEditText, RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);
        mAwesomeValidation.addValidation(areaSftEditText, RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);
        mAwesomeValidation.addValidation(rentAmountEditText,RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);
        mAwesomeValidation.addValidation(phoneNoEditText,RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);
        mAwesomeValidation.addValidation(emailEditText,RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);
        mAwesomeValidation.addValidation(emailEditText,android.util.Patterns.EMAIL_ADDRESS, ERROR_INVALIDEMAIL);
        mAwesomeValidation.addValidation(phoneNoEditText,RegexTemplate.TELEPHONE, ERROR_INVALIDPHONE);
        mAwesomeValidation.addValidation(addressLineEditText,RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);
        mAwesomeValidation.addValidation(cityEditText,RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);
        mAwesomeValidation.addValidation(stateEditText,RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);
        mAwesomeValidation.addValidation(zipCodeEditText,RegexTemplate.NOT_EMPTY, ERROR_NOTEMPTY);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_submit:
                submitToServer();
                break;
        }
    }

    /**
     * Send data to server
     */
    private void submitToServer() {
        if(mAwesomeValidation.validate()) {
            Property property = getProperty();
            new CreatePostTask(getContext()).execute(property);
        }
    }

    /**
     * To populate property object
     * @return
     */
    private Property getProperty(){
        Property property = new Property();
        property.setName(titleEditText.getText().toString());
        property.setDescription(detailsEditText.getText().toString());
        String sft = areaSftEditText.getText().toString();
        if (!TextUtils.isEmpty(sft))
            property.setSize(Integer.parseInt(sft));
        property.setType(apartmentTypeSpinner.getSelectedItem().toString());
        property.setNoOfBedRooms(noOfBedRoomsSpinner.getSelectedItemPosition() + 1);
        property.setNoOfBathRooms(noOfBathRoomsSpinner.getSelectedItemPosition() + 1);
        String rentAmount = areaSftEditText.getText().toString();
        if (!TextUtils.isEmpty(rentAmount))
            property.setPrice(Double.parseDouble(rentAmount));
        property.setPhone(phoneNoEditText.getText().toString());
        property.setUserEmail(emailEditText.getText().toString());
        property.getAddress().setLine(addressLineEditText.getText().toString());
        property.getAddress().setCity(cityEditText.getText().toString());
        property.getAddress().setState(stateEditText.getText().toString());
        property.getAddress().setZip(zipCodeEditText.getText().toString());
        property.setUniqueUserId(uniqueUserID);
        property.setStatus("Available");

        return property;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
