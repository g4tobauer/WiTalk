package com.desenvolvigames.applications.witalk.control;

import com.desenvolvigames.applications.witalk.entities.Tab_Ip;
import com.desenvolvigames.applications.witalk.entities.Tab_Usuario;
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
    public JSONObject ParseTabIpToJsonObject(Tab_Ip ip) throws Exception
    {
        return new JSONObject(new Gson().toJson(ip));
    }
    public Tab_Ip ParseJsonObjectToTabIp(JSONObject jsonObject)
    {
        return (Tab_Ip)ParseJsonObject(jsonObject, Tab_Ip.class);
    }

    public JSONObject ParseTabUsuarioToJsonObject(Tab_Usuario usuario) throws Exception
    {
        return new JSONObject(new Gson().toJson(usuario));
    }
    public Tab_Usuario ParseJsonObjectToTabUsuario(JSONObject jsonObject)
    {
        return (Tab_Usuario)ParseJsonObject(jsonObject, Tab_Usuario.class);
    }

    private Object ParseJsonObject(JSONObject jsonObject, Class<?> objClass)
    {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
        return gson.fromJson(jsonObject.toString(), objClass);
    }

    private void foo()
    {
        JsonArray array = new JsonArray();
        array.add(new JsonObject());
    }
}
