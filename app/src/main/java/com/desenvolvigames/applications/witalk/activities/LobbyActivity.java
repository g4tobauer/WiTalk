package com.desenvolvigames.applications.witalk.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.entities.Contact;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LobbyActivity extends BaseActivity implements IAsyncNotifiable{
    private ListView mUiListLobby;
    private ContactsListAdapter mContactsAdapter;
    private List<Contact> mContactList = new ArrayList<>();

    @Override
    public Context GetContext() {
        return this;
    }
    @Override
    public void ExecuteNotify(String tag, Object result) {
        switch (tag){
            case "Usuario":
                result = null;
                break;
            case "Ip":
                List<Contact> lst = ConstantsClass.Usuario.getLobbyList();
                mContactList.clear();
                if(lst != null) {
                    Iterator<Contact> iterator = lst.iterator();
                    while (iterator.hasNext()){
                        Contact contact = iterator.next();
                        if(contact.mUserId.equals(ConstantsClass.Usuario.getAuthenticationId())) {
                            iterator.remove();
                        }
                    }
                    mContactList.addAll(lst);
                }
                setAdapter();
                break;
            default:
                break;
        }
    }
    @Override
    protected void onInitControls() {
        mUiListLobby = (ListView)findViewById(R.id.mUiListLobby);
        setAdapter();
    }
    @Override
    protected void onInitEvents() {
        mUiListLobby.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConstantsClass.ContactOpened = (Contact) parent.getItemAtPosition(position);
                Intent intent = new Intent(LobbyActivity.this, ContactActivity.class);
//                intent.putExtra(getString(R.string.entity_contact), ConstantsClass.ContactOpened);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onSincronize() {
        ConstantsClass.Usuario.Sincronize(LobbyActivity.this);
        ConstantsClass.Usuario.connect();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        onInitControls();
        onInitEvents();
        onSincronize();
    }

    private void setAdapter(){
        if(mContactsAdapter==null) {
            mContactsAdapter = new ContactsListAdapter(LobbyActivity.this);
            mUiListLobby.setAdapter(mContactsAdapter);
        }
        mContactsAdapter.notifyDataSetChanged();
    }

    private class ContactsListAdapter extends BaseAdapter {
        private Context mContext;

        public ContactsListAdapter(Context context){
            mContext = context;
        }

        @Override
        public int getCount() {
            return mContactList.size();
        }
        @Override
        public Contact getItem(int position) {
            return mContactList.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Contact contact = mContactList.get(position);
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.activity_lobby_adapter, parent,  false);

            TextView userNick = (TextView)view.findViewById(R.id.userNick);
            userNick.setText(contact.mNome);

            TextView userStatus = (TextView)view.findViewById(R.id.userStatus);
            userStatus.setText(contact.mUserMessageToken);

//            try {
//                URL url = new URL(contact.mUserImageResource);
//                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                ImageView img = (ImageView) view.findViewById(R.id.userImageResource);
//                img.setImageBitmap(bmp);
//
//            }catch(Exception ex){
//                Log.w("TAG: ", ex);
//            }
            return view;
        }
    }
}
