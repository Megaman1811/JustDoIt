package ca.cerroni.justdoit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class ManageAllTasks extends AppCompatActivity {

    DatabaseConnector dbc;
    ListView tasks;
    ManItemAdapter adapter;
    SearchView search;
    String sort = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        dbc = new DatabaseConnector(this);
        dbc.open();

        tasks = findViewById(R.id.taskList);

        adapter = new ManItemAdapter(this, R.layout.recycler_view_manitem,
                new int[]{ R.id.mani_title, R.id.mani_desc, R.id.mani_img }, new ArrayList<Task>());
        tasks.setAdapter(adapter);


        if(savedInstanceState != null) {
            sort = savedInstanceState.getString("sort");
            adapter.sort = sort;
        }
        search = findViewById(R.id.searchView);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() == 0) {
                    adapter.set(dbc.getAllTasks(sort));
                } else {
                    adapter.set(dbc.searchTasksByName(newText));
                }
                adapter.notifyDataSetChanged();
                return false;
            }
        });


        adapter.addAll(dbc.getAllTasks(sort));

        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_manage_activity, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.set(dbc.getAllTasks(sort));
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbc.close();
    }

    @Override
    protected void onSaveInstanceState(Bundle b) {
        super.onSaveInstanceState(b);
        b.putString("sort", sort);
    }

    public void OnClick(View view){
        Intent intent = new Intent(this, editTask.class);
        startActivity(intent);
    }


    public void sortName(MenuItem item) {
        adapter.clear();
        sort = "name";
        adapter.addAll(dbc.getAllTasks(sort));
        adapter.sort = sort;
    }

    public void sortDate(MenuItem item) {
        adapter.clear();
        sort = "startdate,time";
        adapter.addAll(dbc.getAllTasks(sort));
        adapter.sort = sort;
    }
}
