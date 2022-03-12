package amrk000.NewsLand.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static SharedPrefManager instance;
    private static SharedPreferences sharedPreferences;

    // Keys
    private static final String FIRST_LAUNCH = "firstlaunch";
    private static final String API_KEY = "apikey";
    private static final String LANGUAGE_CODE ="language";
    private static final String COUNTRY_CODE ="country";

    // Defaults
    public static final String DEFAULT_APIKEY = "552044257f4c467bbe47079e5ffa83a9"; //Get an API Key From: https://newsapi.org/
    public static final String DEFAULT_LANGUAGE="en";
    public static final String DEFAULT_COUNTRY=""; //empty = world wide news

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences("settings",MODE_PRIVATE);
    }

    public static SharedPrefManager get(Context context){
        if(instance == null){
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public boolean isFirstLaunch() {
        return sharedPreferences.getBoolean(FIRST_LAUNCH,true);
    }

    public String getApiKey() {
        return sharedPreferences.getString(API_KEY,DEFAULT_APIKEY);
    }

    public String getLanguageCode() {
        return sharedPreferences.getString(LANGUAGE_CODE,DEFAULT_LANGUAGE);
    }

    public String getCountryCode() {
        return sharedPreferences.getString(COUNTRY_CODE,DEFAULT_COUNTRY);
    }

    public void setFirstLaunch(boolean value){
        sharedPreferences.edit()
                .putBoolean(FIRST_LAUNCH,value)
                .apply();
    }

    public void setApiKey(String value){
        sharedPreferences.edit()
                .putString(API_KEY,value)
                .apply();
    }

    public void setLanguageCode(String value){
        sharedPreferences.edit()
                .putString(LANGUAGE_CODE,value)
                .apply();
    }

    public void setCountryCode(String value){
        sharedPreferences.edit()
                .putString(COUNTRY_CODE,value)
                .apply();
    }

}
