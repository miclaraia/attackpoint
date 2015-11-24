package com.michael.objects;

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

    public User(String username, int id) {
        this.username = username;
        this.id = id;

        // TODO add function to query attackpoint for remaining info
    }

    public User(String username, String id) {
        this.username = username;

        if(id.contains(USER_PREFIX)) {
            id = id.split("_")[1];
        }
        int parsed = Integer.parseInt(id);
        this.id = parsed;
    }

    public User(String username, int id, String name, int year, boolean favorite) {
        this.username = username;
        this.id = id;
        this.name = name;
        this.year = year;
        this.favorite = favorite;
    }

    //+++++++++++  SET  ++++++++++++++

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

    public String getId() {
        return USER_PREFIX + id;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
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

}
