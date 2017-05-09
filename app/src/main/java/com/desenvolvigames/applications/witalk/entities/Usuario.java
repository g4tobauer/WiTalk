package com.desenvolvigames.applications.witalk.entities;

import android.os.AsyncTask;
import android.widget.Toast;

import com.desenvolvigames.applications.witalk.fcm.database.WiTalkFirebaseDatabaseManager;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

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
        mWiTalkFirebaseDatabaseManager = new WiTalkFirebaseDatabaseManager(this);
        Init();
    }

    private class UsuarioAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String tag = null;
            if (!IsReleased()) {
                tag = "false";
            }
            return tag;
        }
        @Override
        protected void onPostExecute(String tag) {
            super.onPostExecute(tag);
            mAsyncNotifiable.ExecuteNotify(Usuario.this.getClass().getSimpleName(), Usuario.this);
            if(load !=null)
                load.dismiss();
            load = null;
        }
    }

    private String getFirebaseInstanceMessageToken(){return FirebaseInstanceId.getInstance().getToken();}

    public void connect(){
        if(mIp == null){
            mIp = new Ip();
        }else{
            mIp.Init();
        }
        if(mIp.mIsReleased){
            mIp.Sincronize(this, null);
        }
    }
    public void setIpUsuario(String ipUsuario){
        GetRef().child("IpUsuario").setValue(ipUsuario);
    }
    public String getNomeUsuario(){return mFirebaseUser.getDisplayName();}
    public String getAuthenticationId(){return mFirebaseUser.getUid();}
    public String getIpUsuario(){return mDataSnapshot.child("IpUsuario").getValue(String.class);}
    public String getUserMessageToken(){return mDataSnapshot.child("UserMessageToken").getValue(String.class);}

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
    public void Sincronize(IAsyncNotifiable asyncNotifiable, String syncAction){
        mIsReleased = false;
        mAsyncNotifiable = asyncNotifiable;

        if(mAsyncTask!=null){mAsyncTask.cancel(true);}

        mAsyncTask = new UsuarioAsyncTask();
        mAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        if(mIp!=null){mIp.Sincronize(this, null);}
    }
    @Override
    public void ExecuteNotify(String tag, Object result) {
        mAsyncNotifiable.ExecuteNotify(tag, result);
    }

    @Override
    public void ForceRelease() {
        if(mIp!=null){mIp.ForceRelease();}
        super.ForceRelease();
    }
    @Override
    public String GetRoot() {
        return this.getClass().getSimpleName().concat("/").concat(mFirebaseUser.getUid());
    }
}
