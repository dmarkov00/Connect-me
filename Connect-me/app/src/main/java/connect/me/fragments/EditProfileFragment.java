package connect.me.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.DialogFragment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import connect.me.R;
import connect.me.activities.MainActivity;
import connect.me.databaseIntegration.firebaseInteraction.Dispatcher;
import connect.me.databaseIntegration.models.AdditionalUserData;

import static android.R.attr.fragment;
import static connect.me.R.id.radioButton;
import static connect.me.R.id.radioGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends DialogFragment {

    Spinner spinner;

    private EditText etAge;
    private EditText etName;
    private EditText etPhoneNumber;

    private Dispatcher dispatcher;
    private String etGender;

    public EditProfileFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();

        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
   //     super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        dispatcher = new Dispatcher();


          etAge = (EditText) view.findViewById(R.id.textAge);
          etName = (EditText) view.findViewById(R.id.textName);
          etPhoneNumber = (EditText) view.findViewById(R.id.textNumber);

        RadioGroup rg = (RadioGroup) view.findViewById(R.id.radioGroup);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioButtonMale:
                        // do operations specific to this selection
                        etGender = "Male";
                        break;
                    case R.id.radioButtonFemale:
                        // do operations specific to this selection
                        etGender = "Female";
                        break;
                    case R.id.radioButtonOther:
                        // do operations specific to this selection
                        etGender = "Other";
                        break;
                }
            }
        });

        Button bSubmit = (Button) view.findViewById(R.id.buttonUpdate);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdateUserData();
                startActivity(new Intent(getContext(), MainActivity.class));

            }
        });

        return view;

    }

    public void UpdateUserData(){
        String name = (String) etName.getText().toString();
        int age = Integer.parseInt(etAge.getText().toString());
        String phoneNumber = (String) etPhoneNumber.getText().toString();
        dispatcher.assignAdditionalDataToUser(name,age,phoneNumber,etGender);
    }
   }
