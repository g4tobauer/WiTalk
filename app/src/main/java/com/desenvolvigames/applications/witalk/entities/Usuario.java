package com.desenvolvigames.applications.witalk.entities;

import com.desenvolvigames.applications.witalk.fcm.database.WiTalkFirebaseDatabaseManager;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

/**
 * Created by Joao on 28/04/2017.
 */

public class Usuario
{
    public UserInfo mFirebaseUser;
    public UserInfo mFacebookUser;
    public String mIpAddress;

    private WiTalkFirebaseDatabaseManager mWiTalkFirebaseDatabaseManager;

    public <T extends UserInfo>  Usuario(List<T> list){
        for (UserInfo user : list){
            if(user.getProviderId().equals("firebase")){
                mFirebaseUser = user;
            }
            else if(user.getProviderId().equals("facebook.com")){
                mFacebookUser = user;
            }
        }
        init();
    }
    private void init(){
        DatabaseReference ref = getRef();
        ref.child("FirebaseUid").setValue(mFirebaseUser.getUid());
        ref.child("FacebookUid").setValue(mFacebookUser.getUid());
        ref.child("Nome").setValue(mFirebaseUser.getDisplayName());
        ref.child("Email").setValue(mFirebaseUser.getEmail());
        ref.child("ImgUrl").setValue(mFirebaseUser.getPhotoUrl().toString());
        ref.child("UserMessageToken").setValue(getUserMessageToken());
    }
    private DatabaseReference getRef(){
        mWiTalkFirebaseDatabaseManager = new WiTalkFirebaseDatabaseManager(this.getClass().getSimpleName());
        return mWiTalkFirebaseDatabaseManager.getRef(mFirebaseUser.getUid());
    }

    public void setIpUsuario(String ipUsuario){
        DatabaseReference ref = getRef();
        ref.child("IpUsuario").setValue(ipUsuario);
    }

    public String getUserMessageToken(){
        return FirebaseInstanceId.getInstance().getToken();
    }
}
