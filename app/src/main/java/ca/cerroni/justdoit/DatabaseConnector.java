package ca.cerroni.justdoit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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

    public ArrayList<Task> insert(Task t, ArrayList<Task> i) {
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
        i.add(t);
        return i;
    }

    public void clear() throws SQLException {
        wdb.delete("tasks", null, null);
    }

    public ArrayList<Task> getAllTasks() {
        Cursor c = rdb.query("tasks", new String[]{"id","name","notes","startdate","enddate","freq","time"},
                null, null, null, null, "name");
        return null;
    }

    public ArrayList<Task> getTasksByDate(String date) {
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor c = rdb.query("tasks", new String[]{"name","notes","startdate","freq","time","color","done"},
                "? between datetime(startdate) and datetime(enddate)", new String[]{ date }, null,
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
                tasks.add(t);
            }while(c.moveToNext());
        }

        return tasks;
    }
}
