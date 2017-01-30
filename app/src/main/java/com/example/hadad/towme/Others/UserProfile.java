package com.example.hadad.towme.Others;

import com.example.hadad.towme.Tables.User;

/**
 * Created by hadad on 1/24/2017.
 */

public class UserProfile {
    static User user;
    static boolean isTow;


    public static boolean isTow() {
        return isTow;
    }

    public static void setIsTow(boolean isTow) {
        UserProfile.isTow = isTow;
    }

    public static User getUser(){
        return user;
    }
    public static Long getId(){ return user.getId(); }
    public static void setUser(User user_) {
        user = user_;
    }
}
