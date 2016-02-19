package com.michael.attackpoint.drawer;

/**
 * Created by michael on 2/19/16.
 */
public class NavItemReg extends NavDrawerItem {

    public NavItemReg(String name, String group, int icon, DrawerListener click) {
        super(name, group, click);

        mIcon = icon;
    }

    public void action() {
        mClick.click();
    }
}
