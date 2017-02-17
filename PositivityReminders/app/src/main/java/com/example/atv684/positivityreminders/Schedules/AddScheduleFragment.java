package com.example.atv684.positivityreminders.schedules;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.atv684.positivityreminders.R;
import com.example.atv684.positivityreminders.provider.QuoteDBHelper;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.Date;

/**
 * Created Chris on 10/1/16.
 */
public class AddScheduleFragment extends Fragment implements DayToggleButton.OnDayToggledListener {

    private Button startTimeButton;

    private Date startTime;


    private Button addScheduleButton;

    private LinearLayout daysLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_schedule_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startTimeButton = (Button) view.findViewById(R.id.start_time_button);

        addScheduleButton = (Button) view.findViewById(R.id.add_schedule_button);

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTime = new Date();
                        startTime.setHours(selectedHour);
                        startTime.setMinutes(selectedMinute);

                        startTimeButton.setText(getResources().getString(R.string.start_time_colon) + selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle(getResources().getString(R.string.select_start_time));
                mTimePicker.show();

            }
        });

        daysLayout = (LinearLayout) view.findViewById(R.id.day_layout);

        daysLayout.addView(new DayToggleButton(getContext(), this, getString(R.string.monday_label)));
        daysLayout.addView(new DayToggleButton(getContext(), this, getString(R.string.tuesday_label)));
        daysLayout.addView(new DayToggleButton(getContext(), this, getString(R.string.wednesday_label)));
        daysLayout.addView(new DayToggleButton(getContext(), this, getString(R.string.thursday_label)));
        daysLayout.addView(new DayToggleButton(getContext(), this, getString(R.string.friday_label)));
        daysLayout.addView(new DayToggleButton(getContext(), this, getString(R.string.saturday_label)));
        daysLayout.addView(new DayToggleButton(getContext(), this, getString(R.string.sunday_label)));

        addScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewSchedule();
            }
        });
    }

    public JSONArray parseDays() {

        JSONArray daysJson = new JSONArray();

        for (int i = 0; i < daysLayout.getChildCount(); i++) {

            View view = daysLayout.getChildAt(i);

            if (view instanceof DayToggleButton) {
                DayToggleButton toggle = (DayToggleButton) view;
                if (toggle.isChecked()) {
                    daysJson.put(((DayToggleButton) view).getLabel());
                }
            }
        }

        return daysJson;

    }

    private void addNewSchedule() {

        ScheduleObject schedule = new ScheduleObject();
        schedule.setStartTime(startTime);
        schedule.setDays(parseDays());

        if (schedule.getStartTime() == null || schedule.getDaysJSONArray() == null) {
            Snackbar.make(getView(), R.string.must_specify_time, Toast.LENGTH_LONG).show();
            return;
        }
        if (schedule.getDaysJSONArray().length() <= 0) {
            Snackbar.make(getView(), R.string.must_be_day_of_week, Snackbar.LENGTH_LONG).show();
            return;
        }

        long id = QuoteDBHelper.get(getContext()).addSchedule(schedule);

        schedule.setId(id);

        NotificationScheduler scheduler = new NotificationScheduler(getContext());
        scheduler.scheduleNotification(schedule);

        getActivity().finish();
        getActivity().setResult(Activity.RESULT_OK);

    }


    @Override
    public void onDayToggled(boolean isChecked, String day) {

    }
}
