package ca.cerroni.justdoit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseOpenHelper extends SQLiteOpenHelper {


    public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tasks (" +
                "id integer primary key autoincrement," +
                "name text," +
                "notes text," +
                "startDate text," +
                "endDate text," +
                "time text," +
                "freq integer," +
                "color text," +
                "done integer," +
                "snooze text," +
                "claimed text" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
