package com.michael.attackpoint.discussion.data;

import com.android.volley.VolleyError;
import com.michael.attackpoint.discussion.Discussion;

import java.util.List;

/**
 * Created by michael on 5/2/16.
 */
public interface DiscussionRepository {

    void getDiscussion(boolean forceRefresh, int id, LoadCallback callback);

    void getDiscussion(int id, LoadCallback callback);

    void refreshDiscussion(int id, RefreshCallback callback);

    interface LoadCallback {
        void onLoaded(Discussion discussion);

        void onError(Exception e);
    }

    interface RefreshCallback {
        void onLoaded(Discussion discussion);

        void onError(VolleyError e);
    }

}
