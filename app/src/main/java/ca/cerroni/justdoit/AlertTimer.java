package ca.cerroni.justdoit;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimerTask;

public class AlertTimer extends TimerTask {
    private Context cont; // The service context
    private String packName;
    public static ArrayList<Task> tasks = null;

    public AlertTimer(Context c, String pack, ArrayList<Task> ts) {
        cont = c;
        packName = pack;
        if(tasks == null)
            tasks = ts;
    }

    private void notif(Task tsk) {

        RemoteViews rv = new RemoteViews(packName, R.layout.notification_layout);

        Bitmap b = Bitmap.createBitmap(128,128, Bitmap.Config.ARGB_8888);
        Canvas t = new Canvas(b);
        t.drawBitmap(b,0,0,null);

        Paint bg = new Paint();
        bg.setColor(Color.parseColor("#"+tsk.color));
        bg.setStyle(Paint.Style.FILL);
        t.drawPaint(bg);

        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        p.setTextSize(128);
        p.setTextAlign(Paint.Align.CENTER);
        t.drawText(tsk.name.substring(0,1), 64, 64-((p.descent() + p.ascent())/2), p);

        rv.setImageViewBitmap(R.id.mani_img, b);
        rv.setTextViewText(R.id.mani_title, tsk.name);
        rv.setTextViewText(R.id.mani_desc, tsk.notes);
        Intent snooze = new Intent(cont, SnoozeReceiver.class);
        Intent done = new Intent(cont, DoneReceiver.class);
        snooze.putExtra("TASK", tsk.id);
        done.putExtra("TASK", tsk.id);
        rv.setOnClickPendingIntent(R.id.mani_edit, PendingIntent.getBroadcast(cont, 1298371211,
                snooze, PendingIntent.FLAG_UPDATE_CURRENT));
        rv.setOnClickPendingIntent(R.id.mani_del, PendingIntent.getBroadcast(cont, 1298371211,
                done, PendingIntent.FLAG_UPDATE_CURRENT));

        Notification nBuilder = new NotificationCompat.Builder(cont, "justdoitnotifs")
                .setSmallIcon(R.drawable.ic_notif_dark)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(rv)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).build();
        NotificationManagerCompat notMan = NotificationManagerCompat.from(cont);
        Log.d("DoTimer", "Notif: "+(723500+tsk.id) + " N: "+tsk.name);
        notMan.notify(723500+tsk.id, nBuilder);
    }

    @Override
    public void run() {
        Date curDate = new Date(new java.util.Date().getTime());
        long curTime = curDate.getTime();
        ArrayList<Task> tmp = MainActivity.getTasksAtCurDate(tasks, curDate);
        for(Task t : tmp) {

            SimpleDateFormat time = new SimpleDateFormat("hh:mmaa");
            Calendar timeParse = Calendar.getInstance();
            try {
                timeParse.setTime(time.parse(t.time));
            } catch(Exception e) {}
            Calendar taskTime = Calendar.getInstance();
            taskTime.set(Calendar.HOUR_OF_DAY, timeParse.get(Calendar.HOUR_OF_DAY));
            taskTime.set(Calendar.MINUTE, timeParse.get(Calendar.MINUTE));
            taskTime.set(Calendar.SECOND, 0);
            Log.d("DoTimer", "Set hours "+timeParse.get(Calendar.HOUR_OF_DAY) + " " +
                    timeParse.get(Calendar.MINUTE) + " | " +t.time);

            Log.d("DoTimer", "Tried "+t.name+" "+Task.fmt.format(taskTime.getTime()) + " D: "+t.done.getTime() + " C: "+taskTime.getTimeInMillis());
            if(taskTime.getTimeInMillis() < curTime && t.snooze.getTime()+1800000 < curTime &&
                    t.done.getTime() < taskTime.getTimeInMillis()) { // 30 min snooze, on the next task
                notif(t);
            }
        }
    }
}
