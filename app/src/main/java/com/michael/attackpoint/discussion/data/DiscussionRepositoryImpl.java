package com.michael.attackpoint.discussion.data;

import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.michael.attackpoint.discussion.Discussion;
import com.michael.attackpoint.util.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by michael on 5/2/16.
 */
public class DiscussionRepositoryImpl implements DiscussionRepository {

    protected Map<Integer, Discussion> mDiscussions;
    protected RequestQueue mRequestQueue;

    public DiscussionRepositoryImpl() {
        mDiscussions = new HashMap<>(0);
        mRequestQueue = Volley.newRequestQueue(Singleton.getInstance().getContext());
    }

    public DiscussionRepositoryImpl(RequestQueue requestQueue) {
        mDiscussions = new HashMap<>(0);
        mRequestQueue = requestQueue;
    }

    @Override
    public void getDiscussion(final boolean forceRefresh, final int id, final LoadCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (forceRefresh || !inLocalCache(id)) {
                    removeLocalCache(id);

                    refreshDiscussion(id, new RefreshCallback() {
                        @Override
                        public void onLoaded(Discussion discussion) {
                            callback.onLoaded(discussion);
                        }

                        @Override
                        public void onError(VolleyError e) {
                            callback.onError(e);
                            if (e instanceof NoConnectionError && inLocalCache(id)) {
                                callback.onLoaded(getLocalCache(id));
                            }
                        }
                    });
                } else {
                    callback.onLoaded(getLocalCache(id));
                }
            }
        }).start();
    }

    @Override
    public void getDiscussion(int id, LoadCallback callback) {
        getDiscussion(false, id, callback);
    }

    @Override
    public void refreshDiscussion(final int id, final RefreshCallback callback) {
        DiscussionRequest request = new DiscussionRequest(id, new Response.Listener<Discussion>() {
            @Override
            public void onResponse(Discussion discussion) {
                mDiscussions.put(id, discussion);
                callback.onLoaded(discussion);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                callback.onError(e);
            }
        });
        mRequestQueue.add(request);
    }

    protected boolean inLocalCache(int id) {
        if (mDiscussions.containsKey(id)) return true;
        return false;
    }

    protected Discussion getLocalCache(int id) {
        Discussion discussion = mDiscussions.get(id);
        return discussion;
    }

    protected void removeLocalCache(int id) {
        if (inLocalCache(id)) {
            mDiscussions.remove(id);
        }
    }
}
