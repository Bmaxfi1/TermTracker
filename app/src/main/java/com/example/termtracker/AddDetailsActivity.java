package com.example.termtracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.termtracker.Model.CanBeAddedToDatabase;

public class AddDetailsActivity extends AppCompatActivity{

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras(); //getting the passed in key/value pairs
        if (bundle != null) {
            String value = bundle.getString("type");

            switch (value) {
                case "note":
                    ab.setTitle("New Note");
                    setCurrentFragment(new AddNoteFragment());
                    break;
                case "assessment":
                    ab.setTitle("New Assessment");
                    setCurrentFragment(new AddAssessmentFragment());
                    break;
                case "course":
                    ab.setTitle("New Course");
                    setCurrentFragment(new AddCourseFragment());
                    break;
                case "term":
                    ab.setTitle("New Term");
                    setCurrentFragment(new AddTermFragment());
                    break;
            }
        }
    }

    //overloaded default method, used to set the appbar menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.confirmation_menu, menu);
        return true;
    }

    // Overridden default method used to handle click events on menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        if (id == R.id.appbar_confirm) {
            CanBeAddedToDatabase currentFragment = (CanBeAddedToDatabase)getSupportFragmentManager().findFragmentById(R.id.fragment_display_add);
            assert currentFragment != null;
            currentFragment.addNewItem();
        } else if (id == R.id.cancel_button) {
            Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setCurrentFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_display_add, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}