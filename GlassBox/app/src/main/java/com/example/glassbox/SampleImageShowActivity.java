package com.example.glassbox;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class SampleImageShowActivity extends AppCompatActivity {

    private ImageView mImageView;
    private String mImageUri;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_sample_image_show);
        mImageView = findViewById(R.id.image_view_sample);
        Bundle extras = getIntent().getExtras();
        Integer parent_pos = extras.getInt("PARENT");
        System.out.println(parent_pos);
        Integer child_pos = extras.getInt("CHILD");
        mImageUri = "https://firebasestorage.googleapis.com/v0/b/myml-bc6c6.appspot.com/o/GENERAL%2FDOG%2Fchihuahua%2Fn02085620_10113.JPEG?alt=media&token=7570550b-d4cf-4460-9814-576e9cc6f31d";
        if (parent_pos == 0) {
            if (child_pos == 0) {
                mImageUri = "https://firebasestorage.googleapis.com/v0/b/myml-bc6c6.appspot.com/o/GENERAL%2FFRUIT%2Fapple%2Fn07739344_1008.JPEG?alt=media&token=35c90f98-695f-4efc-8d22-2c44cea668a9";
            }
        }
        if (parent_pos == 0) {
            if (child_pos == 1) {
                mImageUri = "https://firebasestorage.googleapis.com/v0/b/myml-bc6c6.appspot.com/o/GENERAL%2FFRUIT%2Fbanana%2Fn07753592_10129.JPEG?alt=media&token=d48e33ed-d79b-4f92-8468-3906b389e9d6";
            }
        }
        if (parent_pos == 1) {
            if (child_pos == 0) {
                mImageUri = "https://firebasestorage.googleapis.com/v0/b/myml-bc6c6.appspot.com/o/GENERAL%2FBALL%2Fbaseball%2Fn02799071_10091.JPEG?alt=media&token=6bf5f363-d663-4a5e-ada1-9843ceb1a855";
            }
        }
        if (parent_pos == 1) {
            if (child_pos == 1) {
                mImageUri = "https://firebasestorage.googleapis.com/v0/b/myml-bc6c6.appspot.com/o/GENERAL%2FBALL%2Fbasketball%2Fn02802426_10027.JPEG?alt=media&token=4866a836-764c-4216-bbce-626c8a68c31b";
            }
        }

        if (parent_pos == 2) {
            if (child_pos == 0) {
                mImageUri = "https://firebasestorage.googleapis.com/v0/b/myml-bc6c6.appspot.com/o/GENERAL%2FDOG%2Fchihuahua%2Fn02085620_10113.JPEG?alt=media&token=7570550b-d4cf-4460-9814-576e9cc6f31d";
            }
        }
        if (parent_pos == 2) {
            if (child_pos == 1) {
                mImageUri = "https://firebasestorage.googleapis.com/v0/b/myml-bc6c6.appspot.com/o/GENERAL%2FDOG%2Fcorgi%2Fn02112826_10006.JPEG?alt=media&token=c1cf5847-e369-4859-9c84-5e2d2da935d3";
            }
        }
        if (parent_pos == 2) {
            if (child_pos == 2) {
                mImageUri = "https://firebasestorage.googleapis.com/v0/b/myml-bc6c6.appspot.com/o/GENERAL%2FDOG%2Fhusky%2Fn02109961_10025.JPEG?alt=media&token=2eb8e0a6-cdd6-414c-a448-c1cf58da41a2";
            }
        }
        Picasso.with(this).load(mImageUri).into(mImageView);
    }
}