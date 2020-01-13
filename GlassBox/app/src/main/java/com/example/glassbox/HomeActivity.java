package com.example.glassbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {
    static JSONObject jsonObject = new JSONObject();
    static boolean[][] SelectedItem = { {false, false}, {false, false, false}, {false, false, false}, {false, false, false}, {false, false, false, false}, {false, false, false, false, false, false, false, false, false, false, false}, {false, false, false} };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logo_fade_in();
        animationButton();
        Button tutorialButton = findViewById(R.id.start_button);
        /*try {
            jsonObject.put("00", "apple");
            jsonObject.put("01", "banana");
            jsonObject.put("10", "baseball");
            jsonObject.put("11", "basketall");
            jsonObject.put("12", "soccerball");
            jsonObject.put("20", "chihuahua");
            jsonObject.put("21", "corgi");
            jsonObject.put("22", "husky");
            jsonObject.put("30", "persian");
            jsonObject.put("31", "taddy");
            jsonObject.put("32", "siamese");
            jsonObject.put("40", "rabbit");
            jsonObject.put("41", "horse");
            jsonObject.put("42", "shark");
            jsonObject.put("43", "spider");
            jsonObject.put("50", "watch");
            jsonObject.put("51", "table");
            jsonObject.put("52", "lamp");
            jsonObject.put("53", "computer");
            jsonObject.put("54", "bag");
            jsonObject.put("55", "sneaker");
            jsonObject.put("56", "piano");
            jsonObject.put("57", "bench");
            jsonObject.put("58", "mug");
            jsonObject.put("59", "cellphone");
            jsonObject.put("60", "ambulance");
            jsonObject.put("61", "firetruck");
            jsonObject.put("62", "jet");
            jsonObject.put("70", "toaster");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ChooseDataset.class);
                //Intent intent = new Intent(HomeActivity.this, ServerConnectionBasicActivity.class);
                startActivity(intent);
            }
        });
    }

    //animation button
    public void animationButton() {
        Button button = (Button)findViewById(R.id.start_button);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bouncebutton);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.15, 20);
        myAnim.setInterpolator(interpolator);
        //pop once
        button.startAnimation(myAnim);

    }
    public void logo_fade_in(){
        ImageView img = (ImageView)findViewById(R.id.logo);
        final Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        img.startAnimation(animFadeIn);

    }
}