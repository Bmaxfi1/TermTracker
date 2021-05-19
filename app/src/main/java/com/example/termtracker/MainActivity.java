package com.example.termtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creates a database if none exists already
        //todo

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        setCurrentFragment(new HomeFragment());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //changes the fragment that is on display
    public void setCurrentFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_display, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //overloaded default method, used to set the appbar menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

    // Overridden default method used to handle click events on appbar menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        // Display menu item's title by using a Toast.
        if (id == R.id.appbar_add) {
            Toast.makeText(getApplicationContext(), "Add", Toast.LENGTH_SHORT).show();
            launchAddActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void launchAddActivity() {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            item -> {
                Fragment fragment;
                getSupportActionBar();
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        setCurrentFragment(fragment);
                        getSupportActionBar().setTitle("TermTracker");
                        return true;
                    case R.id.nav_terms:
                        fragment = new TermsFragment();
                        setCurrentFragment(fragment);
                        getSupportActionBar().setTitle("Terms");
                        return true;
                    case R.id.nav_courses:
                        fragment = new CoursesFragment();
                        setCurrentFragment(fragment);
                        getSupportActionBar().setTitle("Courses");
                        return true;
                    case R.id.nav_assessments:
                        fragment = new AssessmentsFragment();
                        setCurrentFragment(fragment);
                        getSupportActionBar().setTitle("Assessments");
                        return true;

                }
                return false;
            };
}
