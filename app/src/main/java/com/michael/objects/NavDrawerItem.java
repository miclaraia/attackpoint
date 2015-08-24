package com.michael.objects;

/**
 * Created by michael on 8/24/15.
 */
public class NavDrawerItem extends NavDrawer {
    public int icon;
    public String group;

    public NavDrawerItem(String name, String group, int icon) {
        super(name, TYPE_REGULAR);
        this.group = group;
        this.icon = icon;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getGroup() {
        return this.group;
    }

    public int getIcon() {
        return this.icon;
    }
}
