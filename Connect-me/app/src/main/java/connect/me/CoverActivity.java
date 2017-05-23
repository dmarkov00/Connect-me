package connect.me;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CoverActivity extends AppCompatActivity {

    private Button bLogin;
    private Button bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover);

        bLogin = (Button) findViewById(R.id.btLogin);
        bRegister = (Button)findViewById(R.id.btRegister);

        bLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

        bRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });
    }
}
