package ca.cerroni.justdoit;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.sql.Date;

public class DoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseConnector dbc = new DatabaseConnector(context);
        dbc.open();
        int tid = intent.getIntExtra("TASK", 0);
        Log.d("DoTimer", "AA "+tid);
        Task t = dbc.getTask(tid);
        t.done = new Date(new java.util.Date().getTime());

        dbc.update(t);

        AlertTimer.tasks = dbc.getTasksByDate(t.done);

        Toast.makeText(context, "Task has been completed!", Toast.LENGTH_SHORT).show();
        NotificationManager notMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notMan.cancel(723500+t.id);
        Log.d("DoTimer", "Cancel: "+(723500+t.id) + " N: "+t.name);

        dbc.close();
    }
}
