package com.desenvolvigames.applications.witalk.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.entities.Contact;
import com.desenvolvigames.applications.witalk.entities.MessageBody;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.desenvolvigames.applications.witalk.utilities.OpenActivity;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LobbyActivity extends BaseActivity implements IAsyncNotifiable{
    private ListView mUiListLobby;
    private ListView mUiListUser;
    private LobbyAdapter mUserAdapter;
    private List<Contact> mUserList = new ArrayList<>();

    private LobbyAdapter mContactsAdapter;
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
                            if (contact.mUserId.equals(ConstantsClass.Usuario.getAuthenticationId())) {
                                mUserList.add(contact);
                                iterator.remove();
                            }
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
        setAdapter();
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
                ConstantsClass.ContactOpened = (Contact) parent.getItemAtPosition(position);
                OpenActivity.onCloseAndOpenActivity(LobbyActivity.this, ContactActivity.class, null);
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
            mContactsAdapter = new LobbyAdapter(LobbyActivity.this, mContactList);
            mUiListLobby.setAdapter(mContactsAdapter);
        }
        if(mUserAdapter == null){
            mUserAdapter = new LobbyAdapter(LobbyActivity.this, mUserList);
            mUiListUser.setAdapter(mUserAdapter);
        }
        mContactsAdapter.notifyDataSetChanged();
        mUserAdapter.notifyDataSetChanged();
    }

    private class ContatoHolder {
        public TextView userNick;
        public TextView userStatus;
        public ImageView userImageResource;
    }

    private class LobbyAdapter extends ArrayAdapter<Contact> {

        public LobbyAdapter(@NonNull Context context, List<Contact> contatos) {
            super(context, 0, contatos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ContatoHolder holder = new ContatoHolder();

            // First let's verify the convertView is not null
            if (convertView == null) {
                // This a new view we inflate the new layout
                LayoutInflater inflater = LayoutInflater.from(getContext());
                v = inflater.inflate(R.layout.activity_lobby_adapter, parent, false);
                // Now we can fill the layout with the right values
                holder.userNick = (TextView) v.findViewById(R.id.userNick);
                holder.userStatus = (TextView) v.findViewById(R.id.userStatus);
                holder.userImageResource = (ImageView) v.findViewById(R.id.userImageResource);
                v.setTag(holder);
            }
            else
                holder = (ContatoHolder) v.getTag();

            Contact p = getItem(position);
            holder.userNick.setText(p.mNome);
            holder.userStatus.setText(p.mStatus);
//            Picasso.with(getContext()).load(p.ImagemContato).into(holder.imgContato);
//            Picasso.with(getContext()).load("http://i.imgur.com/DvpvklR.png").into(holder.imgContato);

            AsyncLobbyAdapter asyncContatoAdapter = new AsyncLobbyAdapter(holder.userImageResource);
            asyncContatoAdapter.execute(p.mUserImageResource);

            return v;
        }
    }

    private class AsyncLobbyAdapter extends AsyncTask<String, Void, Bitmap> {
        private ImageView loadedImage;
        public AsyncLobbyAdapter(ImageView imageView){
            loadedImage = imageView;
        }
        @Override
        protected Bitmap doInBackground(String... url) {

            String URL_OF_IMAGE = url[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(URL_OF_IMAGE).openStream();
                bitmap= BitmapFactory.decodeStream(in);
            } catch (Exception e) {
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap result) {
            loadedImage.setImageBitmap(result);
        }
    }
}
