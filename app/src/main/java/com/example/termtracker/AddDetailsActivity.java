package com.example.termtracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.termtracker.Data.DatabaseHelper;
import com.example.termtracker.Misc.DatePickerFragment;
import com.example.termtracker.Model.Term;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddDetailsActivity extends AppCompatActivity{

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
                    Toast.makeText(getApplicationContext(), "passed note into addDetails", Toast.LENGTH_SHORT).show();
                    ab.setTitle("New Note");
                    setCurrentFragment(new AddNoteFragment());
                    break;
                case "assessment":
                    Toast.makeText(getApplicationContext(), "passed assessment into addDetails", Toast.LENGTH_SHORT).show();
                    ab.setTitle("New Assessment");
                    setCurrentFragment(new AddAssessmentFragment());
                    break;
                case "course":
                    Toast.makeText(getApplicationContext(), "passed course into addDetails", Toast.LENGTH_SHORT).show();
                    ab.setTitle("New Course");
                    setCurrentFragment(new AddCourseFragment());
                    break;
                case "term":
                    Toast.makeText(getApplicationContext(), "passed term into addDetails", Toast.LENGTH_SHORT).show();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        // Display menu item's title by using a Toast.
        //todo functionality to these buttons
        if (id == R.id.confirm_button) {
            Bundle bundle = getIntent().getExtras(); //getting the passed in key/value pairs
            if (bundle != null) {
                String value = bundle.getString("type");

                switch (value) {
                    case "note":
                        Toast.makeText(getApplicationContext(), "note confirm clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case "assessment":
                        Toast.makeText(getApplicationContext(), "assessment confirm clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case "course":
                        Toast.makeText(getApplicationContext(), "course confirm clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case "term":
                        Toast.makeText(getApplicationContext(), "term confirm clicked", Toast.LENGTH_SHORT).show();


                        EditText titleEditText = (EditText)findViewById(R.id.termName);
                        EditText startEditText = (EditText)findViewById(R.id.startDate);
                        EditText endEditText = (EditText)findViewById(R.id.endDate);

                        Term termToAdd = new Term(titleEditText.getText().toString(),
                                startEditText.toString(),
                                endEditText.getText().toString(),
                                false,
                                false,
                                true);
                        if (termToAdd.isValid()) {
                            DatabaseHelper databaseHelper;
                            databaseHelper = new DatabaseHelper(this);
                            long idReturned = databaseHelper.addTerm(termToAdd);
                            if (idReturned < 0) {
                                Toast.makeText(getApplicationContext(), "Something went wrong with the query.  Term not added.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Term Added.  ID: " + idReturned, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(this, MainActivity.class);
                                startActivity(intent);
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Validation failed.  Check your entries.", Toast.LENGTH_SHORT).show();

                        }
                        break;
                }
            }
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