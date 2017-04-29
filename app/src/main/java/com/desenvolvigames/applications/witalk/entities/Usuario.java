package com.desenvolvigames.applications.witalk.entities;

import com.google.firebase.auth.UserInfo;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

/**
 * Created by Joao on 28/04/2017.
 */

public class Usuario
{
    public UserInfo mFirebaseUser;
    public UserInfo mFacebookUser;

    public  Usuario(){}
    public <T extends UserInfo>  Usuario(List<T> list)
    {
        for (UserInfo user : list)
        {
            if(user.getProviderId().equals("firebase"))
            {
                mFirebaseUser = user;
            }else if(user.getProviderId().equals("facebook.com"))
            {
                mFacebookUser = user;
            }
        }
    }
    public void update()
    {

    }
    private String getUserMessageToken()
    {
        return FirebaseInstanceId.getInstance().getToken();
    }
}
