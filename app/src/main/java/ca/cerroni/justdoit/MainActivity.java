package ca.cerroni.justdoit;

import android.content.Intent;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import java.sql.Date;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseConnector dbc;
    ListView tasks;
    CalendarView cview;
    Date cDate;

    //ArrayList<Task> items = new ArrayList<>();

    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbc = new DatabaseConnector(this);
        dbc.open();
        dbc.clear();
        tasks = findViewById(R.id.taskList);

        adapter = new ItemAdapter(this, R.layout.recycler_view_item,
                new int[]{ R.id.item_title, R.id.item_desc, R.id.item_time, R.id.item_img }, new ArrayList<Task>());
        tasks.setAdapter(adapter);

        cview = findViewById(R.id.calendarView);
        cDate = new Date(cview.getDate());
        Log.d("calendar", "CurDate: "+cDate.toString());

        cview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                cDate = Date.valueOf(year+"-"+(month+1)+"-"+dayOfMonth);
                Log.d("calendar", cDate.toString() + "//" + adapter.getCount());

                adapter.set(getTasksAtCurDate(dbc.getTasksByDate(cDate)));
                //dbc.getAllTasks();
                adapter.notifyDataSetChanged();
            }
        });

        Task t1 = new Task();
        t1.name = "Task thing!";
        t1.notes = "It's a task";
        Date d1 = Date.valueOf("2018-12-01");
        t1.startDate = d1;
        t1.endDate = d1;
        t1.time = "5:22";
        t1.freq = 1;
        t1.color = "ff0000";
        dbc.insert(t1);

        t1 = new Task();
        t1.name = "Newer task";
        t1.notes = "It's yet another task";
        t1.startDate = d1;
        Date d2 = Date.valueOf("2018-12-03");
        t1.endDate = d2;
        t1.time = "7:35";
        t1.freq = 2;
        t1.color = "00ff00";
        dbc.insert(t1);

        adapter.set(getTasksAtCurDate(dbc.getTasksByDate(cDate)));

        adapter.notifyDataSetChanged();
    }

    private ArrayList<Task> getTasksAtCurDate(ArrayList<Task> got) {
        Log.d("datefind", "" + got.size());
        boolean occurs = false;
        for(int i = 0; i < got.size(); i++) {
            Task t = got.get(i);
            Date start = t.startDate;
            Log.d("datefind", "{1} name: "+t.name+" start: " + start.toString() + " end: "+t.endDate.toString());
            while(start.before(t.endDate) || start.equals(t.endDate)) {
                Log.d("datefind", "{2} name: "+t.name+" start: " + start.toString() + " cdate: "+cDate.toString()+" eq: "+(start.equals(cDate)?"true":"false"));
                if(start.equals(cDate) || start.toString().equals(cDate.toString())) {
                    Log.d("datefind", "Found: "+start.toString()+" within: "+cDate.toString() +" at: "+t.startDate.toString());
                    occurs = true;
                    break;
                }
                start.setDate(start.getDate()+t.freq);
            }
            if(!occurs) {
                got.remove(i);
            }
        }
        return got;
    }

    @Override
    protected void onResume() {
        super.onResume();
        cDate = new Date(cview.getDate());
        adapter.set(getTasksAtCurDate(dbc.getTasksByDate(cDate)));
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return true;

    }

    public void manageScreen(MenuItem item) {
        Intent intent = new Intent(this, ManageAllTasks.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbc.close();
    }
}