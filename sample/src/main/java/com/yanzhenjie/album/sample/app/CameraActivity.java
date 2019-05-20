/*
 * Copyright 2017 Yan Zhenjie.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.album.sample.app;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

/**
 * Created by YanZhenjie on 2017/8/17.
 */
public class CameraActivity extends AppCompatActivity {

    TextView mTextView;
    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory().getAbsolutePath();
        String path = ExternalStorageDirectoryPath + "/DCIM/FanTate";
        File file = new File(path);
        if (!file.isDirectory()) {
            file.mkdir();
        }
        takePicture(path);
        mTextView = findViewById(R.id.tv_message);
        mImageView = findViewById(R.id.image_view);
    }

    private void takePicture(String path) {
        path += "/Images";
        File file = new File(path);
        if (!file.isDirectory()) {
            file.mkdir();
        }
        Album.camera(this)
                .image()
                .filePath(path + "/" + Calendar.getInstance().getTimeInMillis()
                        + ".jpg")
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
