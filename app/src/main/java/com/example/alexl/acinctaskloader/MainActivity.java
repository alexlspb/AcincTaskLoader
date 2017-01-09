package com.example.alexl.acinctaskloader;

import android.app.Application;
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
    private static final String URL_IMAGE = "https://66.media.tumblr.com/aa40c3b1d82d63c4acb7f3f27dcfd6e7/tumblr_o79a2wEMlL1sdx0rco1_1280.jpg";

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
        uri = Uri.parse(URL_IMAGE);
        image = (SimpleDraweeView) findViewById(R.id.sdvImage);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            publishProgress();
        }
    };

    private void publishProgress() {
        Toast.makeText(this, "Файл загружен", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_load_with_fresco:
                image.setImageURI(uri);
                break;
            case R.id.btn_load:
                new DownloadImageTask((ImageView) findViewById(R.id.classicImage)).execute(URL_IMAGE);
                break;
            case R.id.download_file:
                Intent intent = new Intent(this, DownloadService.class);
                startService(intent.putExtra("url", "http://cdimage.debian.org/debian-cd/8.6.0/i386/iso-cd/debian-8.6.0-i386-netinst.iso"));
                break;
        }
    }
}
