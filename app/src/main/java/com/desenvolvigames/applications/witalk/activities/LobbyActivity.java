package com.desenvolvigames.applications.witalk.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.entities.Contact;
import com.desenvolvigames.applications.witalk.entities.Ip;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class LobbyActivity extends AppCompatActivity implements IAsyncNotifiable, AdapterView.OnItemClickListener{
    private ListView mUiListLobby;
    private ContactsListAdapter mContactsAdapter;
    private List<Contact> mContactList = new ArrayList<>();

//    Ip ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        initControls();
        initEvents();
        sincronize();
        ConstantsClass.Usuario.connect();
    }

    private void initControls(){
        mUiListLobby = (ListView)findViewById(R.id.mUiListLobby);
        setAdapter();
    }
    private void setAdapter(){
        if(mContactsAdapter==null) {
            mContactsAdapter = new ContactsListAdapter(LobbyActivity.this, mContactList);
            mUiListLobby.setAdapter(mContactsAdapter);
        }
        mContactsAdapter.notifyDataSetChanged();
    }
    private void initEvents(){
        mUiListLobby.setOnItemClickListener(this);
    }
    private void sincronize(){
        ConstantsClass.Usuario.Sincronize(LobbyActivity.this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        setAdapter();
    }
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
                    mContactList.addAll(lst);
                }
                setAdapter();
                break;
            default:
                result = null;
                break;
        }
    }

    private class ContactsListAdapter extends BaseAdapter {
        private Context mContext;
        private List<Contact> mContactList;

        public ContactsListAdapter(Context context, List<Contact> contactList){
            this.mContext = context;
            mContactList = contactList;
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
            View view = inflater.inflate(R.layout.listadapter_lobby, parent,  false);

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
