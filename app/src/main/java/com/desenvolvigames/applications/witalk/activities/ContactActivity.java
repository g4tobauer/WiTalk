package com.desenvolvigames.applications.witalk.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.entities.Contact;
import com.desenvolvigames.applications.witalk.fcm.services.NotificationData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joao on 25/05/2017.
 */

public class ContactActivity extends BaseActivity {
    private Contact mContact;
    private ImageButton mBtnSend;
    private EditText mEditTxtContactMessage;
    private ListView mLstViewContactMessage;
    private ContactAdapter mContactAdapter;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String message = bundle.getString(getString(R.string.intent_message));
            mContactAdapter.addMessage(message);
            setAdapter();
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter(mContact.mUserId)
        );
    }
    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        onInitControls();
        onInitEvents();
        onSincronize();
    }
    @Override
    protected void onInitControls() {
        mBtnSend = (ImageButton) findViewById(R.id.btnSend);
        mEditTxtContactMessage = (EditText) findViewById(R.id.editTxtContactMessage);
        mLstViewContactMessage = (ListView) findViewById(R.id.lstViewContactMessage);
    }
    @Override
    protected void onInitEvents() {
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mEditTxtContactMessage.getText().toString();
                if(!message.isEmpty())
                {
                    mEditTxtContactMessage.setText("");
                    setAdapter();
                }
            }
        });
    }
    @Override
    protected void onSincronize() {
        Bundle bundle =  getIntent().getExtras();
        if(bundle!=null) {
            mContact = (Contact) bundle.get(getString(R.string.entity_contact));
        }
        setAdapter();
    }

    private void setAdapter(){
        if(mContactAdapter==null) {
            mContactAdapter = new ContactAdapter(this);
            mLstViewContactMessage.setAdapter(mContactAdapter);
        }
        mContactAdapter.notifyDataSetChanged();
    }

    private class ContactAdapter extends BaseAdapter {
        private Context mContext;
        private List<String> mlstMessage;
        public ContactAdapter(Context context){
            mlstMessage = new ArrayList<>();
            mContext = context;
        }
        @Override
        public int getCount() {
            return mlstMessage.size();
        }
        @Override
        public String getItem(int position) {
            return mlstMessage.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String message = mlstMessage.get(position);
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.activity_contact_adapter, parent,  false);
            TextView contactMessage = (TextView)view.findViewById(R.id.txtContactMessage);
            contactMessage.setText(message);
            return view;
        }

        public void addMessage(String message) {
            mlstMessage.add(message);
        }
    }
}
