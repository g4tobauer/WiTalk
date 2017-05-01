package com.desenvolvigames.applications.witalk.utilities.connection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.desenvolvigames.applications.witalk.activities.old.BaseActivity;
import com.desenvolvigames.applications.witalk.interfaces.IJsonNotifiable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Joao on 16/04/2017.
 */

public class GetAsyncTask extends AsyncTask<String,Integer,String>
{
    private ProgressDialog load;
    private Context _context;
    private IJsonNotifiable _jsonNotifiable;
    private URL url;

    public GetAsyncTask(IJsonNotifiable jsonNotifiable)
    {
        _context = jsonNotifiable.GetContext();
        _jsonNotifiable = jsonNotifiable;
    }
    //Progress
    @Override
    protected void onProgressUpdate(Integer... values)
    {
        super.onProgressUpdate(values);
        Log.i("onProgressUpdate", "Exibindo ProgressDialog na tela Thread: " + Thread.currentThread().getName());
        if(load == null)
            load = ProgressDialog.show(_context, "Por favor Aguarde ...","Consultando ...",true, false);
        load.setMessage("Consultando ... " + values[0]);
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
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null)
            {
                result.append(line);
            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return result.toString();
    }
    //Params

    @Override
    protected void onPostExecute(String json) {
        super.onPostExecute(json);
        _jsonNotifiable.ExecuteNotify(url.toString(),json);
        if(load !=null)
            load.dismiss();
        load = null;
    }
    //Result
}
