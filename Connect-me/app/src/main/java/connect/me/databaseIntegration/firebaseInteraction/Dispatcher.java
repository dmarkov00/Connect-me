package connect.me.databaseIntegration.firebaseInteraction;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import connect.me.activities.MainActivity;
import connect.me.activities.RegisterActivity;
import connect.me.databaseIntegration.models.AdditionalUserData;
import connect.me.databaseIntegration.models.User;

/**
 * Created by Mirela on 5/31/2017.
 */

public class Dispatcher {
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private AdditionalUserData additionalUserData;

    public Dispatcher(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void createUser(){
        firebaseAuth.getCurrentUser();

    }

   public void assignAdditionalData(String name, int age, String phoneNumber, String gender){
            String userId = firebaseAuth.getCurrentUser().getUid();
            additionalUserData = new AdditionalUserData(gender,null,phoneNumber,name,age);
           mDatabase.child("additionalUserData").child(userId).setValue(additionalUserData);
       }


   }





