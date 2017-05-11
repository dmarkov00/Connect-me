package connect.me;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import connect.me.Fragments.ProfileFragment;

// Note: `FragmentActivity` works here as well
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        showEditDialog();

    }
    private void showEditDialog() {

        FragmentManager fm = getSupportFragmentManager();
        ProfileFragment editNameDialogFragment = ProfileFragment.newInstance("Some Title");
        editNameDialogFragment.show(fm, "fragment_edit_name");

    }


}
