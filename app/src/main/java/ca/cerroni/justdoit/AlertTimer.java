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
import android.widget.RemoteViews;

import java.util.List;
import java.util.TimerTask;

public class AlertTimer extends TimerTask {
    private Context cont; // The service context
    private String packName;
    public static List<Task> tasks = null;

    public AlertTimer(Context c, String pack, List<Task> ts) {
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
        snooze.putExtra("TASK", tsk);

        rv.setOnClickPendingIntent(R.id.mani_edit, PendingIntent.getBroadcast(cont, 0, new Intent(), 0)); // Actually snooze


        Notification nBuilder = new NotificationCompat.Builder(cont, "justdoit")
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(rv)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).build();
    }

    @Override
    public void run() {
    }
}
