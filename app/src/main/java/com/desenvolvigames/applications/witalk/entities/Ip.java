package com.desenvolvigames.applications.witalk.entities;

import android.os.AsyncTask;

import com.desenvolvigames.applications.witalk.fcm.database.WiTalkFirebaseDatabaseManager;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Joao on 01/05/2017.
 */

public class Ip extends EntityBase {

    private static final String IPNODE = "IpNode";
    private String mIp;

    public Ip(){
        mIsReleased = true;
        mIp = ConstantsClass.Usuario.getIpUsuario().replace(".","");
        mWiTalkFirebaseDatabaseManager = new WiTalkFirebaseDatabaseManager(this);
        Init();
    }

    private class IpAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String tag = null;
            if (!IsReleased()) {
                tag = "false";
            }
            return tag;
        }
        protected void onPostExecute(String tag) {
            super.onPostExecute(tag);
            mAsyncNotifiable.ExecuteNotify(Ip.this.getClass().getSimpleName(), Ip.this);
            if(load !=null)
                load.dismiss();
            load = null;
        }
    }

    @Override
    protected void Init(){
        DatabaseReference ref = GetRef().child(ConstantsClass.Usuario.getAuthenticationId());
        ref.child("Nome").setValue(ConstantsClass.Usuario.getNomeUsuario());
        ref.child("UserMessageToken").setValue(ConstantsClass.Usuario.getUserMessageToken());
    }
    @Override
    public void Sincronize(IAsyncNotifiable asyncNotifiable, String syncAction) {
        mIsReleased = false;
        mAsyncNotifiable = asyncNotifiable;
        if(mAsyncTask!=null){mAsyncTask.cancel(true);}
        mAsyncTask = new IpAsyncTask();
        mAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    @Override
    public String GetRoot() {
        return IPNODE.concat("/").concat(mIp);
    }
}
