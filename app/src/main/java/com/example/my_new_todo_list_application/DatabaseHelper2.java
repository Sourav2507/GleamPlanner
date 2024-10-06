package com.example.my_new_todo_list_application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper2 extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TodoList.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TODOS = "todos";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_CATEGORY = "category";

    public DatabaseHelper2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_CATEGORY + " TEXT" + ")";
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
        onCreate(db);
    }

    public long addTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, todo.getTitle());
        values.put(COLUMN_TIME, todo.getTime());
        values.put(COLUMN_CATEGORY, todo.getCategory());
        long id = db.insert(TABLE_TODOS, null, values);
        db.close();
        return id;
    }

    public List<Todo> getAllTodos() {
        List<Todo> todoList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TODOS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Todo todo = new Todo();
                todo.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                todo.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                todo.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_TIME)));
                todo.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
                todoList.add(todo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return todoList;
    }

    public void deleteTodo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODOS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAllTodos() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TODOS); // Replace TABLE_TODOS with your actual table name
        db.close();
    }

}
