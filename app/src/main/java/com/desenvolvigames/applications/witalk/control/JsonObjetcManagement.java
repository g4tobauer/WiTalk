package com.desenvolvigames.applications.witalk.control;

import android.util.Log;

import com.desenvolvigames.applications.witalk.entities.old.EntityBase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 * Created by Joao on 16/04/2017.
 */

public class JsonObjetcManagement
{
    public static <T extends EntityBase>T ParseJsonObject(String jsonString, Class<T> objClass)
    {
        T entityResult = null;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
        try
        {
            entityResult = gson.fromJson(jsonString, objClass);
        }catch (Exception ex)
        {
            Log.i("JsonObjetcManagement", "ParseJsonObject = "+ex.getMessage());
        }
        return entityResult;
    }
    public static <T extends EntityBase>JSONObject ParseObjectJson(T objEntity)
    {
        JSONObject jsonObject = null;
        try
        {
            jsonObject = new JSONObject(new Gson().toJson(objEntity));
        }catch (Exception ex)
        {
            Log.i("JsonObjetcManagement", "ParseObjectJson = "+ex.getMessage());
        }
        return jsonObject;
    }

    private void foo()
    {
        JsonArray array = new JsonArray();
        array.add(new JsonObject());
    }
}
