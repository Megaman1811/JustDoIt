package ca.cerroni.justdoit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Timer;

public class JustDoItAlerts extends Service {
    private Timer timer = new Timer();
    public static boolean running = false;

    public JustDoItAlerts() {
    }

    @Override
    public int onStartCommand(Intent i, int flags, int start) {
        running = true;
        DatabaseConnector dbc = new DatabaseConnector(getApplicationContext());
        dbc.open();
        ArrayList<Task> tasks = dbc.getTasksByDate(new Date(new java.util.Date().getTime()));
        Log.d("DoTimer", "Starting timer...");
        timer.schedule(new AlertTimer(getApplicationContext(), getPackageName(), tasks), 0, 60000);
        dbc.close();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
