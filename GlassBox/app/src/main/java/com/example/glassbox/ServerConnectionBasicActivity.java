package com.example.glassbox;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;




public class ServerConnectionBasicActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mButtonChooseImage;
    private Button mButtonSend;
    private EditText mEditTextFileName;
    private TextView mResultTextView;
    private ImageView mImageView;

    private Uri mImageUri;
    private File mFile;
    private String mURL;
    private String mFilePath;

    private StorageTask mUploadTask;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_test_model);

        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonSend = findViewById(R.id.button_send);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mImageView = findViewById(R.id.image_view);
        mResultTextView = findViewById(R.id.resultTextView);

        mButtonChooseImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendFile();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

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
            //Creates file from Uri
            //mFilePath = ImageFilePath.getPath(ServerConnectionBasicActivity.this, mImageUri);
            //mFile = new File(mFilePath);

            Picasso.with(this).load(mImageUri).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void sendFile(){
        if(mImageUri != null){
            JSONObject requestBody = new JSONObject();
            String model = "my_test_model";

            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String user = currentFirebaseUser.getUid();

            Bitmap bitmap = ((BitmapDrawable)mImageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();
            String image = Base64.encodeToString(data, Base64.NO_WRAP);

            try {
                requestBody.put("model", model);
                requestBody.put("user", user);
                requestBody.put("image", image);
            } catch (JSONException e){
                Log.d("JSONException", e.toString());
            }

            String url = "http://34.83.20.217:5000/predict";
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String ResultString = "With this image : \n";
                                JSONArray jsonarray = response.getJSONArray("predictions");
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    String label = jsonobject.getString("label");
                                    Double probability= jsonobject.getDouble("probability");
                                    probability = probability * 100;
                                    ResultString = ResultString + "It has probability of being "+ label + " of " +probability.toString() + "%\n";
                                }
                                mResultTextView.setText(ResultString);
                            }catch (JSONException e){
                                Log.d("JSON exception", e.toString());
                            }
                            Toast.makeText(ServerConnectionBasicActivity.this, "Received Results", Toast.LENGTH_SHORT).show();
                            Toast.makeText(ServerConnectionBasicActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO: Handle error
                    Log.d("Error Response", error.toString());
                    Toast.makeText(ServerConnectionBasicActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            );
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(postRequest);

        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    
    private void oepnImagesActivity() {
        Intent intent = new Intent(this, UploadImageShowActivity.class);
        startActivity(intent);
    }

}