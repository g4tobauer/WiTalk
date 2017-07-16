package com.desenvolvigames.applications.witalk.activities.old;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.utilities.JsonObjetcManagement;
import com.desenvolvigames.applications.witalk.entities.old.Tab_Ip;
import com.desenvolvigames.applications.witalk.entities.old.Tab_Usuario;
import com.desenvolvigames.applications.witalk.interfaces.IParameterNotifiable;
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

public class BaseActivity extends AppCompatActivity implements IParameterNotifiable
{
    private Tab_Ip _tabIp;
    private Tab_Usuario _tabUsuario;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private JSONObject jsonParameter;

    @Override
    protected void onStop(){super.onStop();}
    @Override
    protected void onDestroy(){super.onDestroy();}
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        IniciarControlesFacebook();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void ExecuteNotify(String tag, Object result){
        switch (tag)
        {
            case ConstantsClass.GetIpUrl:
                try
                {
                    PostIp((String)new JSONObject((String)result).get("ip"));
                    Log.i("RespostaGet", (String)result);
                }catch(Exception ex)
                {
                    Log.i("RespostaGet", ex.getMessage());
                }
                break;
            case ConstantsClass.PostIpObjectUrl:
                _tabIp = JsonObjetcManagement.ParseJsonObject((String)result, Tab_Ip.class);
                if(ConstantsClass.IpExterno.equals(_tabIp.getIp()))
                {
                    _tabUsuario.fk_int_IdIp = _tabIp.getPkIP();
                    PostUsuario();
                }
                break;
            case ConstantsClass.PostUsuarioObjectUrl:
                _tabUsuario = JsonObjetcManagement.ParseJsonObject((String)result, Tab_Usuario.class);
                _tabUsuario = null;
                break;
        }
    }
    @Override
    public void ClearParameters()
    {
        jsonParameter = null;
    }
    @Override
    public String GetParameters() {return jsonParameter.toString();}
    @Override
    public Context GetContext() {
        return this;
    }

    private void IniciarControlesFacebook(){
        loginButton = (LoginButton)findViewById(R.id.fb_login_bn);
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                GetUsuario(parameters);
            }
            @Override
            public void onCancel()
            {
                if(AccessToken.getCurrentAccessToken()!=null)
                    LoginManager.getInstance().logOut();
            }
            @Override
            public void onError(FacebookException error)
            {
                if(AccessToken.getCurrentAccessToken()!=null)
                    LoginManager.getInstance().logOut();
            }

            private void GetUsuario(Bundle parameters)
            {
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback()
                        {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response)
                            {
                                try
                                {
                                    String key = object.getString("id");
                                    String name = object.getString("name");
                                    BaseActivity.this._tabUsuario = new Tab_Usuario();
                                    BaseActivity.this._tabUsuario.str_UsuarioKEY = key;
                                    BaseActivity.this._tabUsuario.str_NomeUsuario = name;
                                    BaseActivity.this.BuscarIp();
                                }catch(Exception ex)
                                {
                                    Log.i("registerCallback",ex.getMessage());
                                }
                            }
                        });
                request.setParameters(parameters);
                request.executeAsync();
            }
        });
    }
    private void BuscarIp(){
        ConstantsClass.IpExterno = "";
        GetAsyncTask task = new GetAsyncTask(BaseActivity.this);
        task.execute(ConstantsClass.GetIpUrl);
    }
    private void PostIp(String ip){
        ConstantsClass.IpExterno = ip;
        Tab_Ip tabIp = new Tab_Ip();
        tabIp.setIp(ConstantsClass.IpExterno);
        jsonParameter = JsonObjetcManagement.ParseObjectJson(tabIp);
        PostAsyncTask task = new PostAsyncTask(BaseActivity.this);
        task.execute(ConstantsClass.PostIpObjectUrl);
    }
    private void PostUsuario(){
        jsonParameter = JsonObjetcManagement.ParseObjectJson(_tabUsuario);
        PostAsyncTask task = new PostAsyncTask(BaseActivity.this);
        task.execute(ConstantsClass.PostUsuarioObjectUrl);
    }
}
