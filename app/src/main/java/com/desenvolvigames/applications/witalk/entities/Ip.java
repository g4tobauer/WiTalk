package com.desenvolvigames.applications.witalk.entities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.desenvolvigames.applications.witalk.fcm.database.WiTalkFirebaseDatabaseManager;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Joao on 01/05/2017.
 */

public class Ip extends EntityBase {

    private static final String IpNode = "IpNode";
    private String mIp;
    public static final String sincronize = "IpSincronize";

    public Ip(){
        mIp = ConstantsClass.Usuario.getIpUsuario().replace(".","");
        Init();
    }

    private class IpAsyncTask extends AsyncTask<String, Void, String> {
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
        protected void onPostExecute(String tag) {
            super.onPostExecute(tag);
            mAsyncNotifiable.ExecuteNotify(tag, sincronize);
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

    @Override
    protected void Init(){
        mIsReleased = false;
        mWiTalkFirebaseDatabaseManager = new WiTalkFirebaseDatabaseManager(this);
        DatabaseReference ref = GetRef();
        ref.child(ConstantsClass.Usuario.getAuthenticationId()).setValue("teste");
    }
    @Override
    public void Sincronize(IAsyncNotifiable asyncNotifiable, String tag) {
        mAsyncNotifiable = asyncNotifiable;
        new IpAsyncTask().execute(tag);
    }
    @Override
    public void ExecuteNotify(String tag, String result) {
    }
    @Override
    public String GetRoot() {
        return IpNode.concat("/").concat(mIp);
    }

}
