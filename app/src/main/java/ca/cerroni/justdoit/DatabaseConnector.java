package ca.cerroni.justdoit;

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

    public ArrayList<Task> getAllTaskItems() {
        Cursor c = rdb.query("tasks", new String[]{"id","name","notes","startdate","enddate","freq","time"},
                null, null, null, null, "name");
        return null;

    }

    public ArrayList<Task> getTasksByDate(String date) {
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor c = rdb.query("tasks", new String[]{"name","notes","startdate","freq","time","color","done"},
                "? between startdate and enddate", new String[]{ date }, null,
                null, "name");

        if(c.moveToFirst()) {
            do {
                Task t = new Task();
                t.name = c.getString(0);
                t.notes = c.getString(1);
                t.startDate = Date.valueOf(c.getString(2));
                t.freq = c.getInt(3);
                t.time = Date.valueOf(c.getString(4));
                // TODO: color, done
                tasks.add(t);
            }while(c.moveToNext());
        }

        return tasks;
    }
}
