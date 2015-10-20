package com.michael.network;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by michael on 10/16/15.
 */
public class Users {
    private static final String DEBUG_TAG = "attackpoint.User";
    private static final String EXPIRE_FORMAT = "ccc, dd-MMM-yyyy HH:mm:ss zzz";

    private List<User> users;

    private SQLiteDatabase database;
    private UserDbHelper dbHelper;

    public Users(Context context) {
        dbHelper = new UserDbHelper(context);
        users = getAllUsers();
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addNewUser(String name, Map<String, List<String>> headers) throws Exception {
        User user = new User(name, headers);
        if (user.getToken() == null) throw new Exception();
        open();
        long row_id = database.insert(UserDbHelper.TABLE, null, user.storeSQL());
        close();
        user.setId(row_id);
        users.add(user);
    }

    public void removeUser(User user) {
        int id = user.getId();
        database.delete(UserDbHelper.TABLE, UserDbHelper.COLUMN_ID
                + " = " + id, null);
    }

    private List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();

        Cursor cursor = database.query(UserDbHelper.TABLE,
                UserDbHelper.allColumns(), null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = cursorToUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return users;
    }

    public void removeExpired() {
        Iterator<User> i = users.iterator();
        while (i.hasNext()) {
            User user = i.next();
            if (user.isExpired()) {
                removeUser(user);
                i.remove();
            }
        }
    }

    public List<User> getUsers() {
        return users;
    }

    private User cursorToUser(Cursor cursor) {
        long id = cursor.getLong(0);
        String name = cursor.getString(1);
        String token = cursor.getString(2);
        String expire = cursor.getString(3);
        User user = new User((int) id, name, token, expire);
        return user;
    }
}
