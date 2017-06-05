package connect.me.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import connect.me.R;
import connect.me.databaseIntegration.firebaseInteraction.Dispatcher;

public class AdditionalDataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> adapter;
    private EditText etAge;
    private EditText etName;
    private EditText etPhoneNumber;
    private Button bSubmit;
    private TextView tvSkip;
    private Dispatcher dispatcher;
    private String spinnerSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_data);

        spinner = (Spinner) findViewById(R.id.spinnerGender);
        etAge = (EditText) findViewById(R.id.etAge);
        etName = (EditText) findViewById(R.id.etName);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        bSubmit = (Button) findViewById(R.id.bSubmit);
        tvSkip = (TextView) findViewById(R.id.tvSkip);
        dispatcher = new Dispatcher();



        adapter  = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //user does not want to input extra information. He is redirected to the map
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUserData();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

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
