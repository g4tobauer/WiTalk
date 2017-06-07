package com.desenvolvigames.applications.witalk.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joao on 25/05/2017.
 */

public class ContactActivity extends BaseActivity {
    private Contact mContact;
    private ImageButton mBtnSend;
    private ScrollView mScrollViewContactMessage;
    private EditText mEditTxtContactMessage;
    private ListView mLstViewContactMessage;
    private ContactAdapter mContactAdapter;

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
        mScrollViewContactMessage = (ScrollView) findViewById(R.id.scrollViewContactMessage);
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
                    mContact.mMessage.mLstMessage.add(message);
                    setAdapter();
                }
            }
        });
    }
    @Override
    protected void onSincronize() {
        Bundle bundle =  getIntent().getExtras();
        mContact = (Contact)bundle.get("contact");
        mContact.onInitContactMessage();
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
        public ContactAdapter(Context context){
            mContext = context;
        }
        @Override
        public int getCount() {
            return mContact.mMessage.mLstMessage.size();
        }
        @Override
        public String getItem(int position) {
            return mContact.mMessage.mLstMessage.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String nick = mContact.mNome;
            String message = mContact.mMessage.mLstMessage.get(position);
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.activity_contact_adapter, parent,  false);
            TextView contactNick = (TextView)view.findViewById(R.id.txtContactNick);
            contactNick.setText(nick);
            TextView contactMessage = (TextView)view.findViewById(R.id.txtContactMessage);
            contactMessage.setText(message);
            return view;
        }
    }
}
