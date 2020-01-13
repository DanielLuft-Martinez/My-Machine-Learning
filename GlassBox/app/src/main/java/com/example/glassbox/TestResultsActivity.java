package com.example.glassbox;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.glassbox.ImageUploadActivity.ResultString;
import static com.example.glassbox.ImageUploadActivity.testImage;
import static com.example.glassbox.ImageUploadActivity.layerImage;


public class TestResultsActivity extends AppCompatActivity {
    private ImageView inputImageView;
    private ImageView layerImageView;

    private TextView predictionsTextView;

    private Button backButton;
    private Button finishButton;


    private String predictions;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_test_results);

        inputImageView = findViewById(R.id.ImageView1);
        layerImageView = findViewById(R.id.ImageView2);
        predictionsTextView = findViewById(R.id.TextView4);

        backButton = findViewById(R.id.back_button);
        finishButton = findViewById(R.id.finish_button);

        predictions = ResultString;


        byte[] decodedImage = Base64.decode(testImage, Base64.DEFAULT);
        final Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
        inputImageView.setImageBitmap(decodedByte);

        byte[] decodedImage2 = Base64.decode(layerImage, Base64.DEFAULT);
        final Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedImage2, 0, decodedImage2.length);
        layerImageView.setImageBitmap(decodedByte2);

        predictionsTextView.setText(getIntent().getStringExtra("resultString"));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestResultsActivity.this, ImageUploadActivity.class);
                startActivity(intent);
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestResultsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
