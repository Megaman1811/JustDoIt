package ca.cerroni.justdoit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class SnoozeReceiver extends BroadcastReceiver {
    DatabaseConnector dbc;
    @Override
    public void onReceive(Context context, Intent intent) {
        dbc = new DatabaseConnector(context);
        dbc.open();
        Task t = (Task)intent.getParcelableExtra("TASK");
        /*SimpleDateFormat d = new SimpleDateFormat("hh:mmaa");
        Date date;
        try {
            date = new Date(d.parse(t.time).getTime());
        } catch(Exception e) {

        }*/
        t.snooze = new Date(new java.util.Date().getTime());

        dbc.update(t);

        AlertTimer.tasks = dbc.getTasksByDate(t.snooze);

        Toast.makeText(context, "Task has been snoozed.", Toast.LENGTH_SHORT).show();
    }
}
