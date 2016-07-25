package com.zeowls.get.BackEnd;

import android.content.Context;
import com.firebase.client.Firebase;

public class FireOwl {

    private Firebase firebaseRef;

    public FireOwl(Context context) {
        Firebase.setAndroidContext(context);
        String firebaseURL = "https://giftshop.firebaseio.com/";
        firebaseRef = new Firebase(firebaseURL);
    }

}
