package ca.cerroni.justdoit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<Task> {
    private static final int ITEM_TITLE = 0;
    private static final int ITEM_DESC = 1;
    private static final int ITEM_TIME = 2;
    private static final int ITEM_IMG = 3;

    private int[] ids;
    private int res;

    public ItemAdapter(Context context, int res, int[] ids, ArrayList<Task> items) {
        super(context, res, ids[0], items);
        this.ids = ids;
        this.res = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater in = LayoutInflater.from(getContext());
        View view = in.inflate(res, parent, false);

        Task task = getItem(position);

        TextView v = view.findViewById(ids[ITEM_TITLE]);
        v.setText(task.name);
        v = view.findViewById(ids[ITEM_DESC]);
        v.setText(task.notes);
        v = view.findViewById(ids[ITEM_TIME]);
        v.setText(task.time);

        ImageView im = view.findViewById(ids[ITEM_IMG]);
        Bitmap b = Bitmap.createBitmap(128,128, Bitmap.Config.ARGB_8888);
        Canvas t = new Canvas(b);
        t.drawBitmap(b,0,0,null);
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        p.setTextSize(128);
        p.setTextAlign(Paint.Align.CENTER);
        t.drawText(task.name.substring(0,1), 64, 64-((p.descent() + p.ascent())/2), p);
        im.setImageBitmap(b);
        im.setBackgroundColor(Color.parseColor("#"+task.color));
        return view;
    }

    public void set(ArrayList<Task> ts) {
        clear();
        addAll(ts);
    }
}
