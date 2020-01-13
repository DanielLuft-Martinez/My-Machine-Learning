package com.example.glassbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glassbox.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.utilities.Validation;

import java.io.Console;

public class CreateAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText usernameField;
    private EditText createEmailField;
    private EditText createPasswordField;
    private EditText confirmPasswordField;
    private Button createAccountButton;
    private String name;
    private String email;
    private String password;
    private User usr;

    //This is for handling database
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        logo_fade_in();

        // Initialize UI elements and our Firebase authentication object
        createEmailField = findViewById(R.id.editTextCreateEmail);
        createPasswordField = findViewById(R.id.editTextCreatePassword);
        confirmPasswordField = findViewById(R.id.editTextConfirmPassword);
        usernameField = findViewById(R.id.editTextCreateName);
        createAccountButton = findViewById(R.id.buttonCreateAccount);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // Clicking the create account button gets the data from the text fields and submits the
        // create request to the Firebase backend
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = usernameField.getText().toString().trim();
                email = createEmailField.getText().toString().trim();
                password = createPasswordField.getText().toString();
                if(!validateFields(usernameField, createEmailField, createPasswordField, confirmPasswordField)) {
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, move the user to the homepage
                                    Toast.makeText(CreateAccountActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name)
                                            .build();

                                    // Make a user class with data and send it to the database.
                                    User newUser = new User();
                                    newUser.setName(name);
                                    newUser.setEmail(email);
                                    newUser.setId(user.getUid());

                                    DatabaseReference newUserReference = firebaseDatabase.getReference().child("users").child(user.getUid());
                                    newUserReference.setValue(newUser);

                                    Intent intent = new Intent(CreateAccountActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

    }
    boolean validateFields(EditText nameField, EditText emailField, EditText passwordField, EditText confirmPasswordField) {

        // Check for valid email
        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(CreateAccountActivity.this, "Please enter a valid email address",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check that password is not empty and is greater than or equal to 8 characters
        String password = passwordField.getText().toString();
        if (password.length() < 7) {
            Toast.makeText(CreateAccountActivity.this, "Password must be at least 8 characters long",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check that password and confirm password fields match
        String confirmPassword = confirmPasswordField.getText().toString();
        if (!confirmPassword.equals(password)) {
            Toast.makeText(CreateAccountActivity.this, "Passwords do not match",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check that the name field is not empty
        String name = nameField.getText().toString();
        if (name.length() == 0) {
            Toast.makeText(CreateAccountActivity.this, "Please enter a name",
                    Toast.LENGTH_SHORT).show();
            return false;
        }


    return true;
    }
    public void logo_fade_in(){
        ImageView img = (ImageView)findViewById(R.id.logo);
        final Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        img.startAnimation(animFadeIn);

    }
}