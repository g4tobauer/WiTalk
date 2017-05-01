package com.desenvolvigames.applications.witalk.entities;

import android.os.AsyncTask;
import android.util.Log;

import com.desenvolvigames.applications.witalk.activities.ConnectActivity;
import com.desenvolvigames.applications.witalk.fcm.database.WiTalkFirebaseDatabaseManager;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.desenvolvigames.applications.witalk.interfaces.IDataBaseManageable;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;
import java.util.Objects;

/**
 * Created by Joao on 28/04/2017.
 */

public class Usuario implements IDataBaseManageable
{
    public static final String getIpUsuario = "getIpUsuario";
    public static final String getUserMessageToken = "getUserMessageToken";

    public UserInfo mFirebaseUser;
    public UserInfo mFacebookUser;

    private boolean mIsReleased;
    private IAsyncNotifiable mAsyncNotifiable;
    private DataSnapshot mDataSnapshot;
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
        mIsReleased = false;
        mWiTalkFirebaseDatabaseManager = new WiTalkFirebaseDatabaseManager(this);
        DatabaseReference ref = getUserRef();
        ref.child("FirebaseUid").setValue(mFirebaseUser.getUid());
        ref.child("FacebookUid").setValue(mFacebookUser.getUid());
        ref.child("Nome").setValue(mFirebaseUser.getDisplayName());
        ref.child("Email").setValue(mFirebaseUser.getEmail());
        ref.child("ImgUrl").setValue(mFirebaseUser.getPhotoUrl().toString());
        ref.child("UserMessageToken").setValue(getFirebaseInstanceMessageToken());
    }
    private DatabaseReference getUserRef(){
        return mWiTalkFirebaseDatabaseManager.getRef();
    }
    private String getFirebaseInstanceMessageToken(){
        return FirebaseInstanceId.getInstance().getToken();
    }
    public void setIpUsuario(String ipUsuario){
        getUserRef().child("IpUsuario").setValue(ipUsuario);
    }

    private String getIpUsuario(){
        mIsReleased = false;
        return mDataSnapshot.child("IpUsuario").getValue(String.class);
    }
    private String getUserMessageToken(){
        mIsReleased = false;
        return mDataSnapshot.child("UserMessageToken").getValue(String.class);
    }

    public void sincronize(IAsyncNotifiable asyncNotifiable, String tag){
        mAsyncNotifiable = asyncNotifiable;
        new UsuarioAsyncTask().execute(tag);
    }

    private boolean isReleased(){
        while (!mIsReleased) {
            try {
                Thread.sleep(100);
            } catch (Exception ex) {
                Log.w("Exception: ", ex);
            }
        }
        return mIsReleased;
    }

    @Override
    public String GetRoot() {
        return this.getClass().getSimpleName().concat("/").concat(mFirebaseUser.getUid());
    }
    @Override
    public void DataSnapshotUpdate(DataSnapshot dataSnapshot) {
        mIsReleased = true;
        mDataSnapshot = dataSnapshot;
    }


    private class UsuarioAsyncTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            String[] arResult = new String[2];
            String tag = params[0];
            String result = null;
            if (isReleased()) {
                switch (tag) {
                    case getIpUsuario:
                        result = getIpUsuario();
                        break;
                    case getUserMessageToken:
                        result = getUserMessageToken();
                        break;
                }
            }
            arResult[0] = tag;
            arResult[1] = result;
            return arResult;
        }

        @Override
        protected void onPostExecute(String... result) {
            super.onPostExecute(result);
            mAsyncNotifiable.ExecuteNotify(result[0], result[1]);
        }
    }
}
