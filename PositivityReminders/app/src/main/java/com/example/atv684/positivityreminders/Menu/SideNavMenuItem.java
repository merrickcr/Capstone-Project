package com.example.atv684.positivityreminders.menu;

import android.support.annotation.DrawableRes;

public class SideNavMenuItem {

    public SideNavMenuItem(int icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    @DrawableRes
    int icon;

    String text;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
