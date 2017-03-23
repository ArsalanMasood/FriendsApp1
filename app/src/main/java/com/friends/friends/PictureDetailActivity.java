package com.friends.friends;

/**
 * Created by Arsalan on 6/26/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import utils.customview.ScaleImageView;


public class PictureDetailActivity extends Activity {
    String imageurl;
    private ImageView  imgView,imgClose,imgShare,imgRotateRight,imgRotateLeft;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fullscreen_image);

        imgView= (ImageView) findViewById(R.id.imgDisplay);
        imgRotateLeft= (ImageView) findViewById(R.id.imgRotateLeft);
        imgRotateRight= (ImageView) findViewById(R.id.imgRotateRight);
        imgClose= (ImageView) findViewById(R.id.imgClose);
        imgShare= (ImageView) findViewById(R.id.imgShare);
        progressBar= (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Uri uri= getUri();
                if(uri!=null){
                    share(uri);
                }
            }
        });

        imgRotateRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgView.setRotation(imgView.getRotation() + 90);
            }
        });
        imgRotateLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgView.setRotation(imgView.getRotation() - 90);
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  finish();
        //        imgView.setRotation(imgView.getRotation() + 90);
            }
        });

        Bundle b = getIntent().getExtras();
        if(b!=null) {
            if (b.containsKey("url")) {
                imageurl = b.getString("url");
                if (!imageurl.equalsIgnoreCase("")){

                    Picasso.with(PictureDetailActivity.this)
                            .load(imageurl)
                            .into(imgView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    progressBar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onError() {
                                    finish();
                                }
                            });
                 }
                else{
                    finish();
                }
                // paths = getImagePaths();
             }
        }
        else{
            finish();
        }
    }

private Uri getUri(){
    Drawable mDrawable = imgView.getDrawable();
    Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();

    String path = MediaStore.Images.Media.insertImage(getContentResolver(),
            mBitmap, "Shared From Friends", null);

    Uri uri = Uri.parse(path);
    return uri;

}

    private void share(Uri uri){
        Intent localIntent1 = new Intent("android.intent.action.SEND");
        localIntent1.setType("image/png");
        localIntent1.putExtra("android.intent.extra.STREAM", uri);
        startActivity(Intent.createChooser(localIntent1, "Share"));

    }

}

