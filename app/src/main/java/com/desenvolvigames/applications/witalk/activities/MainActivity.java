package com.desenvolvigames.applications.witalk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.utilities.connection.GetAsyncTask;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

public class MainActivity extends BaseActivity
{
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
//        if(AccessToken.getCurrentAccessToken()!=null)
//            LoginManager.getInstance().logOut();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        BuscarIp();
        loginButton = (LoginButton)findViewById(R.id.fb_login_bn);
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
            }
            @Override
            public void onCancel()
            {
            }
            @Override
            public void onError(FacebookException error)
            {
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
//        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
    @Override
    public void ExecuteNotify(String json)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            ConstantsClass.IPEXTERNO = (String)jsonObject.get("ip");
            Log.i("RespostaGet", json);
        }catch(Exception ex)
        {
            Log.i("RespostaGet", ex.getMessage());
        }
    }

    private void BuscarIp()
    {
        ConstantsClass.IPEXTERNO = "";
        GetAsyncTask task = new GetAsyncTask(MainActivity.this);
        task.execute(ConstantsClass.GETIPURL);
    }

    private void teste()
    {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response)
                    {
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
