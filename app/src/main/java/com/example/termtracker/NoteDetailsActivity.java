package com.example.termtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.termtracker.Data.DatabaseHelper;
import com.example.termtracker.Model.Note;

public class NoteDetailsActivity extends AppCompatActivity {

    Note note;

    EditText noteTitle;
    EditText noteContent;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        goBackToCourse();
    }

    @Override
    public boolean onSupportNavigateUp() {
        goBackToCourse();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);


        noteTitle = (EditText) findViewById(R.id.note_details_title);
        noteContent = (EditText) findViewById(R.id.note_details_contents);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar7);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras(); //get the key/value pairs
        if (bundle != null) {
            int value = bundle.getInt("noteId");

            DatabaseHelper helper = new DatabaseHelper(this);
            note = helper.getNoteById(value);
        }

        noteTitle.setText(note.getTitle());
        noteContent.setText(note.getContent());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_delete_or_share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.appbar_confirm) {
            Toast.makeText(getApplicationContext(), "confirm", Toast.LENGTH_SHORT).show();
            DatabaseHelper helper = new DatabaseHelper(this);

            Note noteToUpdate = new Note(
                    note.getId(),
                    noteTitle.getText().toString(),
                    noteContent.getText().toString(),
                    note.getCourseId()
            );


            helper.updateNote(noteToUpdate);
            Toast.makeText(this, "Note updated.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.appbar_delete) {
            Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
            DatabaseHelper helper = new DatabaseHelper(this);
            helper.deleteNote(note);
            Toast.makeText(this, "Note Deleted.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.appbar_share) {
            Toast.makeText(getApplicationContext(), "share", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.home) {
            goBackToCourse();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goBackToCourse() {
        Intent intent = new Intent();
        intent.putExtra("courseOfNote", note.getCourseId());
        setResult(RESULT_OK, intent);
        finish();
    }
}