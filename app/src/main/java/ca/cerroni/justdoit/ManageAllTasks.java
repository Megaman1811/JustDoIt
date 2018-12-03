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

    DatabaseConnector dbc;
    ListView tasks;
    ManItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_task);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        dbc = new DatabaseConnector(this);
        dbc.open();

        tasks = findViewById(R.id.taskList);

        adapter = new ManItemAdapter(this, R.layout.recycler_view_manitem,
                new int[]{ R.id.mani_title, R.id.mani_desc, R.id.mani_img }, new ArrayList<Task>());
        tasks.setAdapter(adapter);

        adapter.addAll(dbc.getAllTasks());

        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.set(dbc.getAllTasks());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbc.close();
    }

    public void OnClick(View view){
        Intent intent = new Intent(this, editTask.class);
        startActivity(intent);
    }



}
