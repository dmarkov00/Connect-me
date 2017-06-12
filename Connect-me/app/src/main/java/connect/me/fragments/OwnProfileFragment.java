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
import android.widget.Toast;

import connect.me.R;
import connect.me.activities.MainActivity;
import connect.me.databaseIntegration.models.AdditionalUserData;

/**
 * Created by Alex on 11-Jun-17.
 */

public class OwnProfileFragment extends DialogFragment {


    public OwnProfileFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below

    }

    public static OwnProfileFragment newInstance(AdditionalUserData user) {

        OwnProfileFragment frag = new OwnProfileFragment();
        Bundle args = new Bundle();



        args.putParcelable("user", (Parcelable) user);
        frag.setArguments(args);
        return frag;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_own, container, false);
        Button button = (Button) view.findViewById(R.id.button_Edit);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                FragmentManager fm = getFragmentManager();
                EditProfileFragment editFragment = EditProfileFragment.newInstance();
                editFragment.show(fm, "fragment_edit_profile");

            }
        });
        return view;
    }

    @Override

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AdditionalUserData user = getArguments().getParcelable("user");


        TextView nameTextView = (TextView) view.findViewById(R.id.user_profile_name);
        TextView phoneTextView = (TextView) view.findViewById(R.id.user_profile_phone_number);
        TextView genderTextView = (TextView) view.findViewById(R.id.user_profile_gender);
        TextView ageTextView = (TextView) view.findViewById(R.id.user_profile_age);

        nameTextView.setText(user.getName());
        phoneTextView.setText("Phone: " + user.getPhoneNumber());
        genderTextView.setText("Sex: " + user.getGender());
        ageTextView.setText("Age: "+ user.getAge()+"");


    }


}
