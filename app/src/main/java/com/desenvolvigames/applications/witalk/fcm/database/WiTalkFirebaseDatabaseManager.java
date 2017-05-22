package com.desenvolvigames.applications.witalk.fcm.database;

import com.desenvolvigames.applications.witalk.interfaces.IDataBaseManageable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * Created by Joao on 29/04/2017.
 */

public class WiTalkFirebaseDatabaseManager
{
    private IDataBaseManageable mManageable;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mConditionRef;

    public WiTalkFirebaseDatabaseManager(IDataBaseManageable manageable){
        mManageable = manageable;
        mConditionRef = mRootRef.child(mManageable.GetRoot());
        IniciarFirebaseDatabase();
    }
    private void IniciarFirebaseDatabase(){
        mConditionRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                mManageable.DataSnapshotUpdate(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }
    public Map<String, String> getTime(){return ServerValue.TIMESTAMP;}
    public DatabaseReference getRef(){
        return mConditionRef;
    }
}
