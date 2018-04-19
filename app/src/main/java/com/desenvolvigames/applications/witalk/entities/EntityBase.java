package com.desenvolvigames.applications.witalk.entities;

import com.desenvolvigames.applications.witalk.fcm.database.WiTalkFirebaseDatabaseManager;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.desenvolvigames.applications.witalk.interfaces.IDataBaseManageable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import static com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass.SyncTime;

/**
 * Created by Joao on 01/05/2017.
 */

public abstract class EntityBase implements IDataBaseManageable{

    protected final String NODE = "NODE";
    protected DataSnapshot mDataSnapshot;
    protected IAsyncNotifiable mSyncNotifiable;
    protected WiTalkFirebaseDatabaseManager mWiTalkFirebaseDatabaseManager;

    @Override
    public void DataSnapshotUpdate(DataSnapshot dataSnapshot){
        mDataSnapshot = dataSnapshot;
        UpdateSnapshot();
    }

    protected void SyncTime(DatabaseReference ref){
//        if(ref == null)
//            mWiTalkFirebaseDatabaseManager.getRef().child(SyncTime).setValue(mWiTalkFirebaseDatabaseManager.getTime());
//        else
            ref.child(SyncTime).setValue(mWiTalkFirebaseDatabaseManager.getTime());
    }
    protected void Init()
    {
        if(mWiTalkFirebaseDatabaseManager == null)
            mWiTalkFirebaseDatabaseManager = new WiTalkFirebaseDatabaseManager(this);
    }

    public abstract void SincronizeNotifiable(IAsyncNotifiable asyncNotifiable);
    protected abstract void UpdateSnapshot();
}
