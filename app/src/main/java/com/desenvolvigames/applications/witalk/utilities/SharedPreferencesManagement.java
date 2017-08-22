package com.desenvolvigames.applications.witalk.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by NOTEBOOK on 31/07/2017.
 */

public class SharedPreferencesManagement {

    private static final String preferenceFileKey = SharedPreferencesManagement.class.getName();
    private SharedPreferences mSharedPreferences;
    private AppCompatActivity mActivity;

    public SharedPreferencesManagement(AppCompatActivity activity){
        mActivity = activity;
        mSharedPreferences = mActivity.getSharedPreferences(preferenceFileKey, Context.MODE_PRIVATE);
    }
    private void gravarPreferencia(String key, String value){
        mSharedPreferences = mActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    private String lerPreferencia(String key){
        mSharedPreferences = mActivity.getPreferences(Context.MODE_PRIVATE);
        return mSharedPreferences.getString(key, null);
    }
}
