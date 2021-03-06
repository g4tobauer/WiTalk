package com.desenvolvigames.applications.witalk.interfaces;

import android.content.Context;

import com.desenvolvigames.applications.witalk.entities.Ip;
import com.desenvolvigames.applications.witalk.entities.Usuario;

import org.json.JSONObject;

/**
 * Created by Joao on 19/04/2017.
 */

public interface IAsyncNotifiable
{
    Context GetContext();
    void ExecuteNotify(String tag, Object result);
}
