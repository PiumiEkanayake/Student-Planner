package com.example.timetable.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DBHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "StudentPlanner.db";


    private static final String SQL_CREATE_COURSE_ENTRIES =
            "CREATE TABLE " + CourseMaster.Courses.TABLE_NAME + "("+
                    CourseMaster.Courses._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    CourseMaster.Courses.COLUMN_NAME_COURSE_NAME + " TEXT," +
                    CourseMaster.Courses.COLUMN_NAME_INSTITUTE + " TEXT," +
                    CourseMaster.Courses.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    CourseMaster.Courses.COLUMN_NAME_COLOUR + " INTEGER," +
                    CourseMaster.Courses.COLUMN_NAME_START + " TEXT," +
                    CourseMaster.Courses.COLUMN_NAME_END + " TEXT)" ;

    private static final String SQL_CREATE_CLASS_ENTRIES =
            "CREATE TABLE " + ClassMaster.Classes.TABLE_NAME_CLASS + "( "+
                    ClassMaster.Classes._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    ClassMaster.Classes.COLUMN_NAME_CLASS_NAME + " TEXT," +
                    ClassMaster.Classes.COLUMN_NAME_COURSE + " TEXT," +
                    ClassMaster.Classes.COLUMN_NAME_SUBJECT + " TEXT," +
                    ClassMaster.Classes.COLUMN_NAME_CLASS_TYPE + " INTEGER," +
                    ClassMaster.Classes.COLUMN_NAME_CLASS_TEACHER + " TEXT," +
                    ClassMaster.Classes.COLUMN_NAME_CLASS_ROOM + " TEXT," +
                    ClassMaster.Classes.COLUMN_NAME_NOTE + " TEXT," +
                    ClassMaster.Classes.COLUMN_NAME_COLOR + " INTEGER," +
                    ClassMaster.Classes.COLUMN_NAME_FREQUENCY + " TEXT," +
                    ClassMaster.Classes.COLUMN_NAME_DAY + " TEXT," +
                    ClassMaster.Classes.COLUMN_NAME_START_TIME + " TEXT," +
                    ClassMaster.Classes.COLUMN_NAME_END_TIME + " TEXT," +
                    ClassMaster.Classes.COLUMN_NAME_START_DATE + " TEXT," +
                    ClassMaster.Classes.COLUMN_NAME_END_DATE + " TEXT," +
                    ClassMaster.Classes.COLUMN_NAME_REMINDER + " INTEGER )" ;

    private static final String SQL_CREATE_SUBJECT_ENTRIES =
            "CREATE TABLE " + SubjectMaster.Subjects.TABLE_NAME + "("+
                    SubjectMaster.Subjects._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    SubjectMaster.Subjects.COLUMN_NAME_SUBJECT_NAME + " TEXT," +
                    SubjectMaster.Subjects.COLUMN_NAME_TEACHER_NAME + " TEXT," +
                    SubjectMaster.Subjects.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    SubjectMaster.Subjects.COLUMN_NAME_COLOUR + " INTEGER )";

    private static final String SQL_CREATE_STUDY_ENTRIES =
            "CREATE TABLE " + StudyMaster.Studies.TABLE_NAME + "("+
                    StudyMaster.Studies._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    StudyMaster.Studies.COLUMN_NAME_STUDY_TITLE + " TEXT," +
                    StudyMaster.Studies.COLUMN_NAME_SUBJECT + " INTEGER," +
                    StudyMaster.Studies.COLUMN_NAME_COLOUR + " INTEGER," +
                    StudyMaster.Studies.COLUMN_NAME_DATE + " TEXT," +
                    StudyMaster.Studies.COLUMN_NAME_START + " TEXT," +
                    StudyMaster.Studies.COLUMN_NAME_END + " TEXT," +
                    StudyMaster.Studies.COLUMN_NAME_REPEAT + " TEXT," +
                    StudyMaster.Studies.COLUMN_NAME_DAY + " TEXT," +
                    StudyMaster.Studies.COLUMN_NAME_NOTE + " TEXT," +
                    StudyMaster.Studies.COLUMN_NAME_REMINDER + " INTEGER," +
                    StudyMaster.Studies.COLUMN_NAME_REMINDER_TIME + " TEXT," +
                    "FOREIGN KEY(" + StudyMaster.Studies.COLUMN_NAME_SUBJECT + ") " +
                    "REFERENCES " + SubjectMaster.Subjects.TABLE_NAME + "(" + SubjectMaster.Subjects._ID + ") " +
                    "ON DELETE CASCADE)";

    public DBHandler(@Nullable Context context) {

        super(context, DATABASE_NAME, null, 3);
        SQLiteDatabase db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
                db.execSQL(SQL_CREATE_COURSE_ENTRIES);
                db.execSQL(SQL_CREATE_CLASS_ENTRIES);
                db.execSQL(SQL_CREATE_SUBJECT_ENTRIES);
                db.execSQL(SQL_CREATE_STUDY_ENTRIES);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + CourseMaster.Courses.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ClassMaster.Classes.TABLE_NAME_CLASS);
        db.execSQL("DROP TABLE IF EXISTS " + SubjectMaster.Subjects.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StudyMaster.Studies.TABLE_NAME);
        onCreate(db);
    }
    public void create(){
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
    }

