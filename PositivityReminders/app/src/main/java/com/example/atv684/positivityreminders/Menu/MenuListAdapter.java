package com.example.atv684.positivityreminders.menu;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.atv684.positivityreminders.R;

import java.util.ArrayList;

public class MenuListAdapter extends BaseAdapter {

    ArrayList<Object> menuItems = new ArrayList<>();

    Context context;

    public MenuListAdapter(ArrayList<Object> menuItems, Context context) {
        this.menuItems = menuItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return menuItems.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Object currentObject = menuItems.get(position);

        View view = null;
        if(convertView == null){

            if(currentObject instanceof SideNavMenuItem) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list_item, parent, false);


                ((ImageView)view.findViewById(R.id.icon)).setImageDrawable(ContextCompat.getDrawable(context, ((SideNavMenuItem) currentObject).getIcon()));
                ((TextView)view.findViewById(R.id.menu_text)).setText(((SideNavMenuItem) currentObject).getText());

                view.setContentDescription(((SideNavMenuItem) currentObject).getText() + context.getString(R.string.cd_button));
            }

        }
        else{
            view = convertView;
        }
        return view;
    }

    public ArrayList<Object> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(ArrayList<Object> menuItems) {
        this.menuItems = menuItems;
    }


}
