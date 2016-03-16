package com.michael.attackpoint.users;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michael.attackpoint.util.Singleton;

/**
 * Created by michael on 11/11/15.
 */
public class User {
    private static final String DEBUG_TAG = "ap.User";
    private static final String USER_PREFIX = "user_";

    private String name;
    private String username;
    private int year;
    private int id;
    private String location;
    private String email;
    private boolean favorite;

    private UserTable mUserTable;
    private Singleton mSingleton;

    public User(String username, int id) {
        this.username = username;
        this.id = id;

        mUserTable = new UserTable();
        mSingleton = Singleton.getInstance();

        checkTable();
    }

    public User(String username, String id) {
        this.username = username;

        mUserTable = new UserTable();
        mSingleton = Singleton.getInstance();

        if(id.contains(USER_PREFIX)) {
            id = id.split("_")[1];
        }
        int parsed = Integer.parseInt(id);
        this.id = parsed;

        checkTable();
    }

    public User(String username, int id, String name, String location, int year) {
        this.username = username;
        this.id = id;
        this.name = name;
        this.year = year;
        this.location = location;
    }

    public void checkTable() {
        if (mUserTable.userExists(id)) {
            setUser(mUserTable.getUser(id));
        } else {
            // TODO update data when user changes their info?2
            UserRequest request = new UserRequest(id, new Response.Listener<User>() {
                @Override
                public void onResponse(User user) {
                    setUser(user);
                    mUserTable.addUser(user);
                    done();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            mSingleton.add(request);
        }
    }

    public void done() {

    }

    //+++++++++++  SET  ++++++++++++++

    public void setUser(User user) {
        this.name = user.name;
        this.username = user.username;
        this.year = user.year;
        this.id = user.id;
        this.location = user.location;
        this.email = user.email;
        this.favorite = user.favorite;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    //+++++++++++  GET  ++++++++++++++

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public String getLocation() {
        return location;
    }

    public boolean isFavorite() {
        return favorite;
    }


    public String toString() {
        String s = "Name: " + name
                + "\nUsername: " + username
                + "\nYear: " + year
                + "\nId: " + id
                + "\nfavorit: " + favorite;
        return s;
    }

    public Strings strings() {
        return new Strings(this);
    }

    public class Strings {
        public String name;
        public String username;
        public String year;
        public String id;
        public String location;
        public String email;

        public Strings(User user) {
            name = user.name;
            username = user.username;
            year = "" + user.year;
            id = "" + user.id;
            location = user.location;
            email = user.email;

        }
    }

}
