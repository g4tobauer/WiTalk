package com.desenvolvigames.applications.witalk.entities;

import android.os.AsyncTask;
import android.widget.Toast;

import com.desenvolvigames.applications.witalk.fcm.database.WiTalkFirebaseDatabaseManager;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;
import java.util.Map;

/**
 * Created by Joao on 28/04/2017.
 */

public class Usuario extends EntityBase{

    private UserInfo mFirebaseUser;
    private UserInfo mFacebookUser;
    private Ip mIp;

    public <T extends UserInfo>  Usuario(List<T> list){
        for (UserInfo user : list){
            if(user.getProviderId().equals("firebase")){
                mFirebaseUser = user;
            }
            else if(user.getProviderId().equals("facebook.com")){
                mFacebookUser = user;
            }
        }
        if(mWiTalkFirebaseDatabaseManager == null)
            mWiTalkFirebaseDatabaseManager = new WiTalkFirebaseDatabaseManager(this);
        Init();
    }

    @Override
    protected void UpdateSnapshot(){
        if(mAsyncNotifiable!=null)
            mAsyncNotifiable.ExecuteNotify(Usuario.this.getClass().getSimpleName(), Usuario.this);
    }
    @Override
    public DatabaseReference GetIpLobbyReference() {
        return mIp.GetIpLobbyReference();
    }
    @Override
    protected void Init(){
        DatabaseReference ref = GetRef();
        ref.child("FirebaseUid").setValue(mFirebaseUser.getUid());
        ref.child("FacebookUid").setValue(mFacebookUser.getUid());
        ref.child("Nome").setValue(mFirebaseUser.getDisplayName());
        ref.child("Email").setValue(mFirebaseUser.getEmail());
        ref.child("ImgUrl").setValue(mFirebaseUser.getPhotoUrl().toString());
        ref.child("UserMessageToken").setValue(getFirebaseInstanceMessageToken());
    }
    @Override
    public void Sincronize(IAsyncNotifiable asyncNotifiable){
        mAsyncNotifiable = asyncNotifiable;
        if(mIp!=null){mIp.Sincronize(mAsyncNotifiable);}
    }
    @Override
    public void ExecuteNotify(String tag, Object result) {
        mIp = ((Ip)result);
        mAsyncNotifiable.ExecuteNotify(tag, Usuario.this);
    }
    @Override
    public String GetRoot() {
        return this.getClass().getSimpleName().concat("/").concat(mFirebaseUser.getUid());
    }

    public void connect(){
        if(mIp == null){
            mIp = new Ip();
        }else{
            SyncTime(null);
            mIp.Init();
        }
        Sincronize(mAsyncNotifiable);
    }
    public void setIpUsuario(String ipUsuario){GetRef().child("IpUsuario").setValue(ipUsuario);}
    public List<Contact> getLobbyList(){
        return mIp.getLobbyList();
    }
    private String getFirebaseInstanceMessageToken(){return FirebaseInstanceId.getInstance().getToken();}
    protected String getNomeUsuario(){return mFirebaseUser.getDisplayName();}
    protected String getAuthenticationId(){return mFirebaseUser.getUid();}
    protected String getIpUsuario(){return mDataSnapshot.child("IpUsuario").getValue(String.class);}
    protected String getUserMessageToken(){return mDataSnapshot.child("UserMessageToken").getValue(String.class);}
}
