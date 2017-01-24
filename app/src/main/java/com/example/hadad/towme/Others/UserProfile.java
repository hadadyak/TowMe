package com.example.hadad.towme.Others;

import com.example.hadad.towme.Tables.User;

/**
 * Created by hadad on 1/24/2017.
 */

public class UserProfile {
    static User user;

    public static User getUser(){
        return user;
    }

    public static void setUser(User user_) {
        user = user_;
    }
}
