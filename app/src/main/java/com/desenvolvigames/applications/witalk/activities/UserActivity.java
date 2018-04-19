package com.desenvolvigames.applications.witalk.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;
import com.desenvolvigames.applications.witalk.utilities.AsyncImageView;
import com.desenvolvigames.applications.witalk.utilities.OpenActivity;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Joao on 22/08/2017.
 */

public class UserActivity extends BaseActivity implements IAsyncNotifiable{
    private EditText mEdtUserNick, mEdtUserStatus;
    private TextView mUserNick, mUserStatus;
    private ImageView mUserImageResource;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        onInitControls();
        onInitEvents();
        onSincronize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_save:
                save();
                break;
            case R.id.action_cancel:
                cancel();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        OpenActivity.onCloseAndOpenActivity(UserActivity.this, LobbyActivity.class, null);
    }

    @Override
    protected void onInitControls() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mUserNick = (TextView) findViewById(R.id.userNick);
        mUserStatus = (TextView) findViewById(R.id.userStatus);
        mEdtUserNick = (EditText) findViewById(R.id.edtUserNick);
        mEdtUserStatus = (EditText) findViewById(R.id.edtUserStatus);
        mUserImageResource = (ImageView) findViewById(R.id.userImageResource);
    }

    @Override
    protected void onInitEvents() {
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onSincronize() {
        ConstantsClass.Usuario.SincronizeNotifiable(UserActivity.this);
    }

    @Override
    public Context GetContext() {
        return this;
    }

    @Override
    public void ExecuteNotify(String tag, Object result) {
        setFields();
    }

    private void save(){
        if(mEdtUserNick.getText()!= null && mEdtUserNick.getText().length()>0)
            ConstantsClass.Usuario.setNomeUsuario(mEdtUserNick.getText().toString());
        ConstantsClass.Usuario.setStatusUsuario(mEdtUserStatus.getText()==null?"":mEdtUserStatus.getText().toString());
    }

    private void cancel(){
        setFields();
    }

    private void setFields(){
        mUserNick.setText(ConstantsClass.Usuario.getNomeUsuario());
        mUserStatus.setText(ConstantsClass.Usuario.getStatus());
        mEdtUserNick.setText(ConstantsClass.Usuario.getNomeUsuario());
        mEdtUserStatus.setText(ConstantsClass.Usuario.getStatus());
        AsyncImageView asyncContatoAdapter = new AsyncImageView(mUserImageResource);
        asyncContatoAdapter.execute(ConstantsClass.Usuario.getImgUrl());
    }
}
