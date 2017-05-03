package com.desenvolvigames.applications.witalk.entities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
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

    public static final String sincronize = "UserSincronize";
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
        Init();
    }

    private class UsuarioAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String tag = params[0];
            if (IsReleased()) {
                switch (tag) {
                    case sincronize:
                        publishProgress();
                        break;
                }
            }
            return tag;
        }
        @Override
        protected void onPostExecute(String tag) {
            super.onPostExecute(tag);
            mAsyncNotifiable.ExecuteNotify(tag, Usuario.sincronize);
            if(load !=null)
                load.dismiss();
            load = null;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.i("onProgressUpdate", "Exibindo ProgressDialog na tela Thread: " + Thread.currentThread().getName());
            if(load == null)
                load = ProgressDialog.show(mAsyncNotifiable.GetContext(), "Por favor Aguarde ...","Sincronizando ...",true, false);
            load.setMessage("Sincronizando ... ");
        }
    }

    private String getFirebaseInstanceMessageToken(){
        return FirebaseInstanceId.getInstance().getToken();
    }

    public void connect(){
        mIp = new Ip();
        mIp.Sincronize(this, Ip.sincronize);
    }
    public void setIpUsuario(String ipUsuario){
        GetRef().child("IpUsuario").setValue(ipUsuario);
    }
    public String getAuthenticationId(){
        return mFirebaseUser.getUid();
    }
    public String getIpUsuario(){
        mIsReleased = false;
        return mDataSnapshot.child("IpUsuario").getValue(String.class);
    }
    public String getUserMessageToken(){
        mIsReleased = false;
        return mDataSnapshot.child("UserMessageToken").getValue(String.class);
    }

    @Override
    protected void Init(){
        mIsReleased = false;
        mWiTalkFirebaseDatabaseManager = new WiTalkFirebaseDatabaseManager(this);
        DatabaseReference ref = GetRef();
        ref.child("FirebaseUid").setValue(mFirebaseUser.getUid());
        ref.child("FacebookUid").setValue(mFacebookUser.getUid());
        ref.child("Nome").setValue(mFirebaseUser.getDisplayName());
        ref.child("Email").setValue(mFirebaseUser.getEmail());
        ref.child("ImgUrl").setValue(mFirebaseUser.getPhotoUrl().toString());
        ref.child("UserMessageToken").setValue(getFirebaseInstanceMessageToken());
    }
    @Override
    public void Sincronize(IAsyncNotifiable asyncNotifiable, String tag){
        mAsyncNotifiable = asyncNotifiable;
        new UsuarioAsyncTask().execute(tag);
    }
    @Override
    public void ExecuteNotify(String tag, String result) {
        switch (tag){
            case Ip.sincronize:
                Toast.makeText(mAsyncNotifiable.GetContext(), "Ip Sincronizado!", Toast.LENGTH_SHORT);
                break;
        }
    }
    @Override
    public String GetRoot() {
        return this.getClass().getSimpleName().concat("/").concat(mFirebaseUser.getUid());
    }
}
