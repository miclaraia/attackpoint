package com.michael.attackpoint.drawer;

import com.michael.attackpoint.R;

/**
 * Created by michael on 8/24/15.
 */
public abstract class NavDrawerItem {
    public static final String DEFAULT_GROUP = "Attackpoint";
    public static final int TYPE_COUNT = 2;

    public String mName;
    public int mType;
    public int mIcon;
    public String mGroup;

    public DrawerListener mClick;


    /*public NavDrawerItem(String name, String group, int icon, DrawerListener click) {
        mName = name;
        mType = TYPE_REGULAR;
        mGroup = group;
        mIcon = icon;
        mClick = click;
    }*/

    public NavDrawerItem(String name, String group, DrawerListener click) {
        mName = name;
        mGroup = group;
        mClick = click;
    }

    public NavDrawerItem(String name, String group) {
        mName = name;
        mGroup = group;
    }

    /*public NavDrawerItem(String name, int type) {
        mName = name;
        mType = type;
        *//*if (type == TYPE_USER) {
            this.icon = R.drawable.ic_person;
        }*//*
    }*/

    public void setName(String name) {
        mName = name;
    }

    public void setType(int type) {
        mType = type;
    }

    public void setGroup(String group) {
        mGroup = group;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }


    public String getName() {
        return mName;
    }

    public int getType() {
        return mType;
    }

    public String getGroup() {
        return mGroup;
    }

    public int getIcon() {
        return mIcon;
    }

    public abstract void action();

    public interface DrawerListener {
        void click();
    }

}
