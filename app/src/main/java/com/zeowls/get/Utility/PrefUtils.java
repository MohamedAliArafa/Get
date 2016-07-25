package com.zeowls.get.Utility;

/**
 * Created by root on 5/12/16.
 */
import android.content.Context;
import com.zeowls.get.DataModel.UserDataModel;

public class PrefUtils {

    public static void setCurrentUser(UserDataModel currentUser, Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        complexPreferences.putObject("current_user_value", currentUser);
        complexPreferences.commit();
    }

    public static UserDataModel getCurrentUser(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        return complexPreferences.getObject("current_user_value", UserDataModel.class);
    }

    public static void clearCurrentUser(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
    }


}