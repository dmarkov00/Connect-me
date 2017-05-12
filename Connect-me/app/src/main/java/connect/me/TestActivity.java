package connect.me;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import connect.me.Fragments.ProfileFragment;

// Note: `FragmentActivity` works here as well
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

    }

    private void showProfile() {

        FragmentManager fm = getSupportFragmentManager();
        // We can pass the from the selected person and retrieve him from the database
        ProfileFragment profileFragment = ProfileFragment.newInstance("Some Title");
        profileFragment.show(fm, "fragment_profile");

    }
    // Button handler
    public void invokeFragment(View view) {
        showProfile();
    }


}
