package com.friends.friends;

import android.app.Application;


import com.devs.acr.AutoErrorReporter;



public class FriendsApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
//        AutoErrorReporter.get(this)
//
//                .setEmailAddresses("arsalanwizard@gmail.com")
//                .setEmailSubject("Friends Auto Crash Report")
//                .start();

    }
}