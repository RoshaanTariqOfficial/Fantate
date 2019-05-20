/*
 * Copyright 2016 Yan Zhenjie.
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
package com.yanzhenjie.album.sample.Home;

import android.content.Intent;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.yanzhenjie.album.sample.Authorization.LogIn_Activity;
import com.yanzhenjie.album.sample.R;
import com.yanzhenjie.album.sample.app.AlbumActivity;
import com.yanzhenjie.album.sample.app.AlbumFilterActivity;
import com.yanzhenjie.album.sample.app.CameraActivity;
import com.yanzhenjie.album.sample.app.DefineStyleActivity;
import com.yanzhenjie.album.sample.app.GalleryActivity;
import com.yanzhenjie.album.sample.app.ImageActivity;
import com.yanzhenjie.album.sample.app.RecordVideoActivity;
import com.yanzhenjie.album.sample.app.VideoActivity;

import java.io.IOException;

import static android.media.ExifInterface.TAG_DATETIME_ORIGINAL;
import static android.media.ExifInterface.TAG_GPS_AREA_INFORMATION;

/**
 * <p>Album sample.</p>
 * Created by Yan Zhenjie on 2016/10/30.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        helpDialog();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);

        findViewById(R.id.btn_camera).setOnClickListener(this);
        findViewById(R.id.btn_record_video).setOnClickListener(this);
        findViewById(R.id.btn_image).setOnClickListener(this);
        findViewById(R.id.btn_video).setOnClickListener(this);
        findViewById(R.id.btn_album).setOnClickListener(this);
        findViewById(R.id.btn_signout).setOnClickListener(this);
    }

    private void helpDialog() {
        final AlertDialog dialog=new AlertDialog.Builder(MainActivity.this).create();
        View preview = getLayoutInflater().inflate(R.layout.layout_dialog_help,null);
        TextView text;
        ImageView closeBtn;
        ImageView imageView;

       // imageView = preview.findViewById(R.id.imageView4);
        text = preview.findViewById(R.id.helpText);
        closeBtn = preview.findViewById(R.id.imageButton);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(preview);
        dialog.show();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_camera: {
                startActivity(new Intent(this, CameraActivity.class));
                break;
            }
            case R.id.btn_record_video: {
                startActivity(new Intent(this, RecordVideoActivity.class));
                break;
            }
            case R.id.btn_image: {
                startActivity(new Intent(this, ImageActivity.class));
                break;
            }
            case R.id.btn_video: {
                startActivity(new Intent(this, VideoActivity.class));
                break;
            }
            case R.id.btn_album: {
                startActivity(new Intent(this, AlbumActivity.class));
                break;
            }
            case R.id.btn_signout: {
                mAuth.signOut();
                startActivity(new Intent(this, LogIn_Activity.class));
                finish();
                break;
            }
        }
    }
}
