package com.michael.objects;

/**
 * Created by michael on 8/24/15.
 */
public class NavDrawer {
    public static final String DEFAULT_GROUP = "Attackpoint";
    public static final int TYPE_REGULAR = 1;
    public static final int TYPE_SEPERATOR = 2;
    public String name;
    public int type;

    public NavDrawer(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }
}
