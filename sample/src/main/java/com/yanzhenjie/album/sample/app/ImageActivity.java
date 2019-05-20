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

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.sample.Models.ImageAdapter;
import com.yanzhenjie.album.sample.R;
import com.yanzhenjie.album.widget.divider.Api21ItemDivider;
import com.yanzhenjie.album.widget.divider.Divider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.media.ExifInterface.TAG_DATETIME_ORIGINAL;
import static android.media.ExifInterface.TAG_GPS_AREA_INFORMATION;

/**
 * Created by YanZhenjie on 2017/8/17.
 */
public class ImageActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTvMessage;

    private Adapter mAdapter;
    private ArrayList<AlbumFile> mAlbumFiles;



    ImageAdapter myImageAdapter;
    File[] files;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        GridView gridview = findViewById(R.id.gridview);
        myImageAdapter = new ImageAdapter(this);
        gridview.setAdapter(myImageAdapter);
        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();

        String targetPath = ExternalStorageDirectoryPath + "/DCIM/FanTate/Images/";
        Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
        File targetDirector = new File(targetPath);

        files = targetDirector.listFiles();
        if (files != null) {
            for (File file : files) {
                myImageAdapter.add(file.getAbsolutePath());
            }
        }
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                Bitmap bm = ImageAdapter.decodeSampledBitmapFromUri(files[position].getPath(),  ImageActivity.this.getResources().getDisplayMetrics().widthPixels, ImageActivity.this.getResources().getDisplayMetrics().heightPixels);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageInByte = stream.toByteArray();
                float lengthbmp = ((imageInByte.length / 1024) / 1024);
            final AlertDialog  dialog=new AlertDialog.Builder(ImageActivity.this).create();
            View preview= getLayoutInflater().inflate(R.layout.layout_dialog_preview,null);
                ImageView imageView;
                TextView title, size, date, loc;
                Button uploadBtn;
                title = preview.findViewById(R.id.title);
                size = preview.findViewById(R.id.size);
                date = preview.findViewById(R.id.date);
                loc = preview.findViewById(R.id.loc);
                title.setText(files[position].getPath().substring(files[position].getPath().lastIndexOf("/") + 1));
                size.setText(lengthbmp + " MB");
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(files[position].getPath());

                    date.setText(exif.getAttribute(TAG_DATETIME_ORIGINAL));
                    loc.setText(exif.getAttribute(TAG_GPS_AREA_INFORMATION));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                uploadBtn = preview.findViewById(R.id.upload);
                imageView = preview.findViewById(R.id.imageView2);
                uploadBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            dialog.setView(preview);
            imageView.setImageBitmap(bm);
                dialog.show();
            }
        });
    }

    /**
     * Preview image, to album.
     */
    private void previewImage(int position) {
        if (mAlbumFiles == null || mAlbumFiles.size() == 0) {
            Toast.makeText(this, R.string.no_selected, Toast.LENGTH_LONG).show();
        } else {
            Album.galleryAlbum(this)
                    .checkable(true)
                    .checkedList(mAlbumFiles)
                    .currentPosition(position)
                    .widget(
                            Widget.newDarkBuilder(this)
                                    .title(mToolbar.getTitle().toString())
                                    .build()
                    )
                    .onResult(new Action<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAction(@NonNull ArrayList<AlbumFile> result) {
                            mAlbumFiles = result;
                            mAdapter.notifyDataSetChanged(mAlbumFiles);
                            mTvMessage.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);
                        }
                    })
                    .start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album_image, menu);
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