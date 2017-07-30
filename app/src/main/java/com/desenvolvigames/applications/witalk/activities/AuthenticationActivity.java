package com.desenvolvigames.applications.witalk.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.fcm.authenticacion.WiTalkFirebaseAuthentication;
import com.desenvolvigames.applications.witalk.utilities.OpenActivity;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class AuthenticationActivity extends BaseActivity //implements IAsyncNotifiable
{
    private WiTalkFirebaseAuthentication mWiTalkFirebaseAuthentication;
    private Button btn_Entrar;
    private LoginButton mLoginButton;
    private CallbackManager mCallbackManager;
    private AccessTokenTracker mAccessTokenTracker;

    @Override
    protected void onInitControls() {
        btn_Entrar = (Button) findViewById(R.id.btn_Entrar);
        mLoginButton = (LoginButton)findViewById(R.id.fb_login_bn);
        mLoginButton.setReadPermissions(getString(R.string.facebook_app_permission_email), getString(R.string.facebook_app_permission_publicprofile));
        mCallbackManager = CallbackManager.Factory.create();
    }
    @Override
    protected void onInitEvents() {
        btn_Entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ConstantsClass.Usuario != null)
                    OpenActivity.onCloseAndOpenActivity(AuthenticationActivity.this, ConnectActivity.class, null);
                else
                    Toast.makeText(AuthenticationActivity.this, "Usuário não encontrado!", Toast.LENGTH_SHORT).show();
            }
        });
        onCallback();
        mAccessTokenTracker = new AccessTokenTracker(){
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
            {
                if (currentAccessToken == null)
                {
                    if(mWiTalkFirebaseAuthentication.getCurrentUser() != null)
                    {
                        mWiTalkFirebaseAuthentication.signOut();
                        ConstantsClass.Usuario = null;
                    }
                }
            }
        };
    }
    @Override
    protected void onSincronize() {
        mWiTalkFirebaseAuthentication = new WiTalkFirebaseAuthentication(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_authentication);

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getActiveNetworkInfo();
        if (mWifi.isConnected()) {
            onInitControls();
            onInitEvents();
            onSincronize();
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        mWiTalkFirebaseAuthentication.onStartListener();
    }
    @Override
    protected void onStop(){
        super.onStop();
        mWiTalkFirebaseAuthentication.onStopListener();
    }

    @Override
    protected void onDestroy(){
        mAccessTokenTracker.stopTracking();
        super.onDestroy();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void onCallback(){
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>(){
            @Override
            public void onSuccess(LoginResult loginResult){
                Log.d(getString(R.string.app_name), getString(R.string.facebook_app_message_success) + loginResult);
                mWiTalkFirebaseAuthentication.onHandleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel(){Log.d(getString(R.string.app_name), getString(R.string.facebook_app_message_cancel));}
            @Override
            public void onError(FacebookException error){Log.d(getString(R.string.app_name), getString(R.string.facebook_app_message_error), error);}
        });
    }
}
