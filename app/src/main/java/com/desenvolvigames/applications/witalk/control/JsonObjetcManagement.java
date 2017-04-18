package com.desenvolvigames.applications.witalk.control;

import com.desenvolvigames.applications.witalk.entities.UsuarioClass;
import com.google.gson.Gson;

import org.json.JSONObject;
/**
 * Created by Joao on 16/04/2017.
 */

public class JsonObjetcManagement
{
    public JSONObject ParseUsuarioToJsonObject(UsuarioClass usuario) throws Exception
    {
        return new JSONObject(new Gson().toJson(usuario));
    }
    public UsuarioClass ParseJsonObjectToUsuario(JSONObject jsonObject)
    {
        return (UsuarioClass)ParseJsonObject(jsonObject, UsuarioClass.class);
    }


    private Object ParseJsonObject(JSONObject jsonObject, Class<?> objClass)
    {
        return new Gson().fromJson(jsonObject.toString(), objClass);
    }
}
