package ca.cerroni.justdoit;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class SnoozeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseConnector dbc = new DatabaseConnector(context);
        dbc.open();
        int tid = intent.getIntExtra("TASK", 0);
        Log.d("DoTimer", "AB "+tid);
        Task t = dbc.getTask(tid);
        /*SimpleDateFormat d = new SimpleDateFormat("hh:mmaa");
        Date date;
        try {
            date = new Date(d.parse(t.time).getTime());
        } catch(Exception e) {

        }*/
        t.snooze = new Date(new java.util.Date().getTime());

        dbc.update(t);

        AlertTimer.tasks = dbc.getTasksByDate(t.snooze);

        Toast.makeText(context, "Task snoozed for 30 min.", Toast.LENGTH_SHORT).show();
        NotificationManager notMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notMan.cancel(723500+t.id);
        Log.d("DoTimer", "Cancel: "+(723500+t.id) + " N: "+t.name);
        dbc.close();
    }
}
