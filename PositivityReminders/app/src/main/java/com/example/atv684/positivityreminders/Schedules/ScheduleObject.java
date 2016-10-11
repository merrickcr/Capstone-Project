package com.example.atv684.positivityreminders.Schedules;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Random;

/**
 * Created by atv684 on 9/30/16.
 */
public class ScheduleObject {


    Date startTime;
    Date endTime;

    int frequency;

    int id;

    ScheduleObject(){
        id = new Random().nextInt();
    }

    ScheduleObject(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);

            startTime = new Date(jsonObject.getString("startTime"));
            endTime = new Date(jsonObject.getString("endTime"));
            id = jsonObject.optInt("id");
            frequency = jsonObject.optInt("frequency");

        }
        catch (JSONException e){
            Log.e("SCHEDULE OBJECT", e.getMessage());
        }

    }

    public String toJson(){

        JSONObject object = new JSONObject();

        try {
            object.put("startTime", startTime.toString());
            object.put("endTime", endTime.toString());
            object.put("frequency", frequency);
            object.put("id", id);
        }
        catch (JSONException e){
            return null;
        }

        return object.toString();

    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
