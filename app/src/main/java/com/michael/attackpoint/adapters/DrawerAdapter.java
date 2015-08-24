package com.michael.attackpoint.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.michael.attackpoint.R;
import com.michael.objects.DrawerItem;

import java.util.ArrayList;

/**
 * Created by michael on 8/24/15.
 */
public class DrawerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DrawerItem> navDrawerItems;

    public DrawerAdapter(Context context, ArrayList<DrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_item, null);
        }

        ImageView itemIcon = (ImageView) convertView.findViewById(R.id.item_icon);
        TextView itemName = (TextView) convertView.findViewById(R.id.item_text);

        itemIcon.setImageResource(navDrawerItems.get(position).icon);
        itemName.setText(navDrawerItems.get(position).name);

        return convertView;
    }
}
