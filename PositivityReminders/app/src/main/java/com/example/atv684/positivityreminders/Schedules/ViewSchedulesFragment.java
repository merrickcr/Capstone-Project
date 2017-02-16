package com.example.atv684.positivityreminders.schedules;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.atv684.positivityreminders.R;
import com.example.atv684.positivityreminders.provider.QuoteDBHelper;

import java.util.ArrayList;

public class ViewSchedulesFragment extends Fragment {

    ListView listView;

    FloatingActionButton addScheduleFab;

    public static final int ADD_SCHEDULE_REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_schedules_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<ScheduleObject> schedules = new ArrayList<>();

        schedules = QuoteDBHelper.get(getContext()).getSchedules();
        listView.setAdapter(new ScheduleListAdapter(getContext(), schedules));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) view.findViewById(R.id.list_view);

        addScheduleFab = (FloatingActionButton) view.findViewById(R.id.add_schedule_fab);

        addScheduleFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddScheduleActivity.class);
                startActivityForResult(intent, ADD_SCHEDULE_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_SCHEDULE_REQUEST_CODE){
            Snackbar.make(getView(), getString(R.string.snackbar_quote_has_been_added), Snackbar.LENGTH_LONG).show();
        }
    }
}
