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

public class LoginActivity extends AppCompatActivity{

    private EditText etEmail;
    private EditText etPassword;
    private  Button blogin;
    private TextView registerLink;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail  = (EditText) findViewById(R.id.etAge);
        etPassword = (EditText) findViewById(R.id.etPhoneNumber);
        blogin = (Button) findViewById(R.id.bLogin);
        registerLink = (TextView) findViewById(R.id.tvRegisterHere);

        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            //create an intent that opens register activity
            Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            //the current activity performs the intent and opens register page
            LoginActivity.this.startActivity(registerIntent);
            }
        });

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        //tracking if the user is already signed in
//        if(firebaseAuth.getCurrentUser() != null)//means that user is already logged in
//        {
//            //main activity here
//           finish();
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//
//        }

    }

    private void userLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

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
        progressDialog.setMessage("Login in progress");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            //start the main activity
                            finish();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                    }
                });

    }
}
