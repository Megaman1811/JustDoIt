package ca.cerroni.justdoit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;
import android.widget.ListView;

import java.util.ArrayList;

public class ManageAllTasks extends AppCompatActivity {

    ListView tasks;

    ArrayList<Task> items = new ArrayList<>();

    ItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tasks = findViewById(R.id.taskList);

        adapter = new ItemAdapter(this, R.layout.recycler_view_item,
                new int[]{ R.id.item_title, R.id.item_desc, R.id.item_time, R.id.item_img }, items);
        tasks.setAdapter(adapter);

        //items.add(new String[]{"Hello!", "Testing", "12:00", "Image"});
        //items.add(new String[]{"test 123", "A fancy description", "2:00", "Image"});
        adapter.notifyDataSetChanged();

    }

    public void OnClick(View view){
        Intent intent = new Intent(this, editTask.class);
        startActivity(intent);
    }



}
