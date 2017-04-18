package com.desenvolvigames.applications.witalk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.desenvolvigames.applications.witalk.control.JsonObjetcManagement;
import com.desenvolvigames.applications.witalk.entities.UsuarioClass;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    LoginButton loginButton;
    TextView textView;
    CallbackManager callbackManager;

    Button botaoTeste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IniciarControles();
        ConfigurarBotaoFacebook();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void IniciarControles()
    {
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);

        botaoTeste = (Button)findViewById(R.id.btn_teste);
        botaoTeste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                Toast.makeText(MainActivity.this, AccessToken.getCurrentAccessToken().getUserId(), Toast.LENGTH_LONG).show();
                UsuarioClass user = new UsuarioClass();
                teste(user);
            }
        });
    }
    private void ConfigurarBotaoFacebook()
    {
        loginButton = (LoginButton)findViewById(R.id.fb_login_bn);
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback()
                        {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response)
                            {
                                MainActivity.this.teste(new JsonObjetcManagement().ParseJsonObjectToUsuario(object));
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAsync();
            }
            @Override
            public void onCancel()
            {
                textView.setText("Login Cancelled");
            }
            @Override
            public void onError(FacebookException error)
            {
            }
        });
    }

    private void teste(UsuarioClass usuario)
    {
        JsonObjetcManagement management = new JsonObjetcManagement();
        JSONObject obj = null;
        try
        {
            obj = management.ParseUsuarioToJsonObject(usuario);
        }catch(Exception ex)
        {
            Log.i("teste", "Erro "+ex.getMessage());
        }
        if(obj != null)
        {
            UsuarioClass user = management.ParseJsonObjectToUsuario(obj);
            user = null;
        }
    }
}
