package com.desenvolvigames.applications.witalk.entities;

import android.os.AsyncTask;

import com.desenvolvigames.applications.witalk.fcm.database.WiTalkFirebaseDatabaseManager;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Joao on 01/05/2017.
 */

public class Ip extends EntityBase{

    private static final String IPNODE = "IpNode";
    private String mIp;

    public Ip(){
        mIp = ConstantsClass.Usuario.getIpUsuario();
        if(mWiTalkFirebaseDatabaseManager == null)
            mWiTalkFirebaseDatabaseManager = new WiTalkFirebaseDatabaseManager(this);
        Init();
    }
    @Override
    protected void UpdateSnapshot(){
        if(mAsyncNotifiable!=null)
            mAsyncNotifiable.ExecuteNotify(Ip.this.getClass().getSimpleName(), Ip.this);
    }
    @Override
    protected void Init(){
        DatabaseReference ref = GetRef().child(ConstantsClass.Usuario.getAuthenticationId());
        ref.child("Nome").setValue(ConstantsClass.Usuario.getNomeUsuario());
        ref.child("UserMessageToken").setValue(ConstantsClass.Usuario.getUserMessageToken());
    }
    @Override
    public void Sincronize(IAsyncNotifiable asyncNotifiable) {
        mAsyncNotifiable = asyncNotifiable;
    }
    @Override
    public String GetRoot() {
        return IPNODE.concat("/").concat(mIp);
    }
}
