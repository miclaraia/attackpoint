package com.michael.attackpoint.loginfo;

/**
 * Created by michael on 12/2/15.
 */
public class Color {
    private int color = android.graphics.Color.WHITE;

    public Color(String color) {
        this.color = android.graphics.Color.parseColor(color);
    }

    public Color(int color) {
        this.color = color;
    }

    public int get() {
        return this.color;
    }

    public int brightness() {
        int r = android.graphics.Color.red(color);
        int g = android.graphics.Color.green(color);
        int b = android.graphics.Color.blue(color);

        double brightness  =  Math.sqrt(.299*r*r + .587*g*g + .114*b*b);
        return (int) Math.round(brightness);
    }

    public int contrast() {
        int brightness = brightness();

        if (brightness >= 186) return android.graphics.Color.BLACK;
        return android.graphics.Color.WHITE;
    }
}
