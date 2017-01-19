package com.example.atv684.positivityreminders.Schedules;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ServiceCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.atv684.positivityreminders.R;
import com.example.atv684.positivityreminders.provider.QuoteDBHelper;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by atv684 on 10/7/16.
 */
public class ViewSchedulesFragment extends Fragment {

    ListView listView;

    FloatingActionButton addScheduleFab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_schedules_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<ScheduleObject> schedules = new ArrayList<>();

        schedules = new QuoteDBHelper(getContext(), null).getSchedules();
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
                startActivity(intent);
            }
        });



    }

}
