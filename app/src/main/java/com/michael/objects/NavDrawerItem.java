package com.michael.objects;

/**
 * Created by michael on 8/24/15.
 */
public class NavDrawerItem extends NavDrawer {
    public int icon;
    public String group;
    public String action;


    public NavDrawerItem(String name, String group, int icon, String action) {
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

    public void setAction(String action) {
        this.action = action;
    }

    public String getGroup() {
        return this.group;
    }

    public int getIcon() {
        return this.icon;
    }

    public String getAction() {
        return this.action;
    }
}
