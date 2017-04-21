package com.desenvolvigames.applications.witalk.interfaces;

import org.json.JSONObject;

/**
 * Created by Joao on 19/04/2017.
 */

public interface IJsonNotifiable
{
    void ExecuteNotify(String json);
    String GetJsonParameters();
}
