package edu.sjsu.cmpe277.rentalapp.createpost;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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


    private EditText editTextTitle;
    private EditText editTextDetails;
    private EditText editTextAddress;
    private Spinner spinnerNoofBedrooms;
    private Spinner spinnerNoofBathrooms;
    private Button buttonSubmit;

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
        View view= inflater.inflate(R.layout.fragment_create_new_property, container, false);
        editTextTitle=(EditText)view.findViewById(R.id.post_name);
        editTextDetails=(EditText)view.findViewById(R.id.post_description);
        editTextAddress=(EditText)view.findViewById(R.id.post_line);
        spinnerNoofBathrooms=(Spinner)view.findViewById(R.id.post_noofbathrooms);
        spinnerNoofBedrooms=(Spinner)view.findViewById(R.id.post_noofbedrooms);
        buttonSubmit=(Button)view.findViewById(R.id.post_submit);
        buttonSubmit.setOnClickListener(this);


        return view;
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

    private void submitToServer(){
        Property property=new Property();
        property.setName(editTextTitle.getText().toString());
        property.setDescription(editTextDetails.getText().toString());
        property.getAddress().setLine(editTextAddress.getText().toString());
        new CreatePostTask(getContext()).execute(property);
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
