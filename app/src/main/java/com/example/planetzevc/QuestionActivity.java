package com.example.planetzevc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.planetzevc.models.Question;
import com.example.planetzevc.models.QuestionSet;
import com.example.planetzevc.models.User;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    private User user;

    private int currentQuestionIndex;   // 當前 要顯示的 Question

    private RadioGroup radioGroup;
    private List<RadioButton> ratioButtons;


    private TextView questionTopicTextView;

    private Button questionNextOrSubmitButton;
    private QuestionSet questionSet;

    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // 當前 login 的 User
        user = (User) getIntent().getSerializableExtra("user");

        currentQuestionIndex = 0;
        model = Model.getInstance();


        // 獲取 或有的 Questions
        questionSet = getQuestionSet();

        questionTopicTextView = findViewById(R.id.questionTopicTextView);
        radioGroup = findViewById(R.id.radioGroup);

        // get a list of Radio Buttons from the radioGroup
        ratioButtons = new ArrayList<>();
        for (int i = 0; i < radioGroup.getChildCount(); i++)
            ratioButtons.add((RadioButton) radioGroup.getChildAt(i));


        questionNextOrSubmitButton = findViewById(R.id.questionNextOrSubmitButton);
        questionNextOrSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 當前的問題
                Question question = questionSet.questions.get(currentQuestionIndex);

                // 獲取 是 按了 哪個 radio Button
                int selectedID = radioGroup.getCheckedRadioButtonId();

                RadioButton selectedButton = findViewById(selectedID);
                String selectedOption = (String) selectedButton.getText();
                user.answers.put(question.questionID, selectedOption);

                // Button 是 "Submit", 提交到 FireBase, 跳轉到頁面
                if (questionNextOrSubmitButton.getText().equals("Submit")) {
                    model.postUser(user, (Boolean created) -> {
                        if (!created) {
                            Toast.makeText(QuestionActivity.this, "Failed to Submit Questions!", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Toast.makeText(QuestionActivity.this, "Questions has been submitted successfully!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(QuestionActivity.this, DashboardActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);

                    });
                }
                // Button 是 "Next", 更新到下一題
                else {
//                    if (currentQuestionIndex == 0 && selectedOption.equals("No"))
//                        currentQuestionIndex = 3;
//                    else
                        currentQuestionIndex ++;
                    updateCurrentQuestion();
                }

            }
        });

        updateCurrentQuestion();
    }


    public QuestionSet getQuestionSet() {
        // TODO: 把這個改成 從 FireBase 獲取
        return new QuestionSet("qs1", new Question[] {
                new Question("q1", "Do you own or regularly use a car?", new String[]{"Yes", "No"}),
                new Question("q2", "What type of car do you drive?", new String[]{"Gasoline", "Diesel", "Hybrid", "Electric", "I don’t know"})
        });
    }

    private void updateCurrentQuestion() {
        // un-check 所有 radio buttons
        radioGroup.clearCheck();
        // 如果是最後一個問題了, button 應該顯示 "Submit", 否則 顯示 "Next"
        questionNextOrSubmitButton.setText((currentQuestionIndex == questionSet.questions.size() - 1) ? "Submit" : "Next");

        // 獲取 新的 Question 的 Topic 和 選項
        Question question = questionSet.questions.get(currentQuestionIndex);

        // 更新 頁面上的 Topic (Text View)
        questionTopicTextView.setText(question.topic);

        // 更新 頁面上的 option 選項 (Radio Buttons)
        for (int i = 0; i < ratioButtons.size(); i ++) {
            RadioButton radioButton = ratioButtons.get(i);

            // 有這個選項: 1.顯示出來 2.更新文字
            if (i < question.options.size()) {
                radioButton.setVisibility(View.VISIBLE);
                radioButton.setText(question.options.get(i));
            // 沒有這個選項, 不顯示出來
            } else {
                radioButton.setVisibility(View.GONE);
            }
        }
    }
}







