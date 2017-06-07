package com.desenvolvigames.applications.witalk.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Joao on 07/05/2017.
 */

public class Contact implements Serializable{
    public String mNome;
    public String mUserMessageToken;
    public String mUserImageResource;
    public Message mMessage;

    public void onInitContactMessage(){
        if(mMessage==null)
            mMessage = new Message();
    }

    public class Message{
        public ArrayList<String> mLstMessage;
        public Message(){
            mLstMessage = new ArrayList<>();
        }
    }
}
