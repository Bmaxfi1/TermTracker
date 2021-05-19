package com.example.termtracker.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.termtracker.Model.Term;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int VERSION = 4;
    private static final String TABLE_TERMS = "terms";
    private static final String TERMS_COL_ID = "_id";
    private static final String TERMS_COL_TITLE = "title";
    private static final String TERMS_COL_START = "startDate";
    private static final String TERMS_COL_END = "endDate";

    private static final String CREATE_TERMS_TABLE = "CREATE TABLE " + TABLE_TERMS + "(" +
            TERMS_COL_ID + " INTEGER PRIMARY KEY," + TERMS_COL_TITLE + " TEXT," + TERMS_COL_START +
            " TEXT," + TERMS_COL_END + " TEXT)";



    public DatabaseHelper(Context context) {
        //third arg is for CursorFactory instance
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TERMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_TERMS);
        onCreate(db);
    }

    public long addTerm(Term term) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TERMS_COL_TITLE, term.getTitle());
        values.put(TERMS_COL_START, term.getStartDate());
        values.put(TERMS_COL_END, term.getEndDate());

        long id = db.insert(TABLE_TERMS, null, values);
        return id;
    }
}
