package com.michael.objects;

/**
 * Created by michael on 12/29/15.
 */
public class UserCallback extends User {
    private Callback mCallback;

    public interface Callback {
        public void go();
    }

    public UserCallback(String username, int id, Callback callback) {
        super(username, id);
        mCallback = callback;
    }

    public UserCallback(String username, String id, Callback callback) {
        super(username, id);
        mCallback = callback;
    }

    @Override
    public void done() {
        mCallback.go();
    }
}
