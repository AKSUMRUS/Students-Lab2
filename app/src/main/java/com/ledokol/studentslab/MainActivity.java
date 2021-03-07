package com.ledokol.studentslab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.MenuItem;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ledokol.studentslab.registration.LoginActivity;

public class MainActivity extends AppCompatActivity {
    FirebaseUser user;
    FirebaseAuth mAuth;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user == null){
            signIn();
        } else {
            fm=getSupportFragmentManager();
            fm.beginTransaction()
                    .add(R.id.main__content, new BottomNav())
                    .commit();
        }

    }

    void signIn(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {
        Log.e("onBackPressed",String.valueOf(fm.getBackStackEntryCount()));
        if (fm.getBackStackEntryCount() > 0)
            fm.popBackStack();
        else
            finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}