package ca.cerroni.justdoit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Date;
import java.text.SimpleDateFormat;
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
        cv.put("snooze", 0);
        cv.put("claimed", 0);

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
        if(t.done != null){
            cv.put("done", t.done.getTime());
        } else
            cv.put("done", 0);
        if(t.snooze != null) {
            cv.put("snooze", t.snooze.getTime());
        } else
            cv.put("snooze", 0);

        if(t.claimed != null) {
            cv.put("claimed", t.claimed.getTime());
        } else
            cv.put("claimed", 0);

        wdb.update("tasks", cv, "id=?", new String[]{ ""+t.id });
    }

    public Task getTask(int id) {
        Task t = new Task();
        Cursor c = rdb.query("tasks", new String[]{"id","name","notes","startdate","enddate","freq","time","color","done","snooze","claimed"},
                "id=?", new String[]{""+id}, null, null, "id");
        if(c.moveToFirst()) {
            t.id = c.getInt(0);
            t.name = c.getString(1);
            t.notes = c.getString(2);
            t.startDate = Date.valueOf(c.getString(3));
            t.endDate = Date.valueOf(c.getString(4));
            t.freq = c.getInt(5);
            t.time = c.getString(6);
            t.color = c.getString(7);
            t.done = new Date(c.getLong(8));
            t.snooze = new Date(c.getLong(9));
            t.claimed = new Date(c.getLong(10));
        }
        return t;
    }

    public ArrayList<Task> getAllTasks(String sort) {
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor c = rdb.query("tasks", new String[]{"id","name","notes","startdate","enddate","freq","time","color","done","snooze","claimed"},
                null, null, null, null, sort);
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
                t.done = new Date(c.getLong(8));
                t.snooze = new Date(c.getLong(9));
                t.claimed = new Date(c.getLong(10));
                tasks.add(t);
                Log.d("database", "ttt: "+t.startDate.toString() + "/"+t.endDate.toString());
            }while(c.moveToNext());
        }

        Log.d("database", "MaxLen: "+tasks.size());

        return tasks;
    }

    public ArrayList<Task> getTasksByDate(Date date) {
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor c = rdb.query("tasks", new String[]{"name","notes","startdate","freq","time","color","done","enddate","snooze","claimed","id"},
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
                t.done = new Date(c.getLong(6));
                t.endDate = Date.valueOf(c.getString(7));
                t.snooze = new Date(c.getLong(8));
                t.claimed = new Date(c.getLong(9));
                t.id = c.getInt(10);
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
                t.done = new Date(c.getLong(8));
                tasks.add(t);
            }while(c.moveToNext());
        }
        return tasks;
    }
}
