package com.desenvolvigames.applications.witalk.activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.desenvolvigames.applications.witalk.utilities.OpenActivity;
import com.desenvolvigames.applications.witalk.utilities.connection.GetAsyncTask;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;

import org.json.JSONObject;

public class ConnectActivity extends BaseActivity implements IAsyncNotifiable{
    private Button _btnConnect;
    private Toolbar _toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        onInitControls();
        onInitEvents();
        onSincronize();
    }
    @Override
    protected void onInitControls() {
        _btnConnect = (Button) findViewById(R.id.btnConnect);
        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(_toolbar);
    }
    @Override
    protected void onInitEvents() {
        _btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConnectIp();
            }
        });
    }
    @Override
    protected void onSincronize() {
        ConstantsClass.Usuario.SincronizeNotifiable(ConnectActivity.this);
    }

    @Override
    public Context GetContext() {
        return this;
    }
    @Override
    public void ExecuteNotify(String tag, Object result) {
        try{
            ConstantsClass.IpExterno = new JSONObject((String)result).getString(getString(R.string.entity_ip).toLowerCase()).replace(".","x");
            ConstantsClass.Usuario.setIpUsuario(ConstantsClass.IpExterno);
            OpenActivity.onCloseAndOpenActivity(ConnectActivity.this, LobbyActivity.class, null);
        }catch (Exception ex){
            Log.w("TAG", ex);
        }
    }

    @Override
    public void onBackPressed() {
        OpenActivity.onCloseAndOpenActivity(ConnectActivity.this, AuthenticationActivity.class, null);
    }

    private void onConnectIp(){
        ConstantsClass.IpExterno = "";
        GetAsyncTask task = new GetAsyncTask(ConnectActivity.this);
        task.execute(ConstantsClass.GetIpUrl);
    }
}
