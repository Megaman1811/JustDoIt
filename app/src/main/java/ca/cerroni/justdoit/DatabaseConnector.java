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
        cv.put("done", "");
        if(t.snooze != null) {
            SimpleDateFormat time = new SimpleDateFormat("hh:mmaa");
            cv.put("snooze", time.format(t.snooze));
        } else
            cv.put("snooze", "");

        if(t.claimed != null) {
            cv.put("claimed", t.claimed.toString());
        } else
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
        cv.put("done", t.done.toString());
        if(t.snooze != null) {
            SimpleDateFormat time = new SimpleDateFormat("hh:mmaa");
            cv.put("snooze", time.format(t.snooze));
        } else
            cv.put("snooze", "");

        if(t.claimed != null) {
            cv.put("claimed", t.claimed.toString());
        } else
            cv.put("claimed", "");

        wdb.update("tasks", cv, "id=?", new String[]{ ""+t.id });
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
                t.done = Date.valueOf(c.getString(8));
                /*String gt = c.getString(9);
                if(gt.length() > 0) {
                    try {
                        SimpleDateFormat time = new SimpleDateFormat("hh:mmaa");
                        t.snooze = new Date(time.parse(gt).getTime());
                    } catch(Exception e) {}
                }
                String claim = c.getString(10);
                if(claim.length() > 0) {
                    t.claimed = Date.valueOf(claim);
                }*/
                tasks.add(t);
                Log.d("database", "ttt: "+t.startDate.toString() + "/"+t.endDate.toString());
            }while(c.moveToNext());
        }

        Log.d("database", "MaxLen: "+tasks.size());

        return tasks;
    }

    public ArrayList<Task> getTasksByDate(Date date) {
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor c = rdb.query("tasks", new String[]{"name","notes","startdate","freq","time","color","done","enddate","snooze","claimed"},
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
                t.done = Date.valueOf(c.getString(6));
                t.endDate = Date.valueOf(c.getString(7));
                String gt = c.getString(8);
                if(gt.length() > 0) {
                    try {
                        SimpleDateFormat time = new SimpleDateFormat("hh:mmaa");
                        t.snooze = new Date(time.parse(gt).getTime());
                    } catch(Exception e) {}
                }
                String claim = c.getString(9);
                if(claim.length() > 0) {
                    t.claimed = Date.valueOf(claim);
                }
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
                t.done = Date.valueOf(c.getString(8));
                tasks.add(t);
            }while(c.moveToNext());
        }
        return tasks;
    }
}
