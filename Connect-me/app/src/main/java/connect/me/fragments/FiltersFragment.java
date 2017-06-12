package connect.me.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import connect.me.R;


public class FiltersFragment extends DialogFragment {

    private OnFragmentInteractionListener mListener;
    private EditText ageEditText;
    private EditText distanceEditText;
    private RadioGroup genderRadioGroup;
    private RadioButton genderRadioButton;

    public FiltersFragment() {
        // Required empty public constructor
    }

    public void sendDataToActivity(String gender,float distance, int age) {
        if (mListener != null) {
            mListener.onFragmentInteraction(gender,distance,age);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String gender,float distance, int age);
    }

    // region some setters and removers
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //endregion
    //region things I got


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filters, container, false);

        ageEditText = (EditText) view.findViewById(R.id.ageEditText);
        distanceEditText = (EditText) view.findViewById(R.id.distanceEditText);
        genderRadioGroup = (RadioGroup) view.findViewById(R.id.genderRadioGroup);


        Button applyFiltersButton = (Button) view.findViewById(R.id.applyFiltersButton);
        applyFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v ) {
                int selectedId = genderRadioGroup.getCheckedRadioButtonId();
                genderRadioButton = (RadioButton) v.findViewById(selectedId);
                int age =  Integer.parseInt(ageEditText.getText().toString());
                float distance = Float.parseFloat(distanceEditText.getText().toString());
//                String gender = genderRadioButton.getText().toString();

                sendDataToActivity("Male",distance,age);
            }
        });
        return view;

    }


    public static FiltersFragment newInstance() {


        FiltersFragment fragment = new FiltersFragment();


        return fragment;
    }
    //endregion
}
