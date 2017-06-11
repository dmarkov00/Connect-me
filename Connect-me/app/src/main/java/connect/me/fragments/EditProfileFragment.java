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
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import connect.me.R;
import connect.me.activities.MainActivity;
import connect.me.databaseIntegration.firebaseInteraction.Dispatcher;
import connect.me.databaseIntegration.models.AdditionalUserData;

import static android.R.attr.fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> adapter;
    private EditText etAge;
    private EditText etName;
    private EditText etPhoneNumber;
    private Button bSubmit;

    private Dispatcher dispatcher;
    private String spinnerSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_profile);

        spinner = (Spinner) findViewById(R.id.spinner);
        etAge = (EditText) findViewById(R.id.textAge);
        etName = (EditText) findViewById(R.id.textName);
        etPhoneNumber = (EditText) findViewById(R.id.textNumber);
        bSubmit = (Button) findViewById(R.id.buttonSend);

        dispatcher = new Dispatcher();



        adapter  = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUserData();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

//    public static EditProfileFragment newInstance(AdditionalUserData user) {
//
//        EditProfileFragment frag = new EditProfileFragment();
//        Bundle args = new Bundle();
//
//        args.putParcelable("user", (Parcelable) user);
//        frag.setArguments(args);
//        return frag;
//
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerSelectedItem =  (String) parent.getItemAtPosition(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void UpdateUserData(){
        String name = (String) etName.getText().toString();
        int age = Integer.parseInt(etAge.getText().toString());
        String phoneNumber = (String) etPhoneNumber.getText().toString();
        dispatcher.assignAdditionalDataToUser(name,age,phoneNumber,spinnerSelectedItem);
    }
   }
