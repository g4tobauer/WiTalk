package com.desenvolvigames.applications.witalk.entities;

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

    private final String IPNODE =  (getClass().getSimpleName() + NODE).toUpperCase();

    public Ip(){
        if(mWiTalkFirebaseDatabaseManager == null)
            mWiTalkFirebaseDatabaseManager = new WiTalkFirebaseDatabaseManager(this);
        Init();
    }

    public List<Contact> getLobbyList(){
        ArrayList<Contact> lst = null;
        if(mDataSnapshot != null) {
            lst = new ArrayList<>();
            lst.clear();
            for (DataSnapshot data : mDataSnapshot.getChildren()) {
                Contact contact = new Contact();
                contact.mUserId = data.getKey();
                contact.mNome = String.valueOf(data.child(ConstantsClass.Nome).getValue());
                contact.mStatus = String.valueOf(data.child(ConstantsClass.Status).getValue());
                contact.mUserMessageToken = String.valueOf(data.child(ConstantsClass.UserMessageToken).getValue());
                contact.mUserImageResource = String.valueOf(data.child(ConstantsClass.UserImageSource).getValue());
                lst.add(contact);
            }
        }
        return lst;
    }

    @Override
    protected void Init(){
        DatabaseReference ref = mWiTalkFirebaseDatabaseManager.getRef().child(ConstantsClass.Usuario.getAuthenticationId());
        ref.child(ConstantsClass.Nome).setValue(ConstantsClass.Usuario.getNomeUsuario());
        ref.child(ConstantsClass.Status).setValue(ConstantsClass.Usuario.getStatus());
        ref.child(ConstantsClass.UserMessageToken).setValue(ConstantsClass.Usuario.getUserMessageToken());
        ref.child(ConstantsClass.UserImageSource).setValue(ConstantsClass.Usuario.getImgUrl());
        SyncTime(ref);
    }

    @Override
    protected void UpdateSnapshot(){
        if(mAsyncNotifiable!=null)
            mAsyncNotifiable.ExecuteNotify(Ip.this.getClass().getSimpleName(), Ip.this);
    }

    @Override
    public void Sincronize(IAsyncNotifiable asyncNotifiable) {
        mAsyncNotifiable = asyncNotifiable;
    }

    @Override
    public String GetRoot() {
        return (IPNODE + "/" + ConstantsClass.Usuario.getIpUsuario());
    }

    public void disconnect(){
        mWiTalkFirebaseDatabaseManager.getRef().child(ConstantsClass.Usuario.getAuthenticationId()).removeValue();
    }
}
