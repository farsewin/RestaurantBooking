package com.example.restaurantbookingapp.auth;

<<<<<<< HEAD
import android.app.AlertDialog;
=======
>>>>>>> master
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

<<<<<<< HEAD
import androidx.annotation.NonNull;
=======
>>>>>>> master
import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantbookingapp.MainActivity;
import com.example.restaurantbookingapp.R;
<<<<<<< HEAD
=======
import com.example.restaurantbookingapp.RestaurantCardActivity;
>>>>>>> master
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Get views
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        Button loginButton = findViewById(R.id.loginButton);
        TextView registerText = findViewById(R.id.registerText);

        // Login logic
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

<<<<<<< HEAD
            if (TextUtils.isEmpty(email)) {
                showAlert("Please enter your email");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                showAlert("Please enter your password");
=======
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
>>>>>>> master
                return;
            }

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
<<<<<<< HEAD
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            String errorMsg = task.getException() != null ? task.getException().getMessage() : "Login failed.";
                            showAlert("Login failed: " + errorMsg);
=======
                            startActivity(new Intent(LoginActivity.this, RestaurantCardActivity.class));
                            finish();
                        } else {
                            String errorMsg = task.getException() != null ?
                                    task.getException().getMessage() : "Login failed.";
                            Toast.makeText(LoginActivity.this, "Login failed: " + errorMsg, Toast.LENGTH_LONG).show();
>>>>>>> master
                        }
                    });
        });

        // Redirect to register
<<<<<<< HEAD
        registerText.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
=======
        registerText.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
>>>>>>> master
    }
}