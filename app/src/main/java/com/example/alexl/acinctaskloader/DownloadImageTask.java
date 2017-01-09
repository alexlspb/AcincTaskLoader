package com.example.alexl.acinctaskloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * AsyncTask for downloaded images
 */

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView bmImage;

    DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String url = strings[0];
        Bitmap bitImage = null;
        try{
            InputStream in = new URL(url).openStream();
            bitImage = BitmapFactory.decodeStream(in);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return bitImage;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
