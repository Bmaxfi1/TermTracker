package com.example.termtracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class AddDetailsActivity extends AppCompatActivity {

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
                    //set up the layout for this type
                    Toast.makeText(getApplicationContext(), "passed note into addDetails", Toast.LENGTH_SHORT).show();
                    ab.setTitle("New Note");
                    setCurrentFragment(new AddNoteFragment());//debug

                    break;
                case "assessment":
                    Toast.makeText(getApplicationContext(), "passed assessment into addDetails", Toast.LENGTH_SHORT).show();
                    ab.setTitle("New Assessment");
                    break;
                case "course":
                    Toast.makeText(getApplicationContext(), "passed course into addDetails", Toast.LENGTH_SHORT).show();
                    ab.setTitle("New Course");
                    break;
                case "term":
                    Toast.makeText(getApplicationContext(), "passed term into addDetails", Toast.LENGTH_SHORT).show();
                    ab.setTitle("New Term");
                    Fragment fragment = new AddTermFragment();
                    setCurrentFragment(fragment);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        // Display menu item's title by using a Toast.
        //todo functionality to these buttons
        if (id == R.id.confirm_button) {
            Toast.makeText(getApplicationContext(), "Confirm", Toast.LENGTH_SHORT).show();
            return true;
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