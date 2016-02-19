package com.michael.attackpoint.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.michael.attackpoint.R;
import com.michael.attackpoint.Singleton;

import java.util.ArrayList;

/**
 * Created by michael on 8/24/15.
 */
public class DrawerAdapter extends BaseAdapter {
    private static final String DEBUG_TAG = "attackpoint.DAdapter";
    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private LayoutInflater inflater;

    public DrawerAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
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
        NavDrawerItem item = navDrawerItems.get(position);
        View view = null;
        if (item.getType() == NavDrawerItem.TYPE_SEPERATOR)  {
            view = getSepView(convertView, parent, item);
        } else if (item.getType() == NavDrawerItem.TYPE_REGULAR) {
            view = getItemView(convertView, parent, item);
        }/* else if (item.getType() == NavDrawerItem.TYPE_USER) {
            view = getUserView(convertView, parent, item);
        }
        view.setTag(R.id.drawer_info, "test");*/

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

        //convertView.setOnClickListener(new ItemClickListener(item));

        return convertView;
    }

    public View getUserView(View convertView, ViewGroup parent, NavDrawerItem item) {
        UserViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_user, null);

            TextView labelView = (TextView) convertView.findViewById(R.id.item_text);
            ImageView iconView = (ImageView) convertView.findViewById(R.id.item_icon);
            ImageView removeView = (ImageView) convertView.findViewById(R.id.item_remove);

            removeView.setTag(item);

            viewHolder = new UserViewHolder();
            viewHolder.labelView = labelView;
            viewHolder.iconView = iconView;
            viewHolder.removeView = removeView;

            convertView.setTag(R.id.drawer_holder, viewHolder);
        }

        if ( viewHolder == null ) {
            viewHolder = (UserViewHolder) convertView.getTag(R.id.drawer_holder);
        }

        viewHolder.labelView.setText(item.getName());
        viewHolder.removeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Singleton.getInstance().getUserGroup().removeUser((NavDrawerItem) v.getTag());
                } catch (NavGroupUsers.UserException e) {
                    e.printStackTrace();
                }
            }
        });

        //convertView.setOnClickListener(new ItemClickListener(item));

        return convertView;
    }

    public View getSepView(View convertView, ViewGroup parent, NavDrawerItem item) {
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
        return NavDrawerItem.TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return navDrawerItems.get(position).type;
    }

    private static class ItemViewHolder {
        private TextView labelView;
        private ImageView iconView;
    }

    private static class UserViewHolder {
        private TextView labelView;
        private ImageView iconView;
        private ImageView removeView;
    }

    private class SectionViewHolder {
        private TextView labelView;
    }
}
