package connect.me.fragments;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import connect.me.R;
import connect.me.activities.MainActivity;
import connect.me.databaseIntegration.models.AdditionalUserData;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends DialogFragment {

    private EditText mEditText;

    public ProfileFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below

    }

    public static ProfileFragment newInstance(AdditionalUserData additionalUserData) {

        ProfileFragment frag = new ProfileFragment();
        Bundle args = new Bundle();

        args.putParcelable("additionalUserData", additionalUserData);
        frag.setArguments(args);
        return frag;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    @Override

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AdditionalUserData additionalUserData = getArguments().getParcelable("additionalUserData");

        TextView nameTextView = (TextView) view.findViewById(R.id.user_profile_name);
        TextView phoneTextView = (TextView) view.findViewById(R.id.user_profile_phone_number);
        TextView genderTextView = (TextView) view.findViewById(R.id.user_profile_gender);
        TextView ageTextView = (TextView) view.findViewById(R.id.user_profile_age);

        nameTextView.setText(additionalUserData.getName());
        phoneTextView.setText("Phone: " + additionalUserData.getPhoneNumber());
        genderTextView.setText("Sex: " + additionalUserData.getGender());
        ageTextView.setText("Age: "+ additionalUserData.getAge()+"");

    }


}
