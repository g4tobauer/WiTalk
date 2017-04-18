package com.desenvolvigames.applications.witalk.utilities.connection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
    public PostAsyncTask(Context context)
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
                URL url = new URL( params[0] );
                String urlParameters  = "\"Teste\"";
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setUseCaches(false);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestProperty("Content-Length", String.valueOf(urlParameters.getBytes().length));
                urlConnection.connect();
                urlConnection.getOutputStream().write(urlParameters.getBytes());

                Reader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

                for (int c; (c = in.read()) >= 0;)
                    Log.i("Resposta do Post", String.valueOf((char)c));
//                    System.out.print((char)c);


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
