package com.desenvolvigames.applications.witalk.fcm.authenticacion;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.desenvolvigames.applications.witalk.activities.StartActivity;
import com.desenvolvigames.applications.witalk.entities.Usuario;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Joao on 29/04/2017.
 */

public class WiTalkFirebaseAuthentication
{
    private AccessTokenTracker mAccessTokenTracker;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private StartActivity mStartActivity;
    private Usuario mUsuario;

    public WiTalkFirebaseAuthentication(StartActivity startActivity){
        mStartActivity = startActivity;
        InitAuthentication();
    }

    private void InitAuthentication(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    if(mUsuario == null){
                        mUsuario = new Usuario(user.getProviderData());
                        mStartActivity.beginProgram();
                    }
                }
                else{
                    mUsuario = null;
                }
            }
        };
        mAccessTokenTracker = new AccessTokenTracker(){
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken){
                if (currentAccessToken == null){
                    if(FirebaseAuth.getInstance().getCurrentUser() != null)
                        FirebaseAuth.getInstance().signOut();
                }
            }
        };
    }

    public void startListener(){
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void stopListener(){
        if (mAuthListener != null){mAuth.removeAuthStateListener(mAuthListener);}
    }
    public void stopTracking(){
        mAccessTokenTracker.stopTracking();
    }
    public void handleFacebookAccessToken(AccessToken token){
        Log.d("TAG", "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mStartActivity, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        Log.d("TAG", "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()){
                            Log.w("TAG", "signInWithCredential", task.getException());
                            Toast.makeText(mStartActivity, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public Usuario getUsuario(){
        return mUsuario;
    }
}
