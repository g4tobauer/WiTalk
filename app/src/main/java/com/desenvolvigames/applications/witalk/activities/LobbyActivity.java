package com.desenvolvigames.applications.witalk.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.control.ContactLayoutAdapter;
import com.desenvolvigames.applications.witalk.entities.Contact;
import com.desenvolvigames.applications.witalk.entities.MessageBody;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.desenvolvigames.applications.witalk.utilities.OpenActivity;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LobbyActivity extends BaseActivity implements IAsyncNotifiable{
    private ListView mUiListLobby;
    private ListView mUiListUser;
    private ContactLayoutAdapter mUserAdapter;
    private List<Contact> mUserList = new ArrayList<>();

    private ContactLayoutAdapter mContactsAdapter;
    private List<Contact> mContactList = new ArrayList<>();

    @Override
    public Context GetContext() {
        return this;
    }
    @Override
    public void ExecuteNotify(String tag, Object result) {
        if(ConstantsClass.Usuario != null) {
            switch (tag) {
                case "Usuario":
                    result = null;
                    break;
                case "Ip":
                    List<Contact> lstContact = ConstantsClass.Usuario.getLobbyList();
                    mContactList.clear();
                    mUserList.clear();
                    if (lstContact != null) {
                        Iterator<Contact> iterator = lstContact.iterator();
                        while (iterator.hasNext()) {
                            Contact contact = iterator.next();
//                            if (contact.mUserId.equals(ConstantsClass.Usuario.getAuthenticationId())) {
//                                mUserList.add(contact);
//                                iterator.remove();
//                            }
                        }
                        mContactList.addAll(lstContact);
                    }
                    setAdapter();
                    break;
                default:
                    break;
            }
        }
    }
    @Override
    protected void onInitControls() {
        mUiListLobby = (ListView)findViewById(R.id.mUiListLobby);
        mUiListUser = (ListView)findViewById(R.id.mUiListUser);
    }
    @Override
    protected void onInitEvents() {
        mUiListLobby.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConstantsClass.ContactOpened = (Contact) parent.getItemAtPosition(position);
                OpenActivity.onCloseAndOpenActivity(LobbyActivity.this, ContactActivity.class, null);
            }
        });
        mUiListUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ConstantsClass.ContactOpened = (Contact) parent.getItemAtPosition(position);
                OpenActivity.onCloseAndOpenActivity(LobbyActivity.this, UserActivity.class, null);
            }
        });
    }
    @Override
    protected void onSincronize() {
        ConstantsClass.Usuario.connect(LobbyActivity.this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        verifyNotification();
        onInitControls();
        onInitEvents();
        onSincronize();
    }

    @Override
    protected void onResume() {
        setAdapter();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        OpenActivity.onCloseAndOpenActivity(LobbyActivity.this, AuthenticationActivity.class, null);
    }

    private void verifyNotification(){
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if (extras.containsKey(getString(R.string.intent_notification))) {
                Object object = extras.getSerializable(getString(R.string.intent_notification));
                if(object instanceof MessageBody){
                    MessageBody messageBody = (MessageBody)object;
                    object = null;
                }else {
                    object = null;
                }
            }
        }
    }

    private void setAdapter(){
        if(mContactsAdapter==null) {
            mContactsAdapter = new ContactLayoutAdapter(LobbyActivity.this, mContactList);
            mUiListLobby.setAdapter(mContactsAdapter);
        }
        if(mUserAdapter == null){
            mUserAdapter = new ContactLayoutAdapter(LobbyActivity.this, mUserList);
            mUiListUser.setAdapter(mUserAdapter);
        }
        mContactsAdapter.notifyDataSetChanged();
        mUserAdapter.notifyDataSetChanged();
    }
}
