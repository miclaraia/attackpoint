package com.michael.attackpoint.drawer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.michael.attackpoint.R;
import com.michael.attackpoint.drawer.items.NavDrawerGroup;
import com.michael.attackpoint.drawer.items.NavDrawerItem;
import com.michael.attackpoint.drawer.items.NavGroupGeneral;
import com.michael.attackpoint.drawer.items.NavGroupUsers;
import com.michael.attackpoint.drawer.items.NavItemHeader;
import com.michael.attackpoint.drawer.items.NavItemReg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 8/24/15.
 */
public class NavDrawer implements DrawerContract.Drawer {
    private static final String DEBUG_TAG = "NavDrawer";

    private DrawerAdapter mAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerContract.Activity mActivity;

    private List<NavDrawerGroup> mNavGroups;
    private List<NavDrawerItem> mNavItems;


    public NavDrawer(DrawerContract.Activity activity, DrawerLayout drawerLayout,
                     ListView drawerList) {
        mDrawerLayout = drawerLayout;
        mActivity = activity;

        mDrawerToggle = activity.getDrawerToggle(mDrawerLayout);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mNavGroups = new ArrayList<>();

        DrawerClickListener l = new DrawerClickListener();
        drawerList.setOnItemClickListener(l);

        mNavGroups.add(new NavGroupGeneral(mActivity));
        mNavGroups.add(new NavGroupUsers(mActivity));
        mAdapter = new DrawerAdapter(mActivity.getContext());
        refresh();
        drawerList.setAdapter(mAdapter);
    }

    public void addGroup(NavDrawerGroup group) {
        mNavGroups.add(group);
    }

    public void addGroup(int position, NavDrawerGroup group) {
        mNavGroups.add(position, group);
    }

    public int findGroup(String name) {
        for (int i = 0; i < mNavGroups.size(); i++) {
            if (name.equals(mNavGroups.get(i).name())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void open() {

    }

    @Override
    public void close() {

    }

    @Override
    public void toggle() {

    }

    @Nullable
    @Override
    public NavDrawerGroup getGroup(String name) {
        for (NavDrawerGroup group : mNavGroups) {
            if (group.name().equals(name)) return group;
        }
        return null;
    }

    @Override
    public void removeGroup(String name) {
        for (int i = 0; i < mNavGroups.size(); i++) {
            NavDrawerGroup group = mNavGroups.get(i);
            if (group.name().equals(name)) {
                mNavGroups.remove(i);
                break;
            }
        }
        refresh();
    }


    @Override
    public void addItem(NavDrawerItem item) {
        NavDrawerGroup group = getGroup(item.getGroup());
        group.add(item);
        refresh();
    }

    @Override
    public void removeItem(NavDrawerItem item) {
        NavDrawerGroup group = getGroup(item.getGroup());
        group.remove(item) ;
        refresh();
    }

    @Override
    public void refresh() {
        mNavItems = refreshList();

        //TODO seems sketchy
        /*mAdapter = new DrawerAdapter(mActivity, navItems);
        mDrawerList.setAdapter(mAdapter);*/

        mAdapter.replaceData(mNavItems);
    }

    @Override
    public ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    public List<NavDrawerItem> refreshList() {
        ArrayList<NavDrawerItem> navItems = new ArrayList<>();
        for (NavDrawerGroup group : mNavGroups) {
            navItems.addAll(navItems.size(), group.getAll());
        }
        return navItems;
    }

    public class DrawerClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            view.getTag(R.id.drawer_info);
            Log.d(DEBUG_TAG, mNavItems.get(position).getName());

            NavItemReg item = (NavItemReg) mNavItems.get(position);

            mDrawerLayout.closeDrawer(Gravity.LEFT);

            //implement the items action
            item.click();
        }
    }

    public static class DrawerAdapter extends BaseAdapter {
        private List<NavDrawerItem> mItems;
        private LayoutInflater mInflater;

        private static final int TYPE_HEADER = 1;
        private static final int TYPE_REGULAR = 2;
        private static final int TYPE_COUNT = 3;

        public DrawerAdapter(Context context){
            mInflater = LayoutInflater.from(context);
            mItems = new ArrayList<>(0);
        }

        public void replaceData(List<NavDrawerItem> data) {
            setData(data);
            notifyDataSetChanged();
        }

        public void setData(List<NavDrawerItem> data) {
            mItems = data;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NavDrawerItem item = mItems.get(position);
            View view = null;
            if (item instanceof NavItemHeader)  {
                view = getSepView(convertView, parent, item);
            } else if (item instanceof NavItemReg) {
                view = getItemView(convertView, parent, (NavItemReg) item);
            }

            return view;
        }

        public View getItemView(View convertView, ViewGroup parent, NavItemReg item) {
            ItemViewHolder navMenuItemHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.drawer_item, null);

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

        /*public View getUserView(View convertView, ViewGroup parent, NavItemHeader header) {
            UserViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.drawer_user, null);

                TextView labelView = (TextView) convertView.findViewById(R.id.item_text);
                ImageView iconView = (ImageView) convertView.findViewById(R.id.item_icon);
                ImageView removeView = (ImageView) convertView.findViewById(R.id.item_remove);

                removeView.setTag(header);

                viewHolder = new UserViewHolder();
                viewHolder.labelView = labelView;
                viewHolder.iconView = iconView;
                viewHolder.removeView = removeView;

                convertView.setTag(R.id.drawer_holder, viewHolder);
            }

            if ( viewHolder == null ) {
                viewHolder = (UserViewHolder) convertView.getTag(R.id.drawer_holder);
            }

            viewHolder.labelView.setText(header.getName());
        *//*viewHolder.removeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Singleton.getInstance().getUserGroup().removeUser((NavDrawerItem) v.getTag());
                } catch (NavGroupUsers.UserException e) {
                    e.printStackTrace();
                }
            }
        });*//*

            //convertView.setOnClickListener(new ItemClickListener(item));

            return convertView;
        }*/

        public View getSepView(View convertView, ViewGroup parent, NavDrawerItem item) {
            SectionViewHolder sectionHolder = null;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.drawer_section, null);

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
            return TYPE_COUNT;
        }

        @Override
        public int getItemViewType(int position) {
            NavDrawerItem item = mItems.get(position);
            if (item instanceof NavItemHeader) return TYPE_HEADER;
            else if (item instanceof NavItemReg) return TYPE_REGULAR;
            else return TYPE_REGULAR;
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
}
