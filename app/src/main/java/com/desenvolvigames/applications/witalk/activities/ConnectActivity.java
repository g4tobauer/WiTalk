package com.desenvolvigames.applications.witalk.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.entities.Usuario;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;

public class ConnectActivity extends AppCompatActivity implements IAsyncNotifiable{
    private TextView teste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        teste = (TextView) findViewById(R.id.txtTeste);
        ConstantsClass.Usuario.sincronize(ConnectActivity.this, ConstantsClass.Usuario.getIpUsuario);
//        if(user.isReleased());

//        String teste = user.getIpUsuario();
//        Log.d("ERRO",teste);
//        teste = null;
    }

    @Override
    public Context GetContext() {
        return this;
    }

    @Override
    public void ExecuteNotify(String tag, String result) {
        switch (tag){
            case Usuario.getIpUsuario:
                String ip = result;
                teste.setText(ip);
                break;
        }
    }
}
