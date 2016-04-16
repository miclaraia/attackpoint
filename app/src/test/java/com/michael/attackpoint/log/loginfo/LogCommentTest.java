package com.michael.attackpoint.log.loginfo;

import com.michael.attackpoint.log.loginfo.LogComment.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by michael on 3/21/16.
 */
public class LogCommentTest {
    private static final List<Comment> EMPTY = new ArrayList<>();
    private static final List<Comment> NONEMPTY;

    static {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("title1", "author1", 1));
        comments.add(new Comment("title2", "author2", 2));

        NONEMPTY = comments;
    }

    private LogComment mItem;

    @Before
    public void setup() {
        mItem = new LogComment();
    }

    @Test
    public void set_setsItem() {
        mItem.set(EMPTY);
        assertThat(mItem.get(), equalTo(EMPTY));
        mItem.set(NONEMPTY);
        assertThat(mItem.get(), equalTo(NONEMPTY));
    }

    @Test
    public void isEmpty_true() {
        mItem.set(EMPTY);
        assertTrue(mItem.isEmpty());
    }

    @Test
    public void isEmpty_false() {
        mItem.set(NONEMPTY);
        assertFalse(mItem.isEmpty());
    }

    @Test
    public void toJSON_containskeys() {
        JSONObject json = new JSONObject();
        mItem.set(NONEMPTY);
        json = mItem.toJSON(json);

        assertTrue(json.has(LogComment.JSON));
    }

    @Test
    public void toJSON_goodData() throws JSONException {
        JSONObject json = new JSONObject();
        mItem.set(NONEMPTY);
        json = mItem.toJSON(json);
        JSONArray array = json.getJSONArray(LogComment.JSON);

        assertThat(array.length(), equalTo(2));

        Comment comment = NONEMPTY.get(0);
        JSONObject object = array.getJSONObject(0);
        assertThat(object.getString(LogComment.JSON_TITLE), equalTo(comment.getTitle()));
        assertThat(object.getString(LogComment.JSON_AUTHOR), equalTo(comment.getAuthor()));
        assertThat(object.getInt(LogComment.JSON_ID), equalTo(comment.getID()));
    }

    @Test
    public void fromJSON_parses() {
        mItem.set(NONEMPTY);
        JSONObject json = new JSONObject();
        json = mItem.toJSON(json);

        mItem.set(new ArrayList<Comment>());
        mItem.fromJSON(json);

        assertThat(mItem.get().get(0), equalTo(NONEMPTY.get(0)));
        assertThat(mItem.get().get(1), equalTo(NONEMPTY.get(1)));
    }
}