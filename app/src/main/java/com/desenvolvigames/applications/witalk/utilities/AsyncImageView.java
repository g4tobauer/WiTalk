package com.desenvolvigames.applications.witalk.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by NOTEBOOK on 23/08/2017.
 */

public class AsyncImageView extends AsyncTask<String, Void, Bitmap> {
    private ImageView loadedImage;
    public AsyncImageView(ImageView imageView){
        loadedImage = imageView;
    }
    @Override
    protected Bitmap doInBackground(String... url) {

        String URL_OF_IMAGE = url[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(URL_OF_IMAGE).openStream();
            bitmap= BitmapFactory.decodeStream(in);
        } catch (Exception e) {
        }
        return bitmap;
    }
    protected void onPostExecute(Bitmap result) {
        loadedImage.setImageBitmap(result);
    }
}