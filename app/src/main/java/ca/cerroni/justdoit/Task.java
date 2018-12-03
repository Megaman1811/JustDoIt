package ca.cerroni.justdoit;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;

public class Task implements Parcelable {
    public int id;
    public String name;
    public String notes;
    public Date startDate;
    public Date endDate;
    public String time;
    public int freq;
    public String color;
    public boolean done;
    public Date snooze;
    public Date claimed;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(notes);
        dest.writeString(startDate.toString());
        dest.writeString(endDate.toString());
        dest.writeString(time);
        dest.writeInt(freq);
        dest.writeString(color);
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>(){

        @Override
        public Task createFromParcel(Parcel s) {
            Task t = new Task();
            t.id = s.readInt();
            t.name = s.readString();
            t.notes = s.readString();
            t.startDate = Date.valueOf(s.readString());
            t.endDate = Date.valueOf(s.readString());
            t.time = s.readString();
            t.freq = s.readInt();
            t.color = s.readString();
            return t;
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
