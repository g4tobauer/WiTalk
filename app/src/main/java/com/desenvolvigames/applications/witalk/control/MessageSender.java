package com.desenvolvigames.applications.witalk.control;

import android.os.AsyncTask;
import android.util.Log;

import com.desenvolvigames.applications.witalk.entities.MessageBody;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by NOTEBOOK on 05/07/2017.
 */

public class MessageSender  {
    private String mAuthentication;
    private MessageBody mMessageBody;

    public MessageSender(String authentication){
        mAuthentication = authentication;
    }

    public void sendMessage(){
        String json = new Gson().toJson(mMessageBody);
        new PostMessageAsyncTask(json).execute();
    }

    public MessageBody getMessageBody(){
        if(mMessageBody==null)
            mMessageBody = new MessageBody();
        return mMessageBody;
    }

    private class PostMessageAsyncTask extends AsyncTask{
        String mMessage;
        public PostMessageAsyncTask(String json){
            mMessage = json;
        }
        @Override
        protected Object doInBackground(Object[] params) {
            StringBuilder result = new StringBuilder();
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(ConstantsClass.PostFirebaseMessage);
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setUseCaches(false);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Authorization", "key=".concat(mAuthentication));
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestProperty("Content-Length", String.valueOf(mMessage.getBytes().length));
                urlConnection.connect();
                urlConnection.getOutputStream().write(mMessage.getBytes());
                Reader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

                for (int c; (c = in.read()) >= 0;){
                    result.append((char)c);
                }
            }catch (Exception ex){
                Log.w("TAG", ex);
            }
            finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }
    }
}
