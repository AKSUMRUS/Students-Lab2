package com.ledokol.studentslab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ledokol.studentslab.registration.RegistrationActivity;

public class Introduction extends AppCompatActivity {

    FirebaseUser user;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();
    }


    @Override
    public void onStart(){
        super.onStart();
        openMainActivity();
    }
    public void openMainActivity(){
        if (user == null) {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        }else {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FirebaseUser userVerify = mAuth.getCurrentUser();

                    if (!userVerify.isEmailVerified()) {
                        Intent intent = new Intent(Introduction.this, MainActivity.class);
                        intent.putExtra("Verify",true);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }
    }
}