package com.michael.attackpoint.drawer.items;

import com.michael.attackpoint.R;

/**
 * Created by michael on 8/24/15.
 */
public abstract class NavDrawerItem {
    public static final String DEFAULT_GROUP = "Attackpoint";

    public String mName;
    public String mGroup;

    public NavDrawerItem(String name, String group) {
        mName = name;
        mGroup = group;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setGroup(String group) {
        mGroup = group;
    }

    public String getName() {
        return mName;
    }

    public String getGroup() {
        return mGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof NavDrawerItem) {
            NavDrawerItem item = (NavDrawerItem) o;
            if (this.getName().equals(item.getName())) {
                if (this.getGroup().equals(item.getGroup())) return true;
            }
        }

        return false;
    }
}
