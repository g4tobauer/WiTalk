package com.desenvolvigames.applications.witalk.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.entities.Usuario;
import com.desenvolvigames.applications.witalk.fcm.authenticacion.WiTalkFirebaseAuthentication;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartActivity extends AppCompatActivity
{
    private Usuario mUsuario;
    private WiTalkFirebaseAuthentication mWiTalkFirebaseAuthentication;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("condition");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_start);
        IniciarFacebook();
        mWiTalkFirebaseAuthentication = new WiTalkFirebaseAuthentication(this);
        IniciarFirebaseDatabase();
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
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void IniciarFirebaseDatabase(){
        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object obj = dataSnapshot.getValue();
            }
            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }
    private void IniciarFacebook(){
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.fb_login_bn);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                Log.d("TAG", "facebook:onSuccess:" + loginResult);
//                handleFacebookAccessToken(loginResult.getAccessToken());
                mWiTalkFirebaseAuthentication.handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel(){Log.d("TAG", "facebook:onCancel");}
            @Override
            public void onError(FacebookException error){Log.d("TAG", "facebook:onError", error);}
        });
    }
}
