package com.ledokol.studentslab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ledokol.studentslab.events.MainEvents;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ledokol.studentslab.create.CreateEvent;
import com.ledokol.studentslab.registration.LoginActivity;
import com.ledokol.studentslab.registration.RegistrationActivity;

public class MainActivity extends AppCompatActivity {
    FirebaseUser user;
    FirebaseAuth mAuth;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_main_events:
                    Fragment fragment=new MainEvents();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    Toast.makeText(getApplicationContext(),"Profile",Toast.LENGTH_SHORT).show();;
                    //здесь запустить профиль
//                    Fragment fragment=new MainEvents();
//                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user == null){
            signIn();
        }
        else {
            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_viewMain);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//            Intent intent = new Intent(MainActivity.this, CreateEvent.class);
//            startActivity(intent);
        }


    }

    void signIn(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_viewMain);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

//
//    private BottomNavigationView setBottomNavigationView(String navigateFragment) {
//        BottomNavigationView navView = findViewById(R.id.nav_viewMain);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_play, R.id.navigation_dashboard, R.id.navigation_rank, R.id.navigation_notifications)
//                .build();
//        navController = Navigation.findNavController(this, R.id.nav_host_fragmentMain);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
//
//        setNavigateFragment(navController,navigateFragment);
//        return navView;
//    }

}