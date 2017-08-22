package com.desenvolvigames.applications.witalk.entities;

import com.desenvolvigames.applications.witalk.fcm.database.WiTalkFirebaseDatabaseManager;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

import static com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass.Email;
import static com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass.FacebookUid;
import static com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass.FirebaseUid;
import static com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass.IpUsuario;
import static com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass.Nome;
import static com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass.ProviderFacebookId;
import static com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass.ProviderFirebaseId;
import static com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass.Status;
import static com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass.UserImageSource;
import static com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass.UserMessageToken;

/**
 * Created by Joao on 28/04/2017.
 */

public class Usuario extends EntityBase{

    private final String USERNODE =  (getClass().getSimpleName() + NODE).toUpperCase();
    private UserInfo mFirebaseUser;
    private UserInfo mFacebookUser;
    private Ip mIp;
    private boolean mIsConnected;

    public <T extends UserInfo>  Usuario(List<T> list){
        for (UserInfo user : list)
        {
            if(user.getProviderId().equals(ProviderFirebaseId))
            {
                mFirebaseUser = user;
            }
            else if(user.getProviderId().equals(ProviderFacebookId))
            {
                mFacebookUser = user;
            }
        }
        if(mWiTalkFirebaseDatabaseManager == null)
            mWiTalkFirebaseDatabaseManager = new WiTalkFirebaseDatabaseManager(this);
        Init();
    }

    @Override
    protected void Init(){
        DatabaseReference ref = mWiTalkFirebaseDatabaseManager.getRef();
        ref.child(FirebaseUid).setValue(mFirebaseUser.getUid());
        ref.child(FacebookUid).setValue(mFacebookUser.getUid());
        ref.child(Nome).setValue(mFirebaseUser.getDisplayName());
        ref.child(Status).setValue("Olá !");
        ref.child(Email).setValue(mFirebaseUser.getEmail());
        ref.child(UserImageSource).setValue(mFirebaseUser.getPhotoUrl().toString());
        ref.child(UserMessageToken).setValue(getFirebaseInstanceMessageToken());
    }

    @Override
    public String GetRoot() {
        return (USERNODE + "/" + mFirebaseUser.getUid());
    }

    @Override
    public void Sincronize(IAsyncNotifiable asyncNotifiable){
        SyncTime(mWiTalkFirebaseDatabaseManager.getRef());
        mAsyncNotifiable = asyncNotifiable;
        if(mIp!=null){mIp.Sincronize(mAsyncNotifiable);}
    }

    @Override
    protected void UpdateSnapshot(){
        if(mAsyncNotifiable!=null)
            mAsyncNotifiable.ExecuteNotify(Usuario.this.getClass().getSimpleName(), Usuario.this);
    }

    public void connect(IAsyncNotifiable asyncNotifiable){
        if(mIp == null)
        {
            mIp = new Ip();
        }else{
            mIp.Init();
        }
        Sincronize(asyncNotifiable);
        mIsConnected = true;
    }

    public void disconnect(){
        if(mIp != null ) {
            mIp.disconnect();
            mIsConnected = false;
        }
    }

    public boolean isConnected(){
        return mIsConnected;
    }

    public void setIpUsuario(String ipUsuario){
        mWiTalkFirebaseDatabaseManager.getRef().child(IpUsuario).setValue(ipUsuario);
    }

    public List<Contact> getLobbyList(){
        return mIp.getLobbyList();
    }

    public String getAuthenticationId(){
        return mFirebaseUser.getUid();
    }

    public String getNomeUsuario(){
        return mFirebaseUser.getDisplayName();
    }

    public String getUserMessageToken(){
        return mDataSnapshot.child(UserMessageToken).getValue(String.class);
    }

    public String getImgUrl(){
        return mDataSnapshot.child(UserImageSource).getValue(String.class);
    }

    public String getStatus(){
        return mDataSnapshot.child(Status).getValue(String.class);
    }

    protected String getIpUsuario(){
        return mDataSnapshot.child(IpUsuario).getValue(String.class);
    }

    private String getFirebaseInstanceMessageToken(){
        return FirebaseInstanceId.getInstance().getToken();
    }
}
