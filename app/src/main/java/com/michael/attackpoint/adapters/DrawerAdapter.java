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
import com.michael.objects.NavDrawer;
import com.michael.objects.NavDrawerItem;

import java.util.ArrayList;

/**
 * Created by michael on 8/24/15.
 */
public class DrawerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NavDrawer> navDrawerItems;
    private LayoutInflater inflater;

    public DrawerAdapter(Context context, ArrayList<NavDrawer> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;

        inflater = LayoutInflater.from(context);
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
        NavDrawer item = navDrawerItems.get(position);
        View view = null;
        if (item.getType() == NavDrawer.TYPE_SEPERATOR)  {
            view = getSepView(convertView, parent, item);
        } else if (item.getType() == NavDrawer.TYPE_REGULAR) {
            view = getItemView(convertView, parent, (NavDrawerItem) item);
        }

        return view;
    }

    public View getItemView(View convertView, ViewGroup parent, NavDrawerItem item) {
        ItemViewHolder navMenuItemHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_item, null);

            TextView labelView = (TextView) convertView.findViewById(R.id.item_text);
            ImageView iconView = (ImageView) convertView.findViewById(R.id.item_icon);

            navMenuItemHolder = new ItemViewHolder();
            navMenuItemHolder.labelView = labelView ;
            navMenuItemHolder.iconView = iconView ;

            convertView.setTag(navMenuItemHolder);
        }

        if ( navMenuItemHolder == null ) {
            navMenuItemHolder = (ItemViewHolder) convertView.getTag();
        }

        navMenuItemHolder.labelView.setText(item.getName());
        navMenuItemHolder.iconView.setImageResource(item.getIcon());

        return convertView;
    }

    public View getSepView(View convertView, ViewGroup parent, NavDrawer item) {
        SectionViewHolder sectionHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_section, null);

            TextView label = (TextView) convertView.findViewById(R.id.item_text);

            sectionHolder = new SectionViewHolder();
            sectionHolder.labelView = label;

            convertView.setTag(sectionHolder);
        }

        if (sectionHolder == null) sectionHolder = (SectionViewHolder) convertView.getTag();

        sectionHolder.labelView.setText(item.getName());

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return navDrawerItems.get(position).type;
    }

    private static class ItemViewHolder {
        private TextView labelView;
        private ImageView iconView;
    }

    private class SectionViewHolder {
        private TextView labelView;
    }
}
