package com.desenvolvigames.applications.witalk.fcm.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Joao on 29/04/2017.
 */

public class WiTalkFirebaseDatabaseManager
{
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef;

    public WiTalkFirebaseDatabaseManager(String ref){
        mConditionRef = mRootRef.child(ref);
        IniciarFirebaseDatabase();
    }
    private void IniciarFirebaseDatabase(){
        mConditionRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                Object obj = dataSnapshot.getValue();
            }
            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }
    public DatabaseReference getRef(String key){
        return mConditionRef.child(key);
    }
}
