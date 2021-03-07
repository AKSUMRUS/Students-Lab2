package com.ledokol.studentslab;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ledokol.studentslab.create.CreateEvent;
import com.ledokol.studentslab.events.MainEvents;
import com.ledokol.studentslab.profile.ProfileActivity;

public class BottomNav extends Fragment {

    View view;
    FirebaseUser user;
    FirebaseAuth mAuth;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_main_events:
                    fragment=new MainEvents();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_add:
                    fragment=new CreateEvent();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    fragment=new ProfileActivity(user.getUid().toString());
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bottom_nav, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.nav_viewMain);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_main_events);

        return view;
    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

}