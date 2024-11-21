package com.example.planetzevc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.planetzevc.models.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    // UI
    private EditText nameEditText, emailEditText, passowordEditText, confirmPasswordEditText;
    private Button  registerButton;
    private ImageView logoImageView;

    private Model model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // get UI
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passowordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmpasswordEditText);
        logoImageView = findViewById(R.id.logoImageView);
        registerButton = findViewById(R.id.registerButton);

        logoImageView.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        model = Model.getInstance();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view == logoImageView) {
            // go back to home page
            startActivity(new Intent(this, MainActivity.class));
        }

        if (view == registerButton) {
            register();
        }
    }

    private void register() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passowordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();


        // validate
        if (name.isEmpty()) {
            nameEditText.setError("Name is required!");
            nameEditText.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please provide valid email!");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passowordEditText.setError("password is required!");
            passowordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passowordEditText.setError("Min password length should be 6 characters!");
            passowordEditText.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Password must be the same!");
            confirmPasswordEditText.requestFocus();
        }


        model.register(email, password, (String userID) -> {
            if (userID == null) {
                Toast.makeText(RegisterActivity.this, "Failed to register", Toast.LENGTH_LONG).show();
                return;
            }
            // Toast.makeText(RegisterActivity.this, "registered", Toast.LENGTH_LONG).show();

            User user = new User(userID, email, name);
            model.postUser(user, (Boolean created) -> {
                if (!created) {
                    Toast.makeText(RegisterActivity.this, "Failed to create a user!", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(RegisterActivity.this, "user has been registered successfully!", Toast.LENGTH_LONG).show();

                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            });
        });
    }
}