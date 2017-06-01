package connect.me.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import connect.me.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText etAge;
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private Button bRegister;
    private TextView tvSingIn;

    private ProgressDialog progressDialog;

    //firebase authentication object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //intializing firebase object
        firebaseAuth = FirebaseAuth.getInstance();

        //tracking if the user is already signed in
//        if(firebaseAuth.getCurrentUser() != null)//means that user is already logged in
//        {
//            main activity here
//            finish();
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//
//        }

        progressDialog = new ProgressDialog(this);

        etAge = (EditText) findViewById(R.id.etAge);
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etAgee);
        etPassword = (EditText) findViewById(R.id.etPhoneNumber);
        bRegister = (Button) findViewById(R.id.bSubmit);
        tvSingIn = (TextView) findViewById(R.id.tvSignIn);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        tvSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        //creating an intent to change to main activity after registration
        final Intent MainIntent = new Intent(this,MainActivity.class);


        //check if the input fields are empty
        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            //stop the function from further execution
            return;
        }
        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            //stop the function from further execution
            return;
        }
        //if email and password are filled in we are registering our user
        progressDialog.setMessage("Registering user in progress");
        progressDialog.show();

        //create new user
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //user is successfully registered
                            //tracking if the user is already signed in
                            if(firebaseAuth.getCurrentUser() != null)//means that user is already logged in
                            {
                                //main activity here(map)
                                finish();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));

                            }

                            progressDialog.hide();
                            startActivity(MainIntent);
                        }else{
                            Toast.makeText(RegisterActivity.this,"Could nor register. Please try again.",Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }
                    }
                });



    }
}
