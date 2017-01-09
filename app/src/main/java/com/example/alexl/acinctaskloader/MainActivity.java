package com.example.alexl.acinctaskloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button loadFresco, loadClassic, loadFile;
    private Uri uri;
    private SimpleDraweeView image;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        initUI();
        loadFresco.setOnClickListener(this);
        loadClassic.setOnClickListener(this);
        loadFile.setOnClickListener(this);
        registerReceiver(receiver, new IntentFilter(DownloadService.CHANNEL));
    }

    private void initUI() {
        loadFresco = (Button) findViewById(R.id.btn_load_with_fresco);
        loadClassic = (Button) findViewById(R.id.btn_load);
        loadFile = (Button) findViewById(R.id.download_file);
        uri = Uri.parse(Constants.URL_IMAGE);
        image = (SimpleDraweeView) findViewById(R.id.sdvImage);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int downloadedSize = intent.getIntExtra("downloadedSize", 0);
                int totalSize = intent.getIntExtra("totalSize", 0);
                publishProgress(downloadedSize, totalSize);
            }
        };
    }

    private void publishProgress(int downloadedSize, int totalSize) {
        Toast.makeText(this, (downloadedSize * 100) / totalSize + " %", Toast.LENGTH_SHORT).show();
        if (downloadedSize == totalSize) {
            Toast.makeText(this, R.string.download_complite, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_load_with_fresco:
                image.setImageURI(uri);
                break;
            case R.id.btn_load:
                new DownloadImageTask((ImageView) findViewById(R.id.classicImage)).execute(Constants.URL_IMAGE);
                break;
            case R.id.download_file:
                Intent intent = new Intent(this, DownloadService.class);
                startService(intent.putExtra("url", Constants.URL_ISOFILE));
                break;
        }
    }
}
