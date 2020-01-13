package com.example.glassbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.app.AlertDialog;

import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONObject;

import static com.example.glassbox.ChooseModelActivity.imageString1;
import static com.example.glassbox.ChooseModelActivity.imageString2;
import static com.example.glassbox.ChooseModelActivity.imageString3;
import static com.example.glassbox.ChooseModelActivity.imageString4;

public class TraningResultsActivity extends AppCompatActivity {

    static JSONObject jsonObject = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_results);

        ImageView results1 = findViewById(R.id.resultView1);
        ImageView results2 = findViewById(R.id.resultView2);
        ImageView results3 = findViewById(R.id.resultView3);
        ImageView results4 = findViewById(R.id.resultView4);

        Intent intent = getIntent();

        Log.d("imageString1", imageString1);

        byte[] decodedString1 = Base64.decode(imageString1, Base64.DEFAULT);
        byte[] decodedString2 = Base64.decode(imageString2, Base64.DEFAULT);
        byte[] decodedString3 = Base64.decode(imageString3, Base64.DEFAULT);
        byte[] decodedString4 = Base64.decode(imageString4, Base64.DEFAULT);
        final Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
        results1.setImageBitmap(decodedByte1);
        final Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
        results2.setImageBitmap(decodedByte2);
        final Bitmap decodedByte3 = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);
        results3.setImageBitmap(decodedByte3);
        final Bitmap decodedByte4 = BitmapFactory.decodeByteArray(decodedString4, 0, decodedString4.length);
        results4.setImageBitmap(decodedByte4);



        Button NextButton = findViewById(R.id.test_model_botton);

        //button to next page
        NextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(TraningResultsActivity.this, ImageUploadActivity.class);
                startActivity(intent);
            }
        });

        //Pinch the images to zoom in
        results1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(TraningResultsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                PhotoView photoView = mView.findViewById(R.id.ZoominImageView);
                photoView.setScaleType(ImageView.ScaleType.FIT_XY);
                //replace the drawable variable to readin image variable
                photoView.setImageBitmap(decodedByte1);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        results2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(TraningResultsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                PhotoView photoView = mView.findViewById(R.id.ZoominImageView);
                photoView.setScaleType(ImageView.ScaleType.FIT_XY);
                //replace the drawable variable to readin image variable
                photoView.setImageBitmap(decodedByte2);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        results3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(TraningResultsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                PhotoView photoView = mView.findViewById(R.id.ZoominImageView);
                photoView.setScaleType(ImageView.ScaleType.FIT_XY);
                //replace the drawable variable to readin image variable
                photoView.setImageBitmap(decodedByte3);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        results4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(TraningResultsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                PhotoView photoView = mView.findViewById(R.id.ZoominImageView);
                photoView.setScaleType(ImageView.ScaleType.FIT_XY);
                //replace the drawable variable to readin image variable
                photoView.setImageBitmap(decodedByte4);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });




    }
}

