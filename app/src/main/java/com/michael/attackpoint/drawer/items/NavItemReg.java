package com.michael.attackpoint.drawer.items;

/**
 * Created by michael on 2/19/16.
 */
public abstract class NavItemReg extends NavDrawerItem {
    private int mIcon;

    public NavItemReg(String name, String group, int icon) {
        super(name, group);

        mIcon = icon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }

    public int getIcon() {
        return mIcon;
    }

    public abstract void click();
}
