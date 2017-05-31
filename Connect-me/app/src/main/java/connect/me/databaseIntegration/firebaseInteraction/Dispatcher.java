package connect.me.databaseIntegration.firebaseInteraction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Mirela on 5/31/2017.
 */

public class Dispatcher {
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;

    public Dispatcher(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void createUser(){
        firebaseAuth.getCurrentUser();

    }
}
