package com.desenvolvigames.applications.witalk.control;

import android.util.Log;

import com.desenvolvigames.applications.witalk.activities.BaseActivity;
import com.desenvolvigames.applications.witalk.entities.EntityBase;
import com.desenvolvigames.applications.witalk.entities.Tab_Ip;
import com.desenvolvigames.applications.witalk.entities.Tab_Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Joao on 16/04/2017.
 */

public class JsonObjetcManagement
{
    public static <T extends EntityBase>T ParseJsonObject(JSONObject jsonObject, Class<T> objClass)
    {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
        return gson.fromJson(jsonObject.toString(), objClass);
    }
    public static <T extends EntityBase>JSONObject ParseObjectJson(T objEntity)
    {
        JSONObject jsonObject = null;
        try
        {
            jsonObject = new JSONObject(new Gson().toJson(objEntity));
        }catch (Exception ex)
        {
            Log.i("ParseTabIpToJsonObject", "ParseTabIpToJsonObject = "+ex.getMessage());
        }
        return jsonObject;
    }

    private void foo()
    {
        JsonArray array = new JsonArray();
        array.add(new JsonObject());
    }
}
