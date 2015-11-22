package com.michael.attackpoint.drawer;

import com.michael.attackpoint.R;

/**
 * Created by michael on 8/24/15.
 */
public class NavDrawerItem {
    public static final String DEFAULT_GROUP = "Attackpoint";
    public static final int TYPE_REGULAR = 1;
    public static final int TYPE_SEPERATOR = 2;
    public static final int TYPE_USER = 3;

    public String name;
    public int type;
    public int icon;
    public String group;


    public NavDrawerItem(String name, String group, int icon) {
        this.name = name;
        this.type = TYPE_REGULAR;
        this.group = group;
        this.icon = icon;
    }

    public NavDrawerItem(String name, int type) {
        this.name = name;
        this.type = type;
        if (type == TYPE_USER) {
            this.icon = R.drawable.ic_person;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }


    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public String getGroup() {
        return this.group;
    }

    public int getIcon() {
        return this.icon;
    }

}
