package connect.me.fragments;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.DialogFragment;
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

    public static ProfileFragment newInstance(AdditionalUserData user) {

        ProfileFragment frag = new ProfileFragment();
        Bundle args = new Bundle();

        args.putParcelable("user", (Parcelable) user);
        frag.setArguments(args);
        return frag;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AdditionalUserData user = getArguments().getParcelable("user");

        TextView nameTextView = (TextView) view.findViewById(R.id.user_profile_name);
        TextView phoneTextView = (TextView) view.findViewById(R.id.user_profile_phone_number);

//        nameTextView.setText(user.name);
//        phoneTextView.setText(user.phone);

    }


}
