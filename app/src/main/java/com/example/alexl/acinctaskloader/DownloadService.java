package com.example.alexl.acinctaskloader;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by alexl on 07.01.17.
 */

public class DownloadService extends IntentService {

    public static final String CHANNEL = DownloadService.class.getSimpleName()+".broadcast";

    public DownloadService() {
        super("test");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        URL url;
        HttpURLConnection urlConnection;
        InputStream inputStream;
        int totalSize;
        int downloadedSize;
        byte[] buffer;
        int bufferLength;

        File file = null;
        FileOutputStream fos = null;

        try{
            url = new URL(intent.getStringExtra("url"));
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            file = File.createTempFile("image", "download");
            fos = new FileOutputStream(file);
            inputStream = urlConnection.getInputStream();

            totalSize = urlConnection.getContentLength();
            downloadedSize = 0;

            buffer = new byte[1024];
            bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                if (downloadedSize == totalSize) {
                    sendResult();
                }
            }

            fos.close();
            inputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendResult() {
        Intent intent = new Intent(CHANNEL);
        sendBroadcast(intent);
    }

}
