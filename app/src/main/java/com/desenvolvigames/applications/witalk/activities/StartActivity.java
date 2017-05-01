package com.desenvolvigames.applications.witalk.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.activities.old.BaseActivity;
import com.desenvolvigames.applications.witalk.entities.Usuario;
import com.desenvolvigames.applications.witalk.fcm.authenticacion.WiTalkFirebaseAuthentication;
import com.desenvolvigames.applications.witalk.interfaces.IJsonNotifiable;
import com.desenvolvigames.applications.witalk.utilities.connection.GetAsyncTask;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

public class StartActivity extends AppCompatActivity implements IJsonNotifiable
{
    private Usuario mUsuario;
    private WiTalkFirebaseAuthentication mWiTalkFirebaseAuthentication;
    private LoginButton mLoginButton;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_start);
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getActiveNetworkInfo();
        if (mWifi.isConnected()) {
            IniciarFacebook();
            mWiTalkFirebaseAuthentication = new WiTalkFirebaseAuthentication(this);
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        mWiTalkFirebaseAuthentication.startListener();
    }
    @Override
    protected void onStop(){
        super.onStop();
        mWiTalkFirebaseAuthentication.stopListener();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mWiTalkFirebaseAuthentication.stopTracking();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public String GetJsonParameters() {
        return null;
    }
    @Override
    public void ClearParameters() {

    }
    @Override
    public void ExecuteNotify(String tag, String json) {
        try {
            switch (tag) {
                case ConstantsClass.GetIpUrl:
                    ConstantsClass.IpExterno = new JSONObject(json).getString("ip");
                    mUsuario.setIpUsuario(ConstantsClass.IpExterno);
                    break;
            }
        }catch (Exception ex){
            Log.w("TAG", "signInWithCredential", ex);
        }
    }
    @Override
    public Context GetContext() {
        return this;
    }

    private void IniciarFacebook(){
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton = (LoginButton)findViewById(R.id.fb_login_bn);
        mLoginButton.setReadPermissions("email", "public_profile");
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>(){
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                Log.d("TAG", "facebook:onSuccess:" + loginResult);
                mWiTalkFirebaseAuthentication.handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel(){Log.d("TAG", "facebook:onCancel");}
            @Override
            public void onError(FacebookException error){Log.d("TAG", "facebook:onError", error);}
        });
    }
    public void teste(){
        mUsuario = mWiTalkFirebaseAuthentication.getUsuario();
        ConstantsClass.IpExterno = "";
        GetAsyncTask task = new GetAsyncTask(StartActivity.this);
        task.execute(ConstantsClass.GetIpUrl);
        task.execute();
    }
}
