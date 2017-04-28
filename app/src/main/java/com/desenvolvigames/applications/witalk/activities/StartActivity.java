package com.desenvolvigames.applications.witalk.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.desenvolvigames.applications.witalk.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    TextView mTextView;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("condition");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_start);
        mTextView = (TextView)findViewById(R.id.textView);
        IniciarFacebook();
        IniciarFirebaseAuth();
        IniciarFirebaseDatabase();
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop()
    {
        super.onStop();
        if (mAuthListener != null){mAuth.removeAuthStateListener(mAuthListener);}
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void IniciarFirebaseAuth()
    {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }
    private void IniciarFirebaseDatabase()
    {
        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                mTextView.setText(text);
            }
            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }
    private void IniciarFacebook()
    {
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.fb_login_bn);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                Log.d("TAG", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel(){Log.d("TAG", "facebook:onCancel");}
            @Override
            public void onError(FacebookException error){Log.d("TAG", "facebook:onError", error);}
        });
        accessTokenTracker = new AccessTokenTracker()
        {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
            {
                if (currentAccessToken == null)
                {
                    if(FirebaseAuth.getInstance().getCurrentUser() != null)
                        FirebaseAuth.getInstance().signOut();
                }
            }
        };
    }
    private void handleFacebookAccessToken(AccessToken token)
    {
        Log.d("TAG", "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        Log.d("TAG", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful())
                        {
                            Log.w("TAG", "signInWithCredential", task.getException());
                            Toast.makeText(StartActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }
}
