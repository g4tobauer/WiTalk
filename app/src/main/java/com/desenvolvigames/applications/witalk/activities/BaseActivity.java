package com.desenvolvigames.applications.witalk.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.desenvolvigames.applications.witalk.interfaces.IJsonNotifiable;

import org.json.JSONObject;

public class BaseActivity extends AppCompatActivity implements IJsonNotifiable
{
    protected JSONObject jsonParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void ExecuteNotify(String json){}

    @Override
    public String GetJsonParameters(){return null;}
}
