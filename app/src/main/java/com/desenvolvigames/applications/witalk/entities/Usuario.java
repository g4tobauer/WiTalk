package com.desenvolvigames.applications.witalk.entities;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.Toast;

import com.desenvolvigames.applications.witalk.R;
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
            if(user.getProviderId().equals(Resources.getSystem().getString(R.string.provider_firebaseid))){
                mFirebaseUser = user;
            }
            else if(user.getProviderId().equals(Resources.getSystem().getString(R.string.provider_facebookid))){
                mFacebookUser = user;
            }
        }
        if(mWiTalkFirebaseDatabaseManager == null)
            mWiTalkFirebaseDatabaseManager = new WiTalkFirebaseDatabaseManager(this);
        Init();
    }
    @Override
    protected void Init(){
        DatabaseReference ref = GetRef();
        ref.child(Resources.getSystem().getString(R.string.entity_usuario_firebaseuid)).setValue(mFirebaseUser.getUid());
        ref.child(Resources.getSystem().getString(R.string.entity_usuario_facebookuid)).setValue(mFacebookUser.getUid());
        ref.child(Resources.getSystem().getString(R.string.entity_usuario_name)).setValue(mFirebaseUser.getDisplayName());
        ref.child(Resources.getSystem().getString(R.string.entity_usuario_email)).setValue(mFirebaseUser.getEmail());
        ref.child(Resources.getSystem().getString(R.string.entity_usuario_imgurl)).setValue(mFirebaseUser.getPhotoUrl().toString());
        ref.child(Resources.getSystem().getString(R.string.entity_usuario_messagetoken)).setValue(getFirebaseInstanceMessageToken());
    }
    @Override
    public String GetRoot() {
        return getClass().getSimpleName().concat("/").concat(mFirebaseUser.getUid());
    }
    @Override
    public void Sincronize(IAsyncNotifiable asyncNotifiable){
        mAsyncNotifiable = asyncNotifiable;
        if(mIp!=null){mIp.Sincronize(mAsyncNotifiable);}
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
    public void ExecuteNotify(String tag, Object result) {
        mIp = ((Ip)result);
        mAsyncNotifiable.ExecuteNotify(tag, Usuario.this);
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
    public void setIpUsuario(String ipUsuario){GetRef().child(Resources.getSystem().getString(R.string.entity_usuario_ip)).setValue(ipUsuario);}
    public List<Contact> getLobbyList(){
        return mIp.getLobbyList();
    }
    private String getFirebaseInstanceMessageToken(){return FirebaseInstanceId.getInstance().getToken();}
    protected String getNomeUsuario(){return mFirebaseUser.getDisplayName();}
    protected String getAuthenticationId(){return mFirebaseUser.getUid();}
    protected String getIpUsuario(){return mDataSnapshot.child(Resources.getSystem().getString(R.string.entity_usuario_ip)).getValue(String.class);}
    protected String getUserMessageToken(){return mDataSnapshot.child(Resources.getSystem().getString(R.string.entity_usuario_messagetoken)).getValue(String.class);}
}
