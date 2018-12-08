package ca.cerroni.justdoit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class editTask extends AppCompatActivity {
    private boolean wasTask = false;
    Task task = null;
    TextView name;
    TextView notes;
    TextView start;
    TextView end;
    TextView time;
    TextView color;
    TextView freq;
    DatabaseConnector dbc;
    Calendar startCal = Calendar.getInstance();
    Calendar endCal = Calendar.getInstance();
    int timeHour = 0;
    int timeMin = 0;
    DatePickerDialog.OnDateSetListener startdate;
    DatePickerDialog.OnDateSetListener enddate;
    TimePickerDialog.OnTimeSetListener timepick;

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
            wasTask = true;
            name.setText(task.name);
            notes.setText(task.notes);
            start.setText(task.startDate.toString());
            end.setText(task.endDate.toString());
            time.setText(task.time);
            color.setText(task.color);
            freq.setText(task.freq+"");
            SimpleDateFormat tf = new SimpleDateFormat("hh:mmaa");
            try {
                Calendar tc = Calendar.getInstance();
                tc.setTime(tf.parse(task.time));
                timeHour = tc.get(Calendar.HOUR_OF_DAY);
                timeMin = tc.get(Calendar.MINUTE);
            } catch(Exception e) {}
            startCal.setTime(task.startDate);
            endCal.setTime(task.endDate);
        }

        timepick = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String minstr = minute+"";
                if(minstr.length() == 1)
                    minstr = "0"+minstr;
                int hr = hourOfDay%12;
                if(hr == 0) hr = 12;
                Log.d("TimePick", hourOfDay+" "+minute);
                time.setText(hr+":"+minstr+(hourOfDay>=12?"pm":"am"));
            }
        };

        startdate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startCal.set(Calendar.YEAR, year);
                startCal.set(Calendar.MONTH, month);
                startCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateStart();
            }
        };
        enddate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endCal.set(Calendar.YEAR, year);
                endCal.set(Calendar.MONTH, month);
                endCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEnd();
            }
        };
    }

    // Save btn
    public void OnClick(View view){
        if(name.getText().toString().length() == 0 ||
                notes.getText().toString().length() == 0 ||
                time.getText().toString().length() == 0 ||
                start.getText().toString().length() == 0 ||
                end.getText().toString().length() == 0 ||
                color.getText().toString().length() == 0 ||
                freq.getText().toString().length() == 0){
            Toast.makeText(this, "Fields are empty!", Toast.LENGTH_LONG).show();
            return;
        }
        if(task == null) {
            task = new Task();
        }
        task.name = name.getText().toString();
        task.notes = notes.getText().toString();
        task.startDate = Date.valueOf(start.getText().toString());
        task.endDate = Date.valueOf(end.getText().toString());

        SimpleDateFormat tp = new SimpleDateFormat("hh:mmaa");
        String tstr = time.getText().toString();
        try {
            tp.parse(tstr);
        } catch(ParseException e) {
            Toast.makeText(this, "Invalid Time!", Toast.LENGTH_LONG).show();
            return;
        }
        task.time = time.getText().toString();

        String col = color.getText().toString();
        if((col.matches("\\p{XDigit}+") && col.length() == 6) || // Color matches hex
                (col.length() == 7 && col.startsWith("#") &&
                        col.substring(1).matches("\\p{XDigit}+"))) {
            if(col.startsWith("#")) {
                task.color = col.substring(1);
            } else
                task.color = col;
        } else {
            Toast.makeText(this, "Invalid Color!", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            task.freq = Integer.parseInt(freq.getText().toString());
        } catch(NumberFormatException e) {
            Toast.makeText(this, "Invalid Frequency!", Toast.LENGTH_LONG).show();
            return;
        }
        if (wasTask) {
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

    public void onStartClick(View view) {
        new DatePickerDialog(this, startdate, startCal.get(Calendar.YEAR),
                startCal.get(Calendar.MONTH), startCal.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void onEndClick(View view) {
        new DatePickerDialog(this, enddate, endCal.get(Calendar.YEAR),
                endCal.get(Calendar.MONTH), endCal.get(Calendar.DAY_OF_MONTH)).show();
    }

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private void updateStart() {
        start.setText(df.format(startCal.getTime()));
    }

    private void updateEnd() {
        end.setText(df.format(endCal.getTime()));
    }

    public void onTimeClick(View view) {
        new TimePickerDialog(this, timepick, timeHour, timeMin, false).show();
    }
}
