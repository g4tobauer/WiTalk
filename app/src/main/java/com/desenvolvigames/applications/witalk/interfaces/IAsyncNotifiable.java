package com.desenvolvigames.applications.witalk.interfaces;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by Joao on 19/04/2017.
 */

public interface IAsyncNotifiable
{
    Context GetContext();
    void ExecuteNotify(String tag, String result);
}
