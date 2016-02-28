package com.michael.attackpoint.log.loginfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michael on 2/18/16.
 */
public class Note extends LogInfo {
    public static final String NAME = "note";

    public Note(String type, String text) {
        super();
        mActivity.set(type);
        mDescription.set(text);
    }

    public Note(String jsonString) {
        super(jsonString);
    }

    /*@Override
    public void fromJSON(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            setType((String) json.get(JSON_TYPE));
            setText((String) json.get(JSON_TEXT));

            date.fromJSON(json);

            // get comments from JSON
            JSONArray comments_array= json.getJSONArray(JSON_COMMENTS);
            for (int i = 0; i < comments_array.length(); i++) {
                JSONObject c = comments_array.getJSONObject(i);
                addComment(new LogComment(c));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
}
