package ca.cerroni.justdoit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.sql.Date;

public class editTask extends AppCompatActivity {
    Task task = null;
    TextView name;
    TextView notes;
    TextView start;
    TextView end;
    TextView time;
    TextView color;
    TextView freq;
    DatabaseConnector dbc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        dbc = new DatabaseConnector(this);
        dbc.open();
        name = findViewById(R.id.Name);
        notes = findViewById(R.id.Description);
        start = findViewById(R.id.Start);
        end = findViewById(R.id.End);
        time = findViewById(R.id.Time);
        color = findViewById(R.id.Colort);
        freq = findViewById(R.id.freq);

        Intent in = getIntent();
        if((task = (Task)in.getParcelableExtra("TASK")) != null) {
            name.setText(task.name);
            notes.setText(task.notes);
            start.setText(task.startDate.toString());
            end.setText(task.endDate.toString());
            time.setText(task.time);
            color.setText(task.color);
            freq.setText(task.freq+"");
        }
    }
    public void OnClick(View view){
        boolean wasTask = true;
        if(task == null) {
            wasTask = false;
            task = new Task();
        }
        task.name = name.getText().toString();
        task.notes = notes.getText().toString();
        task.startDate = Date.valueOf(start.getText().toString());
        task.endDate = Date.valueOf(end.getText().toString());
        task.time = time.getText().toString();
        task.color = color.getText().toString();
        task.freq = Integer.parseInt(freq.getText().toString());
        // Save btn
        if(wasTask) {
            dbc.update(task);
        } else {
            dbc.insert(task);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbc.close();
    }
}
