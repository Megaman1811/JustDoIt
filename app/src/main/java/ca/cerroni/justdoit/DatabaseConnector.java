package ca.cerroni.justdoit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Date;
import java.util.ArrayList;

public class DatabaseConnector {
    private static final String DB_NAME = "doitDB";
    private SQLiteDatabase wdb;
    private SQLiteDatabase rdb;
    private DatabaseOpenHelper openHelper;

    public DatabaseConnector(Context c) {
        openHelper = new DatabaseOpenHelper(c, DB_NAME, null, 1);
    }

    public void open() throws SQLException {
        wdb = openHelper.getWritableDatabase();
        rdb = openHelper.getReadableDatabase();
    }

    public void close() throws SQLException {
        wdb.close();
        rdb.close();
    }

    public Task insert(Task t) {
        Log.d("insert", "INSERT: "+t.startDate.toString());
        ContentValues cv = new ContentValues();
        cv.put("name", t.name);
        cv.put("notes", t.notes);
        cv.put("startdate", t.startDate.toString());
        cv.put("enddate", t.endDate.toString());
        cv.put("time", t.time);
        cv.put("freq", t.freq);
        cv.put("color", t.color);
        cv.put("done", 0);
        cv.put("snooze", "");
        cv.put("claimed", "");

        wdb.insert("tasks", null, cv);
        return t;
    }

    public void clear() throws SQLException {
        wdb.delete("tasks", null, null);
    }

    public void del(int id) throws SQLException {
        wdb.delete("tasks", "id=?", new String[]{""+id});
    }

    public void update(Task t) {
        ContentValues cv = new ContentValues();
        cv.put("name", t.name);
        cv.put("notes", t.notes);
        cv.put("startdate", t.startDate.toString());
        cv.put("enddate", t.endDate.toString());
        cv.put("time", t.time);
        cv.put("freq", t.freq);
        cv.put("color", t.color);

        wdb.update("tasks", cv, "id=?", new String[]{ ""+t.id });
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor c = rdb.query("tasks", new String[]{"id","name","notes","startdate","enddate","freq","time","color","done"},
                null, null, null, null, "name");
        if(c.moveToFirst()) {
            do {
                Task t = new Task();
                t.id = c.getInt(0);
                t.name = c.getString(1);
                t.notes = c.getString(2);
                t.startDate = Date.valueOf(c.getString(3));
                t.endDate = Date.valueOf(c.getString(4));
                t.freq = c.getInt(5);
                t.time = c.getString(6);
                t.color = c.getString(7);
                t.done = c.getInt(8) >= 1;
                tasks.add(t);
                Log.d("database", "ttt: "+t.startDate.toString() + "/"+t.endDate.toString());
            }while(c.moveToNext());
        }

        Log.d("database", "MaxLen: "+tasks.size());

        return tasks;
    }

    public ArrayList<Task> getTasksByDate(Date date) {
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor c = rdb.query("tasks", new String[]{"name","notes","startdate","freq","time","color","done","enddate"},
                "date(?) between date(startdate) and date(enddate)", new String[]{ date.toString() }, null,
                null, "name");

        if(c.moveToFirst()) {
            do {
                Task t = new Task();
                t.name = c.getString(0);
                t.notes = c.getString(1);
                t.startDate = Date.valueOf(c.getString(2));
                t.freq = c.getInt(3);
                t.time = c.getString(4);
                t.color = c.getString(5);
                t.done = c.getInt(6) >= 1;
                t.endDate = Date.valueOf(c.getString(7));
                tasks.add(t);
            }while(c.moveToNext());
        }

        Log.d("database", "Check: "+date.toString()+" Len: "+tasks.size());

        return tasks;
    }

    public ArrayList<Task> searchTasksByName(String search) {
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor c = rdb.query("tasks", new String[]{"id","name","notes","startdate","enddate","freq","time","color","done"},
                "name like ? collate nocase", new String[]{ "%"+search+"%" }, null,
                null, "name");

        if(c.moveToFirst()) {
            do {
                Task t = new Task();
                t.id = c.getInt(0);
                t.name = c.getString(1);
                t.notes = c.getString(2);
                t.startDate = Date.valueOf(c.getString(3));
                t.endDate = Date.valueOf(c.getString(4));
                t.freq = c.getInt(5);
                t.time = c.getString(6);
                t.color = c.getString(7);
                t.done = c.getInt(8) >= 1;
                tasks.add(t);
            }while(c.moveToNext());
        }
        return tasks;
    }
}
