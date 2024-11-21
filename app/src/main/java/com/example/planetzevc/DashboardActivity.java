package com.example.planetzevc;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.planetzevc.models.Question;
import com.example.planetzevc.models.User;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // 當前 login 的 User
        user = (User) getIntent().getSerializableExtra("user");

        // 首次登入 需要回答問題
        if ((user.answers == null) || (user.answers.size() == 0)) {
            Intent intent = new Intent(this,QuestionActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }

    }
}