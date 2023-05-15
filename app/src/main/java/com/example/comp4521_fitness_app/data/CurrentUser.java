package com.example.comp4521_fitness_app.data;

public class CurrentUser {
    private static CurrentUser instance = null;
    private String username;

    private CurrentUser() {
        // Private constructor to prevent instantiation from outside
    }

    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void clear() {
        username = null;
    }
}

