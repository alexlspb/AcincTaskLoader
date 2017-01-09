package com.example.alexl.acinctaskloader;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Service for download iso image debian netinst
 */

public class DownloadService extends IntentService {

    public static final String CHANNEL = DownloadService.class.getSimpleName() + ".broadcast";

    public DownloadService() {
        super("thread");
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

        FileOutputStream fos;

        try {
            url = new URL(intent.getStringExtra("url"));
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File sdFile = new File(sdPath, "debian_netinst.iso");
            fos = new FileOutputStream(sdFile);
            inputStream = urlConnection.getInputStream();

            totalSize = urlConnection.getContentLength();
            downloadedSize = 0;

            buffer = new byte[1024];
            int i = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                if (i == 1000) {
                    sendResult(downloadedSize/1024000, totalSize/1024000);
                    i = 0;
                }
                fos.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                i++;
            }

            fos.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendResult(int downloadedSize, int totalSize) {
        Intent intent = new Intent(CHANNEL);
        intent.putExtra("downloadedSize", downloadedSize);
        intent.putExtra("totalSize", totalSize);
        sendBroadcast(intent);
    }
}
