package com.desenvolvigames.applications.witalk.interfaces;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by Joao on 30/04/2017.
 */

public interface IDataBaseManageable {
    String GetRoot();
    void DataSnapshotUpdate(DataSnapshot dataSnapshot);
}
