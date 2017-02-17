package com.example.atv684.positivityreminders.schedules;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.example.atv684.positivityreminders.R;

public class DayToggleButton extends LinearLayout {

    Context context;

    private String label;

    private OnDayToggledListener listener;

    private ToggleButton toggleButton;

    public DayToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DayToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DayToggleButton(Context context) {
        super(context);
    }

    public DayToggleButton(Context context, OnDayToggledListener listener, String label) {
        super(context);

        this.label = label;
        this.listener = listener;
        this.label = label;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;

        setLayoutParams(params);

        init(context);
    }

    private void init(Context context) {
        this.context = context;

        inflate(context, R.layout.toggle_button, this);

        toggleButton = (ToggleButton) findViewById(R.id.toggle_button);

        toggleButton.setText(label);
        toggleButton.setTextOff(label);
        toggleButton.setTextOn(label);

        toggleButton.setChecked(true);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                listener.onDayToggled(b, label);
            }
        });

    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public OnDayToggledListener getListener() {
        return listener;
    }

    public void setListener(OnDayToggledListener listener) {
        this.listener = listener;
    }

    public boolean isChecked() {
        return toggleButton.isChecked();
    }

    public interface OnDayToggledListener {

        public void onDayToggled(boolean isChecked, String day);

    }
}
