package com.michael.attackpoint.discussion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 1/26/16.
 */
public class Discussion {

    private static final String DEBUG_TAG = "ap.discussion";

    private List<Comment> mComments;
    private int mId;
    private String mTitle;
    private String mCategory;
    
    public Discussion(List<Comment> comments, int id, String title, String category) {
        mComments = comments;
        mId = id;
        mTitle = title;
        mCategory = category;
    }

    public Discussion(int id, String title, String category) {
        mComments = new ArrayList<>();
        mId = id;
        mTitle = title;
        mCategory = category;
    }
    
    public Discussion(int id) {
        // TODO use network call to get discussion
    }

    public List<Comment> getComments() {
        return mComments;
    }

    public void setComments(List<Comment> comments) {
        mComments = comments;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    void addComment(Comment comment) {
        mComments.add(comment);
    }
}
