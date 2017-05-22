package com.desenvolvigames.applications.witalk.entities;

import android.os.AsyncTask;

import com.desenvolvigames.applications.witalk.fcm.database.WiTalkFirebaseDatabaseManager;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

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

    public List<Contact> getLobbyList(){
        ArrayList<Contact> lst = null;
        if(GetDataSnapshot() != null) {
            lst = new ArrayList<>();
            lst.clear();
            for (DataSnapshot data : GetDataSnapshot().getChildren()) {
                if(!data.getKey().equals("SyncTime"))
                {
                    Contact contact = new Contact();
                    contact.mNome = String.valueOf(data.child("Nome").getValue());
                    contact.mUserMessageToken = String.valueOf(data.child("UserMessageToken").getValue());
                    lst.add(contact);
                }
            }
        }
        return lst;
    }

//    @Override
//    protected void SyncTime(DatabaseReference ref){
////        DatabaseReference ref = GetRef().child(ConstantsClass.Usuario.getAuthenticationId());
//        ref.child("SyncTime").setValue(mWiTalkFirebaseDatabaseManager.getTime());
//    }
    @Override
    public DatabaseReference GetIpLobbyReference(){
        return GetRef();
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
        SyncTime(ref);
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
