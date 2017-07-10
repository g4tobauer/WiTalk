package com.desenvolvigames.applications.witalk.entities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.desenvolvigames.applications.witalk.fcm.database.WiTalkFirebaseDatabaseManager;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.desenvolvigames.applications.witalk.interfaces.IDataBaseManageable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import static com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass.SyncTime;

/**
 * Created by Joao on 01/05/2017.
 */

public abstract class EntityBase implements IDataBaseManageable, IAsyncNotifiable {

    protected DataSnapshot mDataSnapshot;
    protected IAsyncNotifiable mAsyncNotifiable;
    protected WiTalkFirebaseDatabaseManager mWiTalkFirebaseDatabaseManager;

    @Override
    public void DataSnapshotUpdate(DataSnapshot dataSnapshot){
        mDataSnapshot = dataSnapshot;
        UpdateSnapshot();
    }
    @Override
    public Context GetContext() {
        return mAsyncNotifiable.GetContext();
    }
    @Override
    public void ExecuteNotify(String tag, Object result) {}

    protected void SyncTime(DatabaseReference ref){
        if(ref == null)
            GetRef().child(SyncTime).setValue(mWiTalkFirebaseDatabaseManager.getTime());
        else
            ref.child(SyncTime).setValue(mWiTalkFirebaseDatabaseManager.getTime());
    }
    protected DataSnapshot GetDataSnapshot(){
        return mDataSnapshot;
    }
    protected DatabaseReference GetRef(){
        return mWiTalkFirebaseDatabaseManager.getRef();
    }

    public abstract void Sincronize(IAsyncNotifiable asyncNotifiable);
    protected abstract void Init();
    protected abstract void UpdateSnapshot();
    protected abstract DatabaseReference GetIpLobbyReference();
}
