package com.example.termtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setCurrentFragment(new HomeFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    public void setCurrentFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_display, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            item -> {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        setCurrentFragment(fragment);
                        setTitle("TermTracker");
                        return true;
                    case R.id.nav_terms:
                        fragment = new TermsFragment();
                        setCurrentFragment(fragment);
                        setTitle("Terms");
                        return true;
                    case R.id.nav_courses:
                        fragment = new CoursesFragment();
                        setCurrentFragment(fragment);
                        setTitle("Courses");
                        return true;
                    case R.id.nav_assessments:
                        fragment = new AssessmentsFragment();
                        setCurrentFragment(fragment);
                        setTitle("Assessments");
                        return true;

                }
                return false;
            };
}
