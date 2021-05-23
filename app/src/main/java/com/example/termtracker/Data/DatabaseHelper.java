package com.example.termtracker.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.termtracker.Model.Course;
import com.example.termtracker.Model.CourseInstructor;
import com.example.termtracker.Model.Term;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int VERSION = 11;

    private static final String TABLE_TERMS = "terms";
    private static final String TERMS_COL_ID = "_id";
    private static final String TERMS_COL_TITLE = "title";
    private static final String TERMS_COL_START = "startDate";
    private static final String TERMS_COL_END = "endDate";
    private static final String TERMS_COL_COMPLETED = "completed";
    private static final String TERMS_COL_DELETABLE = "deletable";

    private static final String TABLE_COURSES = "courses";
    private static final String COURSES_COL_ID = "_id";
    private static final String COURSES_COL_TITLE = "title";
    private static final String COURSES_COL_START = "startDate";
    private static final String COURSES_COL_END = "endDate";
    private static final String COURSES_COL_COMPLETED = "completed";
    private static final String COURSES_COL_DELETABLE = "deletable";
    private static final String COURSES_COL_TERM_ID = "term_id";

    private static final String TABLE_INSTRUCTORS = "instructors";
    private static final String INSTRUCTORS_COL_ID = "_id";
    private static final String INSTRUCTORS_COL_NAME = "name";
    private static final String INSTRUCTORS_COL_PHONE = "phone";
    private static final String INSTRUCTORS_COL_EMAIL = "email";
    private static final String INSTRUCTORS_COL_COURSE_ID = "course_id";

    private static final String TABLE_ASSESSMENTS = "assessments";
    private static final String ASSESSMENTS_COL_ID = "_id";
    private static final String ASSESSMENTS_COL_TITLE = "title";
    private static final String ASSESSMENTS_COL_TYPE = "type";
    private static final String ASSESSMENTS_COL_START = "startDate";
    private static final String ASSESSMENTS_COL_END = "endDate";
    private static final String ASSESSMENTS_COL_COMPLETED = "completed";
    private static final String ASSESSMENTS_COL_DELETABLE = "deletable";
    private static final String ASSESSMENTS_COL_COURSE_ID = "course_id";

    private static final String TABLE_NOTES = "notes";
    private static final String NOTES_COL_ID = "_id";
    private static final String NOTES_COL_TITLE = "title";
    private static final String NOTES_COL_CONTENT = "content";
    private static final String NOTES_COL_CREATE_DATE = "create_date";
    private static final String NOTES_COL_COURSE_ID = "course_id";


    public DatabaseHelper(Context context) {
        //third arg is for CursorFactory instance
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TERMS_TABLE = "CREATE TABLE " + TABLE_TERMS + "(" +
                TERMS_COL_ID + " INTEGER PRIMARY KEY," + TERMS_COL_TITLE + " TEXT," + TERMS_COL_START +
                " TEXT," + TERMS_COL_END + " TEXT," + TERMS_COL_COMPLETED + " TEXT," +
                TERMS_COL_DELETABLE + " TEXT)";

        String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_COURSES + "(" + COURSES_COL_ID + " INTEGER PRIMARY KEY," +
                COURSES_COL_TITLE + " TEXT," + COURSES_COL_START + " TEXT," +
                COURSES_COL_END + " TEXT," + COURSES_COL_COMPLETED + " TEXT," + COURSES_COL_DELETABLE + " TEXT," +
                COURSES_COL_TERM_ID + " INTEGER," +
                " FOREIGN KEY (" + COURSES_COL_TERM_ID +") REFERENCES " + TABLE_TERMS + " (" + TERMS_COL_ID + ") )";

        String CREATE_INSTRUCTORS_TABLE = "CREATE TABLE " + TABLE_INSTRUCTORS + "(" + INSTRUCTORS_COL_ID + " INTEGER PRIMARY KEY," +
                INSTRUCTORS_COL_NAME + " TEXT," + INSTRUCTORS_COL_PHONE + " TEXT," + INSTRUCTORS_COL_EMAIL +
                " TEXT," + INSTRUCTORS_COL_COURSE_ID + " INTEGER," +
                " FOREIGN KEY (" + INSTRUCTORS_COL_COURSE_ID + ") REFERENCES " + TABLE_COURSES + " (" + COURSES_COL_ID + ") )";

        String CREATE_ASSESSMENTS_TABLE = "CREATE TABLE " + TABLE_ASSESSMENTS + "(" + ASSESSMENTS_COL_ID + " INTEGER PRIMARY KEY," +
                ASSESSMENTS_COL_TITLE + " TEXT," + ASSESSMENTS_COL_TYPE + " TEXT," + ASSESSMENTS_COL_START + " TEXT," + ASSESSMENTS_COL_END +
                " TEXT," + ASSESSMENTS_COL_COMPLETED + " TEXT," + ASSESSMENTS_COL_DELETABLE + " TEXT," + ASSESSMENTS_COL_COURSE_ID + " INTEGER," +
                " FOREIGN KEY (" + ASSESSMENTS_COL_COURSE_ID + ") REFERENCES " + TABLE_COURSES + " (" + COURSES_COL_ID + ") )";

        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "(" + NOTES_COL_ID + " INTEGER PRIMARY KEY," +
                NOTES_COL_TITLE + " TEXT," + NOTES_COL_CONTENT + " TEXT," + NOTES_COL_CREATE_DATE + " TEXT," +
                NOTES_COL_COURSE_ID + " INTEGER," + "FOREIGN KEY (" + NOTES_COL_COURSE_ID + ") REFERENCES " + TABLE_COURSES + " (" + COURSES_COL_ID + ") )";

        db.execSQL(CREATE_TERMS_TABLE);
        db.execSQL(CREATE_COURSES_TABLE);
        db.execSQL(CREATE_INSTRUCTORS_TABLE);
        db.execSQL(CREATE_ASSESSMENTS_TABLE);
        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_TERMS);
        db.execSQL("drop table if exists " + TABLE_COURSES);
        db.execSQL("drop table if exists " + TABLE_INSTRUCTORS);
        db.execSQL("drop table if exists " + TABLE_ASSESSMENTS);
        db.execSQL("drop table if exists " + TABLE_NOTES);


        onCreate(db);
    }

    public long addTerm(Term term) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TERMS_COL_TITLE, term.getTitle());
        values.put(TERMS_COL_START, term.getStartDate());
        values.put(TERMS_COL_END, term.getEndDate());
        values.put(TERMS_COL_COMPLETED, term.isCompleted());
        values.put(TERMS_COL_DELETABLE, term.isDeletable());

        long id = db.insert(TABLE_TERMS, null, values);
        return id;
    }

    public List<Term> getAllTerms() {
        List<Term> allTerms = new ArrayList<Term>();

        String selectQuery = "SELECT * FROM " + TABLE_TERMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Term term = new Term(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        Boolean.getBoolean(cursor.getString(4)),
                        Boolean.getBoolean(cursor.getString(5))

                );
                allTerms.add(term);
            } while (cursor.moveToNext());
        }
        return allTerms;
    }

    public Term getTermById(int id) {
        Term term;

        String selectQuery = "SELECT * FROM " + TABLE_TERMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                term = new Term(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        Boolean.getBoolean(cursor.getString(4)),
                        Boolean.getBoolean(cursor.getString(5))
                );
                if (term.getId() == id) {
                    return term;
                }
            } while (cursor.moveToNext());
        }
        Log.d("superdopetag", "null object on getTermById");
        return null;
    }

    public Term getTermByName(String title) {
        Term term;

        String selectQuery = "SELECT * FROM " + TABLE_TERMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                term = new Term(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        Boolean.getBoolean(cursor.getString(4)),
                        Boolean.getBoolean(cursor.getString(5))
                );
                if (term.getTitle().equals(title)) {
                    return term;
                }
            } while (cursor.moveToNext());
        }
        Log.d("superdopetag", "null object on getTermByName");
        return null;
    }


    public List<Course> getAllCourses() {
        List<Course> allCourses = new ArrayList<Course>();

        String selectQuery = "SELECT * FROM " + TABLE_COURSES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Course course = new Course(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        Boolean.getBoolean(cursor.getString(4)),
                        Boolean.getBoolean(cursor.getString(5)),
                        Integer.parseInt(cursor.getString(6))
                );
                allCourses.add(course);
            } while (cursor.moveToNext());
        }
        return allCourses;

    }

    public long addCourse(Course course) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COURSES_COL_TITLE, course.getTitle());
        values.put(COURSES_COL_START, course.getStartDate());
        values.put(COURSES_COL_END, course.getEndDate());
        values.put(COURSES_COL_COMPLETED, course.isCompleted());
        values.put(COURSES_COL_DELETABLE, course.isDeletable());
        values.put(COURSES_COL_TERM_ID, course.getTermId());

        long id = db.insert(TABLE_COURSES, null, values);
        return id;
    }

    public long addInstructor(CourseInstructor instructor) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(INSTRUCTORS_COL_NAME, instructor.getName());
        values.put(INSTRUCTORS_COL_PHONE, instructor.getPhone());
        values.put(INSTRUCTORS_COL_EMAIL, instructor.getEmail());
        values.put(INSTRUCTORS_COL_COURSE_ID, instructor.getCourseId());

        long id = db.insert(TABLE_INSTRUCTORS, null, values);
        return id;
    }

}
