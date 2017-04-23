package com.desenvolvigames.applications.witalk.utilities.connection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.desenvolvigames.applications.witalk.activities.BaseActivity;
import com.desenvolvigames.applications.witalk.interfaces.IJsonNotifiable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Joao on 16/04/2017.
 */

public class PostAsyncTask extends AsyncTask<String,String,String>
{
    private ProgressDialog load;
    private Context _context;
    private IJsonNotifiable _jsonNotifiable;
    public PostAsyncTask(BaseActivity activity)
    {
        _context = activity;
        _jsonNotifiable = activity;
    }
    //Progress
    @Override
    protected void onProgressUpdate(String... values)
    {
        super.onProgressUpdate(values);
        Log.i("AsyncTask", "Exibindo ProgressDialog na tela Thread: " + Thread.currentThread().getName());
        load = ProgressDialog.show(_context, "Por favor Aguarde ...","Testando ...");
    }
    //Progress

    //Result
    //Params
    @Override
    protected String doInBackground(String... params)
    {

        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try
        {
            String urlParameters  = _jsonNotifiable.GetJsonParameters();
            _jsonNotifiable.ClearParameters();
            URL url = new URL( params[0] );
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(urlParameters.getBytes().length));
            urlConnection.connect();
            urlConnection.getOutputStream().write(urlParameters.getBytes());
            Reader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

            for (int c; (c = in.read()) >= 0;)
            {
                result.append((char)c);
            }

        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return result.toString().trim();
    }
    //Params

    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);
        _jsonNotifiable.ExecuteNotify("POSTIP", result);
        if(load !=null)
            load.dismiss();
    }
    //Result
}
