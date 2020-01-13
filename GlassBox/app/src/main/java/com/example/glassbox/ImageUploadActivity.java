package com.example.glassbox;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class ImageUploadActivity extends AppCompatActivity {

    CatLoadingView mView;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    static String ResultString;
    static String testImage;
    static String layerImage;


    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_upload_image);

        mView = new CatLoadingView();

        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);

        mButtonChooseImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendFile();
            }
        });


    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(mImageView);
        }
    }

    private void sendFile(){
        if(mImageUri != null){
            mView.show(getSupportFragmentManager(), "");
            JSONObject requestBody = new JSONObject();
            String model = "my_test_model";

            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String user = currentFirebaseUser.getUid();

            Bitmap bitmap = ((BitmapDrawable)mImageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();
            testImage = Base64.encodeToString(data, Base64.NO_WRAP);

            try {
                requestBody.put("model", model);
                requestBody.put("user", user);
                requestBody.put("image", testImage);
            } catch (JSONException e){
                Log.d("JSONException", e.toString());
            }

            String url = "http://34.83.20.217:5000/predict";
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                ResultString = "";
                                JSONArray jsonarray = response.getJSONArray("predictions");
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    String label = jsonobject.getString("label");
                                    String probability= jsonobject.getString("probability");
                                    ResultString = ResultString + label.substring(0, 1).toUpperCase() + label.substring(1) + " : \t\t" + probability + "\n";
                                }
                                layerImage = response.getString("image");
                                Intent intent = new Intent(ImageUploadActivity.this, TestResultsActivity.class);
                                intent.putExtra("resultString", ResultString);
                                startActivity(intent);
                            }catch (JSONException e) {
                                Log.d("JSON exception", e.toString());
                            }
                        }
                    }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                Log.d("Error Response", error.toString());
                                Toast.makeText(ImageUploadActivity.this, "Volley error "+ error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    );
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(postRequest);

        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }





}
