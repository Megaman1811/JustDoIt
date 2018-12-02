package ca.cerroni.justdoit;

import android.content.Intent;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView tasks;

    ArrayList<String[]> items = new ArrayList<>();

    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tasks = findViewById(R.id.taskList);

        adapter = new ItemAdapter(this, R.layout.recycler_view_item,
                new int[]{ R.id.item_title, R.id.item_desc, R.id.item_time, R.id.item_img }, items);
        tasks.setAdapter(adapter);

        items.add(new String[]{"Hello!", "Testing", "12:00", "Image"});
        items.add(new String[]{"test 123", "A fancy description", "2:00", "Image"});
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
}