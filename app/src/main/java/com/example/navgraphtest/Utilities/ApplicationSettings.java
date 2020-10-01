package com.example.navgraphtest.Utilities;
import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationSettings {

    static public String getSetting(Context mContext, String settingName, String defaultValue)
    {
        SharedPreferences settings = mContext.getSharedPreferences("SETTINGS", 0);
        return settings.getString(settingName, defaultValue);
    }

    static public void setSetting(Context mContext, String settingName, String defaultValue)
    {
        SharedPreferences settings = mContext.getSharedPreferences("SETTINGS", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(settingName, defaultValue);
        editor.apply();
    }

    static public void clearSettings(Context mContext) {
        SharedPreferences settings = mContext.getSharedPreferences("SETTINGS", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();
    }

    static public boolean settingsExist(Context mContext) {
        SharedPreferences settings = mContext.getSharedPreferences("SETTINGS", 0);

        // The app requires a calorie goal, so check for this.
        return (!settings.getString("calorieGoal", "").equals(""));

    }
}
