package com.desenvolvigames.applications.witalk.fcm.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Joao on 29/04/2017.
 */

public class WiTalkFirebaseDatabaseManager
{

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("condition");
}
