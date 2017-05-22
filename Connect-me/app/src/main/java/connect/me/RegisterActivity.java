package connect.me;

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

public class RegisterActivity extends AppCompatActivity  implements View.OnClickListener{

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

        progressDialog = new ProgressDialog(this);

        etAge = (EditText) findViewById(R.id.etAge);
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (Button) findViewById(R.id.bRegister);
        tvSingIn = (TextView) findViewById(R.id.tvSignIn);

        bRegister.setOnClickListener(this);
        tvSingIn.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        if(v==bRegister){
            registerUser();
        }
        //open the login activity
        if(v==tvSingIn){

        }
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

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //user is successfully registered
                            Toast.makeText(RegisterActivity.this,"Registered successfully",Toast.LENGTH_SHORT).show();

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
