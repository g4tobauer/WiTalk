package com.desenvolvigames.applications.witalk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.control.JsonObjetcManagement;
import com.desenvolvigames.applications.witalk.entities.Tab_Ip;
import com.desenvolvigames.applications.witalk.utilities.connection.GetAsyncTask;
import com.desenvolvigames.applications.witalk.utilities.connection.PostAsyncTask;
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
//    private LoginButton loginButton;
//    private CallbackManager callbackManager;
    private TextView txtView;

    @Override
    protected void onStop(){super.onStop();}

    @Override
    protected void onDestroy(){super.onDestroy();}

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        txtView = (TextView)findViewById(R.id.textView);
        BuscarIp();
//        IniciarControlesFacebook();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
    @Override
    public void ExecuteNotify(String tag, String json)
    {
        switch (tag)
        {
            case "GETIP":
                try
                {
                    JSONObject jsonObject = new JSONObject(json);
                    ConstantsClass.IpExterno = (String)jsonObject.get("ip");
                    PopularIp();
                    PostIp();
                    Log.i("RespostaGet", json);
                }catch(Exception ex)
                {
                    Log.i("RespostaGet", ex.getMessage());
                }
                break;
            case "POSTIP":
                Tab_Ip tabIp;
                tabIp = JsonObjetcManagement.ParseJsonObject(json, Tab_Ip.class);
                txtView.setText(tabIp.getIp());
                break;
        }
    }

    @Override
    public String GetJsonParameters()
    {
        return jsonParameter.toString();
    }
    @Override
    public void ClearParameters()
    {
        jsonParameter = null;
    }

//    private void IniciarControlesFacebook()
//    {
//        loginButton = (LoginButton)findViewById(R.id.fb_login_bn);
//        callbackManager = CallbackManager.Factory.create();
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
//        {
//            @Override
//            public void onSuccess(LoginResult loginResult)
//            {
//                Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG).show();
//            }
//            @Override
//            public void onCancel()
//            {
//                if(AccessToken.getCurrentAccessToken()!=null)
//                    LoginManager.getInstance().logOut();
//            }
//            @Override
//            public void onError(FacebookException error)
//            {
//                if(AccessToken.getCurrentAccessToken()!=null)
//                    LoginManager.getInstance().logOut();
//            }
//        });
//    }
    private void BuscarIp()
    {
        ConstantsClass.IpExterno = "";
        GetAsyncTask task = new GetAsyncTask(MainActivity.this);
        task.execute(ConstantsClass.GetIpUrl);
    }
    private void PostIp()
    {
        PostAsyncTask task = new PostAsyncTask(MainActivity.this);
        task.execute(ConstantsClass.PostIpObjectUrl);
    }
    private void PopularIp()
    {
        Tab_Ip tabIp = new Tab_Ip();
        tabIp.setIp(ConstantsClass.IpExterno);
        jsonParameter = JsonObjetcManagement.ParseObjectJson(tabIp);
    }
    private void teste(Bundle parameters)
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
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
