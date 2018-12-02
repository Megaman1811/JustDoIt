package ca.cerroni.justdoit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<String[]> {
    private int[] ids;
    private int res;

    public ItemAdapter(Context context, int res, int[] ids, ArrayList<String[]> items) {
        super(context, res, ids[0], items);
        this.ids = ids;
        this.res = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater in = LayoutInflater.from(getContext());
        View view = in.inflate(res, parent, false);

        String[] data = getItem(position);

        for(int i = 0; i < data.length-1; i++) { // -1 cause our last element is an image
            TextView v = view.findViewById(ids[i]);
            v.setText(data[i]);
        }
        //TODO: image

        return view;
    }
}
