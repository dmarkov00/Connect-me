package connect.me;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etUsername = (EditText) findViewById(R.id.etUserName);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button blogin = (Button) findViewById(R.id.bLogin);
        final TextView registerLink = (TextView) findViewById(R.id.tvRegisterHere);

        registerLink.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //create an intent that opens register activity
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                //the current activity performs the intent and opens register page
                LoginActivity.this.startActivity(registerIntent);
            }
        });

    }
}
