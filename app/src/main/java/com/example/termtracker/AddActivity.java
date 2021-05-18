package com.example.termtracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void launchAddActivity(View view) {
        if (view == findViewById(R.id.add_note_button)) {
            Intent intent = new Intent(this, AddDetailsActivity.class);
            intent.putExtra("type", "note");
            startActivity(intent);
        } else if (view == findViewById(R.id.add_assessment_button)) {
            Intent intent = new Intent(this, AddDetailsActivity.class);
            intent.putExtra("type", "assessment");
            startActivity(intent);
        } else if (view == findViewById(R.id.add_course_button)) {
            Intent intent = new Intent(this, AddDetailsActivity.class);
            intent.putExtra("type", "course");
            startActivity(intent);
        } else if (view == findViewById(R.id.add_term_button)) {
            Intent intent = new Intent(this, AddDetailsActivity.class);
            intent.putExtra("type", "term");
            startActivity(intent);
        }
    }

}