//    CRUD operations for Course

    public boolean  addCourse(String name, String institute, String description, Integer colour, String start, String end ){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CourseMaster.Courses.COLUMN_NAME_COURSE_NAME,name);
        values.put(CourseMaster.Courses.COLUMN_NAME_DESCRIPTION,description);
        values.put(CourseMaster.Courses.COLUMN_NAME_COLOUR,colour);
        values.put(CourseMaster.Courses.COLUMN_NAME_INSTITUTE,institute);
        values.put(CourseMaster.Courses.COLUMN_NAME_START,start);
        values.put(CourseMaster.Courses.COLUMN_NAME_END,end);
        long result = db.insert(CourseMaster.Courses.TABLE_NAME,null,values);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean  updateCourse(String id,String name, String institute, String description, Integer colour, String start, String end ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CourseMaster.Courses.COLUMN_NAME_COURSE_NAME,name);
        values.put(CourseMaster.Courses.COLUMN_NAME_DESCRIPTION,description);
        values.put(CourseMaster.Courses.COLUMN_NAME_COLOUR,colour);
        values.put(CourseMaster.Courses.COLUMN_NAME_INSTITUTE,institute);
        values.put(CourseMaster.Courses.COLUMN_NAME_START,start);
        values.put(CourseMaster.Courses.COLUMN_NAME_END,end);
        db.update(CourseMaster.Courses.TABLE_NAME,values,"_id = ?",new String[]{id});
        return true;
    }
    public Cursor getAllCourse(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("Select * from "+ CourseMaster.Courses.TABLE_NAME,null);
        return res;
    }
    public Cursor getSingleCourse(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("Select * from " + CourseMaster.Courses.TABLE_NAME + " WHERE "+ CourseMaster.Courses._ID + " = " + id, null);
        return res;
    }
    public Integer deleteCourse(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CourseMaster.Courses.TABLE_NAME, " _id = ? ",new String[]{id});
    }

    //CRUD Operations for CLass
    public boolean addClass(String name, String course, String subject, String classType, String teacher, String classroom,String note, Integer colour,String freq, String day, String startTime,String endTime, String sDate,String eDate,Integer reminder ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ClassMaster.Classes.COLUMN_NAME_CLASS_NAME,name);
        values.put(ClassMaster.Classes.COLUMN_NAME_COURSE,course);
        values.put(ClassMaster.Classes.COLUMN_NAME_SUBJECT,subject);
        values.put(ClassMaster.Classes.COLUMN_NAME_CLASS_TYPE,classType);
        values.put(ClassMaster.Classes.COLUMN_NAME_CLASS_TEACHER,teacher);
        values.put(ClassMaster.Classes.COLUMN_NAME_CLASS_ROOM,classroom);
        values.put(ClassMaster.Classes.COLUMN_NAME_NOTE,note);
        values.put(ClassMaster.Classes.COLUMN_NAME_COLOR,colour);
        values.put(ClassMaster.Classes.COLUMN_NAME_FREQUENCY,freq);
        values.put(ClassMaster.Classes.COLUMN_NAME_DAY,day);
        values.put(ClassMaster.Classes.COLUMN_NAME_START_TIME,startTime);
        values.put(ClassMaster.Classes.COLUMN_NAME_END_TIME,endTime);
        values.put(ClassMaster.Classes.COLUMN_NAME_START_DATE,sDate);
        values.put(ClassMaster.Classes.COLUMN_NAME_END_DATE,eDate);
        values.put(ClassMaster.Classes.COLUMN_NAME_REMINDER,reminder);

        long result = db.insert(ClassMaster.Classes.TABLE_NAME_CLASS,null,values);
        if(result == -1)
            return false;
        else
            return true;
    }

    // CRUD operations for Subject

    public boolean addSubject(String subjectName, String teacherName, String subjectDesc, Integer colour) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SubjectMaster.Subjects.COLUMN_NAME_SUBJECT_NAME, subjectName);
        values.put(SubjectMaster.Subjects.COLUMN_NAME_TEACHER_NAME, teacherName);
        values.put(SubjectMaster.Subjects.COLUMN_NAME_DESCRIPTION, subjectDesc);
        values.put(SubjectMaster.Subjects.COLUMN_NAME_COLOUR, colour);

        long result = db.insert(SubjectMaster.Subjects.TABLE_NAME, null, values);

        return result != -1;
    }

    public boolean updateSubject(String subjectId, String subjectName, String teacherName, String subjectDesc, Integer colour) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SubjectMaster.Subjects.COLUMN_NAME_SUBJECT_NAME, subjectName);
        values.put(SubjectMaster.Subjects.COLUMN_NAME_TEACHER_NAME, teacherName);
        values.put(SubjectMaster.Subjects.COLUMN_NAME_DESCRIPTION, subjectDesc);
        values.put(SubjectMaster.Subjects.COLUMN_NAME_COLOUR, colour);

        db.update(SubjectMaster.Subjects.TABLE_NAME, values, "_id = ?", new String[]{subjectId});

        return true;
    }

    public Cursor getAllSubjects() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM "+ SubjectMaster.Subjects.TABLE_NAME, null);
    }

    public Cursor getSingleSubject(int subjectId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + SubjectMaster.Subjects.TABLE_NAME + " WHERE "+
                SubjectMaster.Subjects._ID + " = " + subjectId, null);
    }

    public Integer deleteSubject(String subjectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SubjectMaster.Subjects.TABLE_NAME, " _id = ? ", new String[]{subjectId});
    }

    // CRUD operations for Study

    public boolean addStudy(String studyTitle, Integer subject, Integer colour, String date, String startTime,
                            String endTime, String repeat, String day, String note, Boolean reminderBool,
                            String reminderTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        int reminder = reminderBool ? 1 : 0;  // Convert reminder toggle value from boolean to integer
        reminderTime = reminderBool ? reminderTime : null;  // Set reminder time to null if reminder is not enabled

        values.put(StudyMaster.Studies.COLUMN_NAME_STUDY_TITLE, studyTitle);
        values.put(StudyMaster.Studies.COLUMN_NAME_SUBJECT, subject);
        values.put(StudyMaster.Studies.COLUMN_NAME_COLOUR, colour);
        values.put(StudyMaster.Studies.COLUMN_NAME_DATE, date);
        values.put(StudyMaster.Studies.COLUMN_NAME_START, startTime);
        values.put(StudyMaster.Studies.COLUMN_NAME_END, endTime);
        values.put(StudyMaster.Studies.COLUMN_NAME_REPEAT, repeat);
        values.put(StudyMaster.Studies.COLUMN_NAME_DAY, day);
        values.put(StudyMaster.Studies.COLUMN_NAME_NOTE, note);
        values.put(StudyMaster.Studies.COLUMN_NAME_REMINDER, reminder);
        values.put(StudyMaster.Studies.COLUMN_NAME_REMINDER_TIME, reminderTime);

        long result = db.insert(StudyMaster.Studies.TABLE_NAME, null, values);

        return result != -1;
    }

    public boolean updateStudy(String studyId, String studyTitle, Integer subject, Integer colour, String date,
                               String startTime, String endTime, String repeat, String day, String note,
                               Boolean reminderBool, String reminderTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        int reminder = reminderBool ? 1 : 0;  // Convert reminder toggle value from boolean to integer
        reminderTime = reminderBool ? reminderTime : null;  // Set reminder time to null if reminder is not enabled

        values.put(StudyMaster.Studies.COLUMN_NAME_STUDY_TITLE, studyTitle);
        values.put(StudyMaster.Studies.COLUMN_NAME_SUBJECT, subject);
        values.put(StudyMaster.Studies.COLUMN_NAME_COLOUR, colour);
        values.put(StudyMaster.Studies.COLUMN_NAME_DATE, date);
        values.put(StudyMaster.Studies.COLUMN_NAME_START, startTime);
        values.put(StudyMaster.Studies.COLUMN_NAME_END, endTime);
        values.put(StudyMaster.Studies.COLUMN_NAME_REPEAT, repeat);
        values.put(StudyMaster.Studies.COLUMN_NAME_DAY, day);
        values.put(StudyMaster.Studies.COLUMN_NAME_NOTE, note);
        values.put(StudyMaster.Studies.COLUMN_NAME_REMINDER, reminder);
        values.put(StudyMaster.Studies.COLUMN_NAME_REMINDER_TIME, reminderTime);

        db.update(StudyMaster.Studies.TABLE_NAME, values, "_id = ?", new String[]{studyId});

        return true;
    }

    public Cursor getAllStudies() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM "+ StudyMaster.Studies.TABLE_NAME, null);
    }

    public Cursor getSingleStudy(int studyId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + StudyMaster.Studies.TABLE_NAME + " WHERE " +
                StudyMaster.Studies._ID + " = " + studyId, null);
    }

    public int getLatestStudyId() {
        int latestStudyId = 0;

        SQLiteDatabase db = getReadableDatabase();
        Cursor latestStudy = db.rawQuery("SELECT * FROM " + StudyMaster.Studies.TABLE_NAME + " ORDER BY " +
                StudyMaster.Studies._ID + " DESC LIMIT 1", null);

        if (latestStudy.moveToFirst()) {
            latestStudyId = latestStudy.getInt(0);
            latestStudy.close();
        }

        return latestStudyId;
    }

    public Integer deleteStudy(String studyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(StudyMaster.Studies.TABLE_NAME, " _id = ? ", new String[]{studyId});
    }
}
