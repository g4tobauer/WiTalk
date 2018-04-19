package com.desenvolvigames.applications.witalk.entities;

import com.desenvolvigames.applications.witalk.fcm.database.WiTalkFirebaseDatabaseManager;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
        Init();
    }

    @Override
    protected void Init(){
        super.Init();
        Query query = mWiTalkFirebaseDatabaseManager.getRef().orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                    Usuario.this.initDefault();
//                else
//                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
//                            mWiTalkFirebaseDatabaseManager.getRef().child(issue.getKey()).setValue(issue.getValue());
//                    }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public String GetRoot() {
        return (USERNODE + "/" + mFirebaseUser.getUid());
    }

    @Override
    public void SincronizeNotifiable(IAsyncNotifiable asyncNotifiable){
        SyncTime(mWiTalkFirebaseDatabaseManager.getRef());
        mSyncNotifiable = asyncNotifiable;
        if(mIp!=null){mIp.SincronizeNotifiable(mSyncNotifiable);}
    }

    @Override
    protected void UpdateSnapshot(){
        if(mSyncNotifiable!=null)
            mSyncNotifiable.ExecuteNotify(Usuario.this.getClass().getSimpleName(), Usuario.this);
    }

    public void connect(IAsyncNotifiable asyncNotifiable){
        if(mIp == null)
        {
            mIp = new Ip();
        }else{
            mIp.Init();
        }
        SincronizeNotifiable(asyncNotifiable);
        mIsConnected = true;
    }

    public void disconnect(){
        if(mIp != null ) {
            mIp.disconnect();
            mIsConnected = false;
        }
    }

    public void setIpUsuario(String ipUsuario){
        mWiTalkFirebaseDatabaseManager.getRef().child(IpUsuario).setValue(ipUsuario);
    }

    public void setNomeUsuario(String nomeUsuario){
        DatabaseReference ref = mWiTalkFirebaseDatabaseManager.getRef();
        ref.child(Nome).setValue(nomeUsuario);
        if(mIp != null)
            mIp.setNomeUsuario(nomeUsuario);
    }

    public void setStatusUsuario(String statusUsuario){
        DatabaseReference ref = mWiTalkFirebaseDatabaseManager.getRef();
        ref.child(Status).setValue(statusUsuario);
        if(mIp != null)
            mIp.setStatusUsuario(statusUsuario);
    }

    public boolean isConnected(){
        return mIsConnected;
    }

    public List<Contact> getLobbyList(){
        return mIp.getLobbyList();
    }

    public String getAuthenticationId(){
        return mFirebaseUser.getUid();
    }

    public String getNomeUsuario(){
        String nome = mDataSnapshot.child(Nome).getValue(String.class);
        nome = nome == null?"":nome;
        if(nome.isEmpty())
            nome = mFirebaseUser.getDisplayName();
        return nome;
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

    private void initDefault(){
        DatabaseReference ref = mWiTalkFirebaseDatabaseManager.getRef();
        ref.child(FirebaseUid).setValue(mFirebaseUser.getUid());
        ref.child(FacebookUid).setValue(mFacebookUser.getUid());
        ref.child(Nome).setValue(mFirebaseUser.getDisplayName());
        ref.child(Status).setValue("Olá !");
        ref.child(Email).setValue(mFirebaseUser.getEmail());
        ref.child(UserImageSource).setValue(mFirebaseUser.getPhotoUrl().toString());
        ref.child(UserMessageToken).setValue(getFirebaseInstanceMessageToken());
    }
}
