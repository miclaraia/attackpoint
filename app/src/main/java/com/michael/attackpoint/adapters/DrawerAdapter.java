package com.michael.attackpoint.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.michael.attackpoint.R;
import com.michael.attackpoint.Singleton;
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

            convertView.setTag(R.id.drawer_holder, navMenuItemHolder);
        }

        if ( navMenuItemHolder == null ) {
            navMenuItemHolder = (ItemViewHolder) convertView.getTag(R.id.drawer_holder);
        }

        navMenuItemHolder.labelView.setText(item.getName());
        navMenuItemHolder.iconView.setImageResource(item.getIcon());

        convertView.setOnClickListener(new ItemClickListener(item));

        return convertView;
    }

    public View getSepView(View convertView, ViewGroup parent, NavDrawer item) {
        SectionViewHolder sectionHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_section, null);

            TextView label = (TextView) convertView.findViewById(R.id.item_text);

            sectionHolder = new SectionViewHolder();
            sectionHolder.labelView = label;

            convertView.setTag(R.id.drawer_holder, sectionHolder);
        }

        if (sectionHolder == null) sectionHolder = (SectionViewHolder) convertView.getTag(R.id.drawer_holder);

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

    public static class DrawerClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Toast.makeText(Singleton.getInstance().getContext(), "Drawer item clicked!", Toast.LENGTH_LONG).show();
        }
    }

    private class ItemClickListener implements View.OnClickListener {
        private NavDrawerItem item;

        public ItemClickListener(NavDrawerItem item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            switch (item.getGroup()) {
                case Attackpoint
            }
            Toast.makeText(Singleton.getInstance().getContext(), item.getName(), Toast.LENGTH_LONG).show();
        }
    }
}
