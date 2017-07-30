package com.desenvolvigames.applications.witalk.fcm.authenticacion;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.activities.AuthenticationActivity;
import com.desenvolvigames.applications.witalk.entities.Usuario;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;
import com.facebook.AccessToken;
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
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private AuthenticationActivity mAuthenticationActivity;
    private Usuario mUsuario;

    public WiTalkFirebaseAuthentication(AuthenticationActivity authenticationActivity){
        mAuthenticationActivity = authenticationActivity;
        onInitAuthentication();
    }

    private void onInitAuthentication(){
        mAuth = FirebaseAuth.getInstance();
        onInitListeners();
    }
    private void onInitListeners(){
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                {
                    if(mUsuario == null)
                    {
                        mUsuario = new Usuario(user.getProviderData());
                        if(ConstantsClass.Usuario == null)
                            ConstantsClass.Usuario = mUsuario;
//                        onBeginProgram();
                    }
                }
                else
                {
                    mUsuario = null;
                }
            }
        };
    }


    public void onStartListener(){
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void onStopListener(){
        if (mAuthListener != null){mAuth.removeAuthStateListener(mAuthListener);}
    }
    public void onHandleFacebookAccessToken(AccessToken token){
        Log.d(mAuthenticationActivity.getString(R.string.app_name), "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(mAuthenticationActivity,
                new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        Log.d(mAuthenticationActivity.getString(R.string.app_name), "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful())
                        {
                            Log.w(mAuthenticationActivity.getString(R.string.app_name), "signInWithCredential", task.getException());
                            Toast.makeText(mAuthenticationActivity, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void signOut(){
        mAuth.signOut();
    }
    public FirebaseUser getCurrentUser(){
        return mAuth.getCurrentUser();
    }
}
