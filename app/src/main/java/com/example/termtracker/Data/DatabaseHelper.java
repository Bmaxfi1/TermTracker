package com.example.termtracker.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.termtracker.Model.Assessment;
import com.example.termtracker.Model.AssessmentType;
import com.example.termtracker.Model.Course;
import com.example.termtracker.Model.CourseInstructor;
import com.example.termtracker.Model.Note;
import com.example.termtracker.Model.Term;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int VERSION = 12;

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
                " FOREIGN KEY (" + COURSES_COL_TERM_ID + ") REFERENCES " + TABLE_TERMS + " (" + TERMS_COL_ID + ") )";

        String CREATE_INSTRUCTORS_TABLE = "CREATE TABLE " + TABLE_INSTRUCTORS + "(" + INSTRUCTORS_COL_ID + " INTEGER PRIMARY KEY," +
                INSTRUCTORS_COL_NAME + " TEXT," + INSTRUCTORS_COL_PHONE + " TEXT," + INSTRUCTORS_COL_EMAIL +
                " TEXT," + INSTRUCTORS_COL_COURSE_ID + " INTEGER," +
                " FOREIGN KEY (" + INSTRUCTORS_COL_COURSE_ID + ") REFERENCES " + TABLE_COURSES + " (" + COURSES_COL_ID + ") )";

        String CREATE_ASSESSMENTS_TABLE = "CREATE TABLE " + TABLE_ASSESSMENTS + "(" + ASSESSMENTS_COL_ID + " INTEGER PRIMARY KEY," +
                ASSESSMENTS_COL_TITLE + " TEXT," + ASSESSMENTS_COL_TYPE + " TEXT," + ASSESSMENTS_COL_START + " TEXT," + ASSESSMENTS_COL_END +
                " TEXT," + ASSESSMENTS_COL_COMPLETED + " TEXT," + ASSESSMENTS_COL_DELETABLE + " TEXT," + ASSESSMENTS_COL_COURSE_ID + " INTEGER," +
                " FOREIGN KEY (" + ASSESSMENTS_COL_COURSE_ID + ") REFERENCES " + TABLE_COURSES + " (" + COURSES_COL_ID + ") )";

        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "(" + NOTES_COL_ID + " INTEGER PRIMARY KEY," +
                NOTES_COL_TITLE + " TEXT," + NOTES_COL_CONTENT + " TEXT," +
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
                        sqliteBoolToJavaBool(cursor.getString(4)),
                        sqliteBoolToJavaBool(cursor.getString(5))

                );
                allTerms.add(term);
            } while (cursor.moveToNext());
        }
        return allTerms;
    }

    public Term getTermById(long id) {
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
                        sqliteBoolToJavaBool(cursor.getString(4)),
                        sqliteBoolToJavaBool(cursor.getString(5))
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
                        sqliteBoolToJavaBool(cursor.getString(4)),
                        sqliteBoolToJavaBool(cursor.getString(5))
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
                        sqliteBoolToJavaBool(cursor.getString(4)),
                        sqliteBoolToJavaBool(cursor.getString(5)),
                        Integer.parseInt(cursor.getString(6))
                );
                allCourses.add(course);
            } while (cursor.moveToNext());
        }
        return allCourses;
    }

    public Course getCourseById(int id) {
        Course course;

        String selectQuery = "SELECT * FROM " + TABLE_COURSES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                course = new Course(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        sqliteBoolToJavaBool(cursor.getString(4)),
                        sqliteBoolToJavaBool(cursor.getString(5)),
                        Integer.parseInt(cursor.getString(6))
                );
                if (course.getId() == id) {
                    return course;
                }
            } while (cursor.moveToNext());
        }
        Log.d("superdopetag", "null object on getCourseById");
        return null;
    }

    public Course getCourseByName(String title) {
        Course course;

        String selectQuery = "SELECT * FROM " + TABLE_COURSES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                course = new Course(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        sqliteBoolToJavaBool(cursor.getString(4)),
                        sqliteBoolToJavaBool(cursor.getString(5)),
                        Integer.parseInt(cursor.getString(6))
                );
                if (course.getTitle().equals(title)) {
                    return course;
                }
            } while (cursor.moveToNext());
        }
        Log.d("superdopetag", "null object on getCourseByName");
        return null;

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

    public long addNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTES_COL_TITLE, note.getTitle());
        values.put(NOTES_COL_CONTENT, note.getContent());
        values.put(NOTES_COL_COURSE_ID, note.getCourseId());

        long id = db.insert(TABLE_NOTES, null, values);
        return id;
    }

    public List<Assessment> getAllAssessments() {
        List<Assessment> allAssessments = new ArrayList<Assessment>();

        String selectQuery = "SELECT * FROM " + TABLE_ASSESSMENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Assessment assessment = new Assessment(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        AssessmentType.get(cursor.getString(2)),
                        cursor.getString(3),
                        cursor.getString(4),
                        sqliteBoolToJavaBool(cursor.getString(5)),
                        sqliteBoolToJavaBool(cursor.getString(6)),
                        Integer.parseInt(cursor.getString(7))
                );
                allAssessments.add(assessment);
            } while (cursor.moveToNext());
        }
        return allAssessments;
    }

    public Assessment getAssessmentById(int id) {
        Assessment assessment;

        String selectQuery = "SELECT * FROM " + TABLE_ASSESSMENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                assessment = new Assessment(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        AssessmentType.get(cursor.getString(2)),
                        cursor.getString(3),
                        cursor.getString(4),
                        sqliteBoolToJavaBool(cursor.getString(5)),
                        sqliteBoolToJavaBool(cursor.getString(6)),
                        Integer.parseInt(cursor.getString(7))
                );
                if (assessment.getId() == id) {
                    return assessment;
                }
            } while (cursor.moveToNext());
        }
        Log.d("superdopetag", "null object on getAssessmentById");
        return null;

    }

    public long addAssessment(Assessment assessment) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ASSESSMENTS_COL_TITLE, assessment.getTitle());
        values.put(ASSESSMENTS_COL_TYPE, assessment.getAssessmentType().toString());
        values.put(ASSESSMENTS_COL_START, assessment.getStartDate());
        values.put(ASSESSMENTS_COL_END, assessment.getEndDate());
        values.put(ASSESSMENTS_COL_COMPLETED, assessment.isCompleted());
        values.put(ASSESSMENTS_COL_DELETABLE, assessment.isDeletable());
        values.put(ASSESSMENTS_COL_COURSE_ID, assessment.getCourseId());

        long id = db.insert(TABLE_ASSESSMENTS, null, values);
        return id;
    }

    public long updateAssessment(Assessment assessment) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ASSESSMENTS_COL_TITLE, assessment.getTitle());
        values.put(ASSESSMENTS_COL_TYPE, assessment.getAssessmentType().toString());
        values.put(ASSESSMENTS_COL_START, assessment.getStartDate());
        values.put(ASSESSMENTS_COL_END, assessment.getEndDate());
        values.put(ASSESSMENTS_COL_COMPLETED, assessment.isCompleted());
        values.put(ASSESSMENTS_COL_DELETABLE, assessment.isDeletable());
        values.put(ASSESSMENTS_COL_COURSE_ID, assessment.getCourseId());

        db.update(TABLE_ASSESSMENTS, values, "_id = ?", new String[]{String.valueOf(assessment.getId())});

        Course course = getCourseById((int) assessment.getCourseId());
        checkAndUpdateCourseCompleteStatus(course);

        return assessment.getId();
    }

    public long updateTerm(Term term) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TERMS_COL_TITLE, term.getTitle());
        values.put(TERMS_COL_START, term.getStartDate());
        values.put(TERMS_COL_END, term.getEndDate());
        values.put(TERMS_COL_COMPLETED, term.isCompleted());
        values.put(TERMS_COL_DELETABLE, term.isDeletable());

        db.update(TABLE_TERMS, values, "_id = ?", new String[]{String.valueOf(term.getId())});
        return term.getId();
    }

    public long updateCourse(Course course) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COURSES_COL_TITLE, course.getTitle());
        values.put(COURSES_COL_START, course.getStartDate());
        values.put(COURSES_COL_END, course.getEndDate());
        values.put(COURSES_COL_COMPLETED, course.isCompleted());
        values.put(COURSES_COL_DELETABLE, course.isDeletable());
        values.put(COURSES_COL_TERM_ID, course.getTermId());

        db.update(TABLE_COURSES, values, "_id = ?", new String[]{String.valueOf(course.getId())});

        checkAndUpdateTermCompleteStatus(getTermById(course.getTermId()));

        return course.getId();
    }

    public long updateNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTES_COL_TITLE, note.getTitle());
        values.put(NOTES_COL_CONTENT, note.getContent());

        db.update(TABLE_NOTES, values, "_id = ?", new String[]{String.valueOf(note.getId())});

        return note.getId();
    }

    public long deleteNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_NOTES, "_id = ?", new String[]{String.valueOf(note.getId())});

        return note.getId();
    }

    public long updateInstructor(CourseInstructor instructor) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(INSTRUCTORS_COL_NAME, instructor.getName());
        values.put(INSTRUCTORS_COL_PHONE, instructor.getPhone());
        values.put(INSTRUCTORS_COL_EMAIL, instructor.getEmail());

        db.update(TABLE_INSTRUCTORS, values, "_id = ?", new String[]{String.valueOf(instructor.getId())});

        return instructor.getId();
    }

    public long deleteAssessment(Assessment assessment) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_ASSESSMENTS, "_id = ?", new String[]{String.valueOf(assessment.getId())});

        Course course = getCourseById((int) assessment.getCourseId());
        checkAndUpdateCourseCompleteStatus(course);

        return assessment.getId();
    }

    public long deleteCourse(Course course) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_INSTRUCTORS, "course_id = ?", new String[]{String.valueOf(course.getId())});
        db.delete(TABLE_NOTES, "course_id = ?", new String[]{String.valueOf(course.getId())});
        db.delete(TABLE_ASSESSMENTS, "course_id = ?", new String[]{String.valueOf(course.getId())});
        db.delete(TABLE_COURSES, "_id = ?", new String[]{String.valueOf(course.getId())});

        checkAndUpdateTermCompleteStatus(getTermById(course.getTermId()));

        return course.getId();
    }

    public long deleteTerm(Term term) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_TERMS, "_id = ?", new String[]{String.valueOf(term.getId())});

        return term.getId();
    }

    public long deleteInstructor(CourseInstructor instructor) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_INSTRUCTORS, "_id = ?", new String[]{String.valueOf(instructor.getId())});

        return instructor.getId();
    }

    public List<CourseInstructor> getAllInstructors() {
        List<CourseInstructor> allInstructors = new ArrayList<CourseInstructor>();

        String selectQuery = "SELECT * FROM " + TABLE_INSTRUCTORS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CourseInstructor instructor = new CourseInstructor(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        Integer.parseInt(cursor.getString(4))

                );
                allInstructors.add(instructor);
            } while (cursor.moveToNext());
        }
        return allInstructors;
    }

    public List<Note> getAllNotes() {
        List<Note> allNotes = new ArrayList<Note>();

        String selectQuery = "SELECT * FROM " + TABLE_NOTES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        Integer.parseInt(cursor.getString(3))

                );
                allNotes.add(note);
            } while (cursor.moveToNext());
        }
        return allNotes;
    }

    public Note getNoteById(int id) {
        Note note;

        String selectQuery = "SELECT * FROM " + TABLE_NOTES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                note = new Note(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        Integer.parseInt(cursor.getString(3))
                );
                if (note.getId() == id) {
                    return note;
                }
            } while (cursor.moveToNext());
        }
        Log.d("superdopetag", "null object on getNoteById");
        return null;

    }



    public Boolean sqliteBoolToJavaBool(String i) {
        if (i.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    public void checkAndUpdateCourseCompleteStatus(Course course) {
        List<Assessment> allAssessments = getAllAssessments();
        List<Assessment> allAssessmentsForThisCourse = new ArrayList<>();
        for (Assessment assessment: allAssessments) {
            if (assessment.getCourseId() == course.getId()) {
                allAssessmentsForThisCourse.add(assessment);
            }
        }

        boolean courseComplete = true;

        if (allAssessmentsForThisCourse.isEmpty()) {
            courseComplete = false;
        } else {
            for (Assessment assessment : allAssessmentsForThisCourse) {
                if (!assessment.isCompleted()) {
                    courseComplete = false;
                    break;
                }
            }
        }

        course.setCompleted(courseComplete);
        updateCourse(course);
    }

    public void checkAndUpdateTermCompleteStatus(Term term) {
        List<Course> allCourses = getAllCourses();
        List<Course> allCoursesForThisTerm = new ArrayList<>();
        for (Course course: allCourses) {
            if (course.getTermId() == term.getId()) {
                allCoursesForThisTerm.add(course);
            }
        }

        boolean termComplete = true;

        if (allCoursesForThisTerm.isEmpty()) {
            termComplete = false;
        } else {
            for (Course course : allCoursesForThisTerm) {
                if (!course.isCompleted()) {
                    termComplete = false;
                    break;
                }
            }
        }

        term.setCompleted(termComplete);
        updateTerm(term);
    }

}
