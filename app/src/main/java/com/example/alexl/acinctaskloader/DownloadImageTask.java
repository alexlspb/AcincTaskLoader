package com.example.alexl.acinctaskloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by alexl on 06.01.17.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
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
            Log.e("Ошибка передачи изображения", e.getMessage());
            e.printStackTrace();
        }
        return bitImage;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
