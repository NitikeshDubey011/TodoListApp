package com.example.abc.todolistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Nitikesh Dubey on 12/29/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME="TaskDatabase";
    public static final String DB_TABLE="Task";
    public static final String DB_COLUMN="TaskName";
    public static final int DB_VER=1;


    public DbHelper(Context context) {
        super(context, DB_NAME,null,DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query=String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL);",DB_TABLE,DB_COLUMN);
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query=String.format("DELETE TABLE IF EXISTS %s",DB_TABLE);
        db.execSQL(query);
        onCreate(db);
    }

    public void insertNewTask(String task){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DB_COLUMN,task);
        sqLiteDatabase.insertWithOnConflict(DB_TABLE,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        sqLiteDatabase.close();
    }

    public void deleteTask(String task){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.delete(DB_TABLE,DB_COLUMN + " = ?",new String[]{task});
        sqLiteDatabase.close();
    }

    public ArrayList<String> getTaskList(){
        ArrayList<String> taskList=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.query(DB_TABLE,new String[]{DB_COLUMN},null,null,null,null,null);
        while (cursor.moveToNext()){
            int index=cursor.getColumnIndex(DB_COLUMN);
            taskList.add(cursor.getString(index));
        }
        cursor.close();
        sqLiteDatabase.close();
        return taskList;
    }
}
