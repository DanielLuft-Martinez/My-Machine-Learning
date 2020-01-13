package com.example.glassbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.glassbox.HomeActivity.SelectedItem;

import com.example.glassbox.CatLoadingView;

public class ChooseModelActivity extends AppCompatActivity{
    CatLoadingView mView;
    Button mSendButton;
    Button epochs1;
    Button epochs2;
    Button epochs3;
    Button process_size1;
    Button process_size2;
    Button process_size3;
    Button conv1;
    Button conv2;
    Button conv3;
    Button inters1;
    Button inters2;
    Button inters3;

    static String imageString1;
    static String imageString2;
    static String imageString3;
    static String imageString4;


    private String[][] datasetStrings = {{"apple", "banana"},
        {"baseball", "basketball", "soccerball"},
        {"chihuahua", "corgi", "husky"},
        {"persian", "tabby", "siamese"},
        {"rabbit", "horse", "shark", "spider"},
        {"watch", "table", "computer", "bag", "sneaker", "piano", "bench", "mug", "cellphone", "jet"},
        {"ambulance", "firetruck", "jet"}};

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_model);

        mView = new CatLoadingView();

        epochs1 = (Button)findViewById(R.id.button_cm02);
        epochs2 = (Button)findViewById(R.id.button_cm03);
        epochs3 = (Button)findViewById(R.id.button_cm04);
        process_size1 = (Button)findViewById(R.id.button_cm12);
        process_size2 = (Button)findViewById(R.id.button_cm13);
        process_size3 = (Button)findViewById(R.id.button_cm14);
        conv1 = (Button)findViewById(R.id.button_cm22);
        conv2 = (Button)findViewById(R.id.button_cm23);
        conv3 = (Button)findViewById(R.id.button_cm24);
        inters1 = (Button)findViewById(R.id.button_cm32);
        inters2 = (Button)findViewById(R.id.button_cm33);
        inters3 = (Button)findViewById(R.id.button_cm34);

        epochs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                epochs1.setSelected(true);
                epochs2.setSelected(false);
                epochs3.setSelected(false);
            }
        });

        epochs2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                epochs2.setSelected(true);
                epochs1.setSelected(false);
                epochs3.setSelected(false);
            }
        });

        epochs3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                epochs3.setSelected(true);
                epochs1.setSelected(false);
                epochs2.setSelected(false);
            }
        });

        process_size1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                process_size1.setSelected(true);
                process_size2.setSelected(false);
                process_size3.setSelected(false);
            }
        });

        process_size2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                process_size2.setSelected(true);
                process_size1.setSelected(false);
                process_size3.setSelected(false);
            }
        });

        process_size3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                process_size3.setSelected(true);
                process_size1.setSelected(false);
                process_size2.setSelected(false);
            }
        });

        conv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conv1.setSelected(true);
                conv2.setSelected(false);
                conv3.setSelected(false);
            }
        });

        conv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conv2.setSelected(true);
                conv1.setSelected(false);
                conv3.setSelected(false);
            }
        });

        conv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conv3.setSelected(true);
                conv1.setSelected(false);
                conv2.setSelected(false);
            }
        });

        inters1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inters1.setSelected(true);
                inters2.setSelected(false);
                inters3.setSelected(false);
            }
        });

        inters2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inters2.setSelected(true);
                inters1.setSelected(false);
                inters3.setSelected(false);
            }
        });

        inters3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inters3.setSelected(true);
                inters1.setSelected(false);
                inters2.setSelected(false);
            }
        });

        logo_fade_in();
        //animationButton();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mSendButton = (Button) findViewById(R.id.RequestButton);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mView.show(getSupportFragmentManager(), "");
                requestJSON();

            }
        });
    }

    private void requestJSON(){
        JSONObject requestBody = new JSONObject();
        String model = "my_test_model";
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //mDatabase.child("models").child(user).push().setValue(model);

        mSendButton.setClickable(false);

        Integer epochs = 10;
        Integer proc_size = 50;
        Integer conv_sets = 1;
        Integer dd_sets = 1;
        if (epochs1.isSelected())
            epochs = 10;
        else if (epochs2.isSelected())
            epochs = 25;
        else if (epochs3.isSelected())
            epochs = 30;
        if (process_size1.isSelected())
            proc_size = 50;
        else if (process_size2.isSelected())
            proc_size = 100;
        else if (process_size3.isSelected())
            proc_size = 150;
        if (conv1.isSelected())
            conv_sets = 1;
        else if (conv2.isSelected())
            conv_sets = 2;
        else if (conv3.isSelected())
            conv_sets = 3;
        if (inters1.isSelected())
            dd_sets = 1;
        else if (inters2.isSelected())
            dd_sets = 2;
        else if (inters3.isSelected())
            dd_sets = 3;

        JSONArray datasets = new JSONArray();

        try {

            for (int category = 0; category<SelectedItem.length; category++){
                for (int dataset = 0; dataset<SelectedItem[category].length; dataset++){
                    if (SelectedItem[category][dataset] == true){
                        datasets.put(datasetStrings[category][dataset]);
                    }
                }
            }
            //Toast.makeText(this, datasets.toString(), Toast.LENGTH_SHORT).show();
            requestBody.put("model", model);
            requestBody.put("user", user);
            requestBody.put("epochs", epochs);
            requestBody.put("proc_size", proc_size);
            requestBody.put("conv_sets", conv_sets);
            requestBody.put("dd_sets", dd_sets);
            requestBody.put("datasets", datasets);

        } catch (JSONException e){
            Log.d("JSONException", e.toString());
        }



        String url = "http://34.83.20.217:5000/create";
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(ChooseModelActivity.this, TraningResultsActivity.class);
                        try{
                            JSONArray jsonarray = response.getJSONArray("images");
                            imageString1 = jsonarray.get(0).toString();
                            imageString2 = jsonarray.get(1).toString();
                            imageString3 = jsonarray.get(2).toString();
                            imageString4 = jsonarray.get(3).toString();

                        }catch(JSONException e){
                            Log.d("onResponse", e.toString());
                        }
                        mSendButton.setClickable(true);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                Log.d("Error Response", error.toString());
                mSendButton.setClickable(true);
                Toast.makeText(ChooseModelActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);



    }

    public void logo_fade_in(){
        ImageView img = (ImageView)findViewById(R.id.logo);
        final Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        img.startAnimation(animFadeIn);

    }
}