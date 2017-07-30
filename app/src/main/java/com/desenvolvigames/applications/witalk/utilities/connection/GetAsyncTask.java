package com.desenvolvigames.applications.witalk.utilities.connection;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.SSLCertificateSocketFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.desenvolvigames.applications.witalk.interfaces.IAsyncNotifiable;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Joao on 16/04/2017.
 */

public class GetAsyncTask extends AsyncTask<String,String,String>
{
    private ProgressDialog load;
    private Context _context;
    private IAsyncNotifiable _asyncNotifiable;
    private URL url;

    public GetAsyncTask(IAsyncNotifiable asyncNotifiable){
        _context = asyncNotifiable.GetContext();
        _asyncNotifiable = asyncNotifiable;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        load = new ProgressDialog(_context);
        load.setMessage("Loading...");
        load.show();
    }

    //Result
    //Params
    @Override
    protected String doInBackground(String... params){
        StringBuilder result = new StringBuilder();
        HttpsURLConnection urlConnection = null;
        try{
            url = new URL(params[0]);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null){
                result.append(line);
            }
        }catch(Exception ex){
            Log.w("GetAsyncTask", ex);
            ex.printStackTrace();
        }
        finally{
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return result.toString();
    }
    //Params

    @Override
    protected void onPostExecute(String json) {
        super.onPostExecute(json);
        _asyncNotifiable.ExecuteNotify(url.toString(),json);
        if(load !=null)
            load.dismiss();
        load = null;
    }
    //Result
}
