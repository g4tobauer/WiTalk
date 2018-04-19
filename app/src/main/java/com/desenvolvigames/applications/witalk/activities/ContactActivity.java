package com.desenvolvigames.applications.witalk.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.control.MessageSender;
import com.desenvolvigames.applications.witalk.entities.Contact;
import com.desenvolvigames.applications.witalk.entities.MessageBody;
import com.desenvolvigames.applications.witalk.utilities.OpenActivity;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joao on 25/05/2017.
 */

public class ContactActivity extends BaseActivity {
    private ImageButton mBtnSend;
    private EditText mEditTxtContactMessage;
    private ListView mLstViewContactMessage;
    private ContactAdapter mContactAdapter;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            MessageBody messageBody = null;
            Bundle extras = intent.getExtras();
            if(extras != null) {
                if (extras.containsKey(getString(R.string.intent_message))) {
                    Object object = extras.getSerializable(getString(R.string.intent_message));
                    if(object instanceof MessageBody){
                        messageBody = (MessageBody)object;
                        object = null;
                    }else {
                        object = null;
                    }
                }
            }
//            Bundle bundle = intent.getExtras();
//            String message = bundle.getString(getString(R.string.intent_message));
            addMessage(messageBody.data.UserMessage, messageBody.data.UserId);
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter(ConstantsClass.ContactOpened.mUserId)
        );
    }
    @Override
    protected void onStop() {
        super.onStop();
        ConstantsClass.ContactOpened = null;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        onInitControls();
        onInitEvents();
        onSincronize();

        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mEditTxtContactMessage.getWindowToken(), 0);
    }
    @Override
    protected void onInitControls() {
        mBtnSend = (ImageButton) findViewById(R.id.btnSend);
        mEditTxtContactMessage = (EditText) findViewById(R.id.editTxtContactMessage);
        mLstViewContactMessage = (ListView) findViewById(R.id.lstViewContactMessage);
        mLstViewContactMessage.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        mLstViewContactMessage.setStackFromBottom(true);
    }
    @Override
    protected void onInitEvents() {
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mEditTxtContactMessage.getText().toString();
                if(!message.isEmpty())
                {
                    MessageSender messageSender = new MessageSender(getString(R.string.firebase_api_key));
                    messageSender.getMessageBody().to = ConstantsClass.ContactOpened.mUserMessageToken;

                    messageSender.getMessageBody().data.UserMessage = message;
                    messageSender.getMessageBody().data.UserId = ConstantsClass.Usuario.getAuthenticationId();
                    messageSender.getMessageBody().data.UserName = ConstantsClass.Usuario.getNomeUsuario();
                    messageSender.getMessageBody().data.UserMessageToken = ConstantsClass.Usuario.getUserMessageToken();

                    if(message.length()>10)
                        message = message.substring(0,9).concat("...");

                    messageSender.getMessageBody().notification.body = message;
                    messageSender.getMessageBody().notification.icon = ConstantsClass.Usuario.getImgUrl();
                    messageSender.getMessageBody().notification.title = ConstantsClass.Usuario.getNomeUsuario();
                    try{
                        MessageSender.SenderResult senderResult = messageSender.sendMessage();
                        if(senderResult != null){
                            if(senderResult.success !=0){
                                addMessage(message, messageSender.getMessageBody().data.UserId);
                            }
                        }
                    }catch (Exception ex){
                        Log.w("TAG", ex);
                    }
                }
            }
        });
    }
    @Override
    protected void onSincronize() {
        setAdapter();
    }

    @Override
    public void onBackPressed() {
        OpenActivity.onCloseAndOpenActivity(ContactActivity.this, LobbyActivity.class, null);
    }

    private String mLastId;

    private void addMessage(String message, String id){

        if(mLastId == null || !mLastId.equals(id)){
            mLastId = id;
            mContactAdapter.addMessage(message);
        }
        else{
            mContactAdapter.appendMessage(message);
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

        public void appendMessage(String message) {
            int lastIndex = mlstMessage.size()-1;
            StringBuilder sb = new StringBuilder();
            sb.append(mlstMessage.get(lastIndex))
            .append("\n")
            .append(message);
            mlstMessage.set(lastIndex, sb.toString());
        }
    }
}
