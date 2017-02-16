package com.example.atv684.positivityreminders.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.atv684.positivityreminders.R;

public class DetailRow extends RelativeLayout {

    TextView valueName;

    TextView value;

    public DetailRow(Context context) {
        super(context);
        init(context);
    }

    public DetailRow(Context context, String field, String value) {
        super(context);
        init(context);

        setValueName(field);
        setValue(value);
    }

    private void init(Context context) {

        View view = inflate(context, R.layout.detail_row_layout, this);

        valueName = (TextView) view.findViewById(R.id.value_name);
        value = (TextView) view.findViewById(R.id.value);

    }

    public DetailRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DetailRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DetailRow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setValueName(String valueName) {
        this.valueName.setText(valueName);
    }

    public void setValue(String value){
        this.value.setText(value);
    }
}
