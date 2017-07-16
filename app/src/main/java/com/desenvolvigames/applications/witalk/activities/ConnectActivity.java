package com.desenvolvigames.applications.witalk.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.desenvolvigames.applications.witalk.utilities.OpenActivity;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;

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
    public Context GetContext() {
        return this;
    }
    @Override
    public void ExecuteNotify(String tag, Object result) {
        Toast.makeText(ConnectActivity.this, tag+" Sincronizado!", Toast.LENGTH_SHORT).show();
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
                OpenActivity.onOpenActivity(ConnectActivity.this, LobbyActivity.class, null);
            }
        });
    }
    @Override
    protected void onSincronize() {
        ConstantsClass.Usuario.Sincronize(ConnectActivity.this);
    }


    @Override
    public void onBackPressed() {
        OpenActivity.onOpenActivity(ConnectActivity.this, AuthenticationActivity.class, null);
    }
}
