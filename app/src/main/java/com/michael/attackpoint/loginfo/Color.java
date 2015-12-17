package com.michael.attackpoint.loginfo;

/**
 * Class managing log entry colors
 * @author Michael Laraia
 */
public class Color {
    private int color = android.graphics.Color.WHITE;

    /**
     * creates empty Color object
     */
    public Color() {

    }

    /**
     * creates Color object
     * @param color properly formed hexadecimal color
     */
    public Color(String color) {
        this.color = android.graphics.Color.parseColor(color);
    }

    /**
     * creates color object
     * @param color int representing hexadecimal color
     */
    public Color(int color) {
        this.color = color;
    }

    /**
     * Gets color as integer
     * @return color
     */
    public int get() {
        return this.color;
    }

    /**
     * caclulates brightness value of the current color
     * @return brightness
     */
    public int brightness() {
        int r = android.graphics.Color.red(color);
        int g = android.graphics.Color.green(color);
        int b = android.graphics.Color.blue(color);

        double brightness  =  Math.sqrt(.299*r*r + .587*g*g + .114*b*b);
        return (int) Math.round(brightness);
    }

    /**
     * Returns color for text to have sufficient contrast with background color
     * @return white or black
     */
    public int contrast() {
        int brightness = brightness();

        if (brightness >= 186) return android.graphics.Color.BLACK;
        return android.graphics.Color.WHITE;
    }
}
