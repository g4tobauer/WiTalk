package com.desenvolvigames.applications.witalk.utilities.connection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.desenvolvigames.applications.witalk.MainActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Joao on 16/04/2017.
 */

public class GetAsyncTask extends AsyncTask<String,String,String>
{
    private ProgressDialog load;
    private Context _context;
    public GetAsyncTask(Context context)
    {
        _context = context;
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
        try {
            URL url = new URL(params[0]);

            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return result.toString();
    }
    //Params

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(load !=null)
            load.dismiss();
    }
    //Result
}
