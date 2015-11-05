package com.michael.objects;

import android.graphics.Color;
import android.text.Html;
import android.text.format.Time;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michael on 8/4/15.
 */
public class LogInfo {
    private static final int SNIPPET_MAX = 50;

    public static final String JSON_TYPE = "type";
    public static final String JSON_TEXT = "text";
    public static final String JSON_DISTANCE = "distance";
    public static final String JSON_UNIT = "unit";
    public static final String JSON_TIME = "time";
    public static final String JSON_PACE = "pace";
    public static final String JSON_INTENSITY = "intensity";
    public static final String JSON_COLOR = "color";

    public String type;
    public String text;
    public String snippet;
    public String comments;
    public String session;

    public Times time = new Times();
    public Paces pace = new Paces();
    public Intensities intensity = new Intensities();
    public Distances distance = new Distances();
    public Colors color = new Colors();

    public LogInfo() {

    }

    public LogInfo(String text, String type, String distance, String unit, String time) {
        this.text = text;
        this.type = type;

        this.distance.set(distance, unit);
        this.time.set(time);

        this.pace.set(this.time.get(), this.distance.get());
    };
    public LogInfo(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            setType((String) json.get(JSON_TYPE));
            setText((String) json.get(JSON_TEXT));

            if(!json.isNull(JSON_DISTANCE)) {
                Distance d = new Distance((String) json.get(JSON_DISTANCE));
                distance.set(d);
                this.pace.calc(this.time.get(), this.distance.get());
            }

            this.time.set((String) json.get(JSON_TIME));
            this.intensity.set((int) json.get(JSON_INTENSITY));
            this.color.set((int) json.get(JSON_COLOR));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Strings strings() {
        return new Strings(this);
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setText(String text) {
        text = Html.fromHtml(text).toString().replace('\n',' ');
        this.text = text;
        setSnippet(text);
    }

    public void setSnippet(String snippet) {
        snippet = snippet.substring(0, Math.min(text.length(), SNIPPET_MAX));
        int index = snippet.lastIndexOf(' ');
        if (index > 0) {
            snippet = snippet.substring(0, index);
        }
        this.snippet = snippet;
    }

    public String toString() {
        JSONObject json = new JSONObject();
        try {
            json.put(JSON_TYPE, this.type);
            json.put(JSON_TEXT, this.text);
            json.put(JSON_TIME, this.time.toString());
            json.put(JSON_DISTANCE, this.distance.toString());
            json.put(JSON_INTENSITY, this.intensity.get());
            json.put(JSON_COLOR, this.color.get());

            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class Distances {
        private Distance distance;

        public void set(int distance, String unit) {
            Distance d = new Distance(distance, unit);
            set(d);
        }

        public void set(String distance, String unit) {
            Distance d = new Distance(distance, unit);
            set(d);
        }

        public void set(float distance, String unit) {
            Distance d = new Distance(distance, unit);
            set(d);
        }

        public void set(Distance distance) {
            this.distance = distance;
            //if (distance != null && distance.distance != 0) LogInfo.this.pace.set();
        }

        public Distance get() {
            return distance;
        }

        public String toString() {
            if (this.distance == null) return null;
            return distance.toString();
        }

    }

    public class Intensities {
        private int intensity;

        public void set(int intensity) {
            this.intensity = intensity;
        }

        public void set(String intensity) {
            this.intensity = Integer.parseInt(intensity);
        }

        public int get() {
            return this.intensity;
        }

        public String toString() {
            return Integer.toString(this.intensity);
        }
    }

    public class Times {
        private Time time;

        public void set(String time) {
            String[] pieces = time.split(":");
            int h = 0;
            int m = 0;
            int s = 0;
            switch (pieces.length) {
                case 1:
                    s = Integer.parseInt(pieces[0]);
                    break;
                case 2:
                    m = Integer.parseInt(pieces[0]);
                    s = Integer.parseInt(pieces[1]);
                    break;
                case 3:
                    h = Integer.parseInt(pieces[0]);
                    m = Integer.parseInt(pieces[1]);
                    s = Integer.parseInt(pieces[2]);
                    break;
            }
            this.time = new android.text.format.Time();
            this.time.set(s,m,h,0,0,0);
        }

        public void set(Time time) {
            this.time = time;
        }

        public Time get() {
            return this.time;
        }

        public String toString() {
            if (this.time == null) {
                return null;
            }
            String format;
            if (time.hour == 0) {
                if (time.minute == 0) {
                    format = "%S";
                }
                else format = "%M:%S";
            }
            else format = "%H:%M:%S";
            return time.format(format);
        }
    }

    public class Paces {
        private Time pace;
        private String unit;

        public void set() {
            if (LogInfo.this.distance.distance == null) return;
            Time t = LogInfo.this.time.time;
            Distance d = LogInfo.this.distance.distance;
            this.unit = d.unit;
            set(calc(t, d));
        }

        public void set(Time time, Distance distance) {
            this.unit = distance.unit;
            set(calc(time, distance));
        }

        public void set(Time pace) {
            this.pace = pace;
        }

        public Time calc(Time time, Distance distance) {
            // TODO make sure this calculates properly
            int h = time.hour;
            int m = time.minute;
            int s = time.second;

            int pace = (int) Math.floor((h*3600 + m*60 + s) / distance.distance);

            h = (int) pace / 3600;
            int r = pace % 3600;
            m = (int) r / 60;
            s = r % 60;
            Time out = new Time();
            out.set(s,m,h,0,0,0);
            return out;
        }

        public Time get() {
            return pace;
        }

        public String toString() {
            String out;
            if (this.pace == null) {
                return null;
            }
            if (this.pace.hour == 0) {
                out = pace.format("%M:%S");
            }
            else out = pace.format("%H:%M:%S");

            out += " / " + this.unit;
            return out;
        }
    }

    public class Colors {
        private int color = Color.WHITE;

        public void set(String color) {
            this.color = Color.parseColor(color);
        }

        public void set(int color) {
            this.color = color;
        }

        public int get() {
            return this.color;
        }

        public int brightness() {
            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);

            double brightness  =  Math.sqrt(.299*r*r + .587*g*g + .114*b*b);
            return (int) Math.round(brightness);
        }

        public int getContrast() {
            int brightness = brightness();

            if (brightness >= 186) return Color.BLACK;
            return Color.WHITE;
        }
    }

    public class Strings {
        public String text;
        public int color;
        public int contrast;
        public String snippet;
        public String type;
        public String distance;
        public String pace;
        public String time;
        public String intensity;
        public String comments;
        public String session;

        public Strings(LogInfo li) {
            this.text = li.text;
            this.color = li.color.get();
            this.contrast = li.color.getContrast();
            this.snippet = li.snippet;
            this.type = li.type;
            this.distance = li.distance.toString();
            this.pace = li.pace.toString();
            this.time = li.time.toString();
            this.intensity = li.intensity.toString();

            // TODO
            this.comments = "";
            this.session = "";
        }
    }
}
