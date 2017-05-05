package com.desenvolvigames.applications.witalk.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.entities.Usuario;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;

public class ConnectActivity extends AppCompatActivity implements IAsyncNotifiable{
    private Button btnConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initControls();
        initEventos();
        sincronize();
    }

    private void initControls(){
        btnConnect = (Button) findViewById(R.id.btnConnect);
    }
    private void initEventos(){
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstantsClass.Usuario.connect();
            }
        });
    }
    private void sincronize(){
        ConstantsClass.Usuario.Sincronize(ConnectActivity.this);
    }

    @Override
    public Context GetContext() {
        return this;
    }
    @Override
    public void ExecuteNotify(String tag, String result) {
        switch (tag){
            case Usuario.SINCRONIZE:
                if(Usuario.SINCRONIZE.equals(result)){
                    Toast.makeText(ConnectActivity.this, "Usuario Sincronizado!", Toast.LENGTH_SHORT).show();
                    ConstantsClass.Usuario.Sincronize(ConnectActivity.this);
                }
                break;
            default:
                break;
        }
    }
}
