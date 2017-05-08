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

/**
 * Created by Joao on 01/05/2017.
 */

public abstract class EntityBase implements IDataBaseManageable, IAsyncNotifiable {

    protected boolean mIsReleased;
    protected AsyncTask<String, Void, String> mAsyncTask;
    protected ProgressDialog load;
    protected IAsyncNotifiable mAsyncNotifiable;
    protected DataSnapshot mDataSnapshot;
    protected WiTalkFirebaseDatabaseManager mWiTalkFirebaseDatabaseManager;

    @Override
    public void DataSnapshotUpdate(DataSnapshot dataSnapshot){
        mIsReleased = true;
        mDataSnapshot = dataSnapshot;
    }
    @Override
    public Context GetContext() {
        return mAsyncNotifiable.GetContext();
    }

    protected boolean IsReleased(){
        while (!mIsReleased && !mAsyncTask.isCancelled()) {
            try {
                Thread.sleep(3000);
            } catch (Exception ex) {
                Log.w("Exception: ", ex);
            }
        }
        return mIsReleased;
    }
    protected DatabaseReference GetRef(){
        return mWiTalkFirebaseDatabaseManager.getRef();
    }

    protected abstract void Init();
    public abstract void Sincronize(IAsyncNotifiable asyncNotifiable, String syncAction);
    public void ForceRelease(){
        mAsyncTask.cancel(true);
    }
    @Override
    public void ExecuteNotify(String tag, Object result) {}
}
