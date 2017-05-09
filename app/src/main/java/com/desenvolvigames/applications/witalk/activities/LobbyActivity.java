package com.desenvolvigames.applications.witalk.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.entities.Contact;
import com.desenvolvigames.applications.witalk.entities.Usuario;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LobbyActivity extends AppCompatActivity implements IAsyncNotifiable, AdapterView.OnItemClickListener{
    private ListView mUiListLobby;
    private ContactsListAdapter mContactsAdapter;
    private ArrayList<Contact> mLstContacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        initControls();
        initEvents();
        sincronize();
    }
    private void initControls(){
        mUiListLobby = (ListView)findViewById(R.id.mUiListLobby);
        setAdapter();
    }
    private void setAdapter(){
        if(mContactsAdapter==null) {
            mContactsAdapter = new ContactsListAdapter(this, mLstContacts);
            mUiListLobby.setAdapter(mContactsAdapter);
        }
        mContactsAdapter.notifyDataSetChanged();
    }
    private void initEvents(){
        mUiListLobby.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(LobbyActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
    }

    private void sincronize(){
        ConstantsClass.Usuario.Sincronize(LobbyActivity.this, mLobbySyncAction);
    }
    @Override
    public Context GetContext() {
        return this;
    }
    @Override
    public void ExecuteNotify(String tag, Object result) {
        switch (tag){
            case mLobbySyncAction:
                sincronize();
                break;
            default:
                break;
        }
    }

    private class ContactsListAdapter extends BaseAdapter {
        private Context mContext;
        private List<Contact> mContactList;

        public ContactsListAdapter(Context context, List<Contact> statelist){
            this.mContext = context;
            this.mContactList = statelist;
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
            View view = inflater.inflate(R.layout.listadapter_lobby, null);

            TextView userNick = (TextView)view.findViewById(R.id.userNick);
            userNick.setText(contact.mNome);

            TextView userStatus = (TextView)view.findViewById(R.id.userStatus);
            userStatus.setText(contact.mUserMessageToken);

            try {
                URL url = new URL(contact.mUserImageResource);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                ImageView img = (ImageView) view.findViewById(R.id.userImageResource);
                img.setImageBitmap(bmp);

            }catch(Exception ex){
                Log.w("TAG: ", ex);
            }
            return view;
        }
    }

}
