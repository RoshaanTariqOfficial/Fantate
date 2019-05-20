package com.yanzhenjie.album.sample.app;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.sample.R;

import java.io.File;
import java.util.Calendar;
import java.util.UUID;

public class RecordVideoActivity extends AppCompatActivity {

    TextView mTextView;
    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();
        String path = ExternalStorageDirectoryPath + "/DCIM/FanTate";
        File file = new File(path);
        if (!file.isDirectory()) {
            file.mkdir();
        }
        recordVideo(path);
        mTextView = findViewById(R.id.tv_message);
        mImageView = findViewById(R.id.videoView);
    }

    private void recordVideo(String path) {
        path += "/Videos";
        File file = new File(path);
        if (!file.isDirectory()) {
            file.mkdir();
        }
        Album.camera(this)
                .video()
                .filePath(path + "/" + Calendar.getInstance().getTimeInMillis() + ".mp4")
                .quality(1)
                .limitDuration(Integer.MAX_VALUE)
                .limitBytes(Integer.MAX_VALUE)
                .onResult(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        mTextView.setText(result);

                        Album.getAlbumConfig()
                                .getAlbumLoader()
                                .load(mImageView, result);
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        finish();
                    }
                })
                .start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return true;
    }
}
