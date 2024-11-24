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
        boolean addToFireBase = false;
        if (addToFireBase) {
            questionSet = getQuestionSet();
            model.postQuestionSet(questionSet, (Boolean success) -> {});
            updateCurrentQuestion();
        } else {
            model.getQuestionSet("qs1", (QuestionSet questionSet) -> {
                this.questionSet = questionSet;
                updateCurrentQuestion();
            });
        }



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

                    if (currentQuestionIndex == 0 && selectedOption.equals("No")) {
                        currentQuestionIndex = 3;
                    }
                    else if( currentQuestionIndex ==3 && selectedOption.equals("Never")) {
                        currentQuestionIndex +=2;

                    }
                    else if( currentQuestionIndex ==5 && selectedOption.equals("None")) {
                        currentQuestionIndex +=2;

                    }
                    else if( currentQuestionIndex == 7 && !selectedOption.equals("Meat-based (eat all types of animal products)")) {
                        currentQuestionIndex +=2;

                    }
                    else {
                        currentQuestionIndex++;
                    }

                    updateCurrentQuestion();

                }

            }
        });


    }


    public QuestionSet getQuestionSet() {
        // TODO: 把這個改成 從 FireBase 獲取
        return new QuestionSet("qs1", new Question[] {
                new Question("q1", "Do you own or regularly use a car?", new String[]{"Yes", "No"}),
                new Question("q2", "What type of car do you drive?", new String[]{"Gasoline", "Diesel", "Hybrid", "Electric", "I don’t know"}),
                new Question("q3", "How many kilometers/miles do you drive per year?", new String[]{"Up to 5,000 km (3,000 miles)", "5,000–10,000 km (3,000–6,000 miles)","10,000–15,000 km (6,000–9,000 miles)","15,000–20,000 km (9,000–12,000 miles)" ,
                        "20,000–25,000 km (12,000–15,000 miles)","More than 25,000 km (15,000 miles)"}),
                new Question("q4", "How often do you use public transportation (bus, train,\n" +
                        "subway)", new String[]{"Never" , "Occasionally (1-2 times/week)" , "Frequently (3-4 times/week)" , "Always (5+ times/week)"}),
                new Question("q5", "How many short-haul flights (less than 1,500 km / 932 miles) have you\n" +
                        "taken in the past year?", new String[]{"Under 1 hour" , "1-3 hours" , "3-5 hours","5-10 hours" , "More than 10 hours"}),
                new Question("q6", ". How many short-haul flights (less than 1,500 km / 932 miles) have you\n" +
                        "taken in the past year?", new String[]{"None" , "1-2 flights", "3-5 flights", "6-10 flights", "More than 10 flights"}),
                new Question("q7", "How many long-haul flights (more than 1,500 km / 932 miles) have you\n" +
                        "taken in the past year?", new String[]{"None" , "1-2 flights", "3-5 flights", "6-10 flights", "More than 10 flights"}),
                new Question("q8", "What best describes your diet?", new String[]{"Vegetarian", "Vegan", "Pescatarian (fish/seafood)", "Meat-based (eat all types of animal product"}),

                new Question("q9_1", "How often do you eat the following animal-based products? (Beef)", new String[]{
                        "Beef",
                        "Pork",
                        "Chicken",
                        "Fish/Seafood"}),

                new Question("q10", "How often do you waste food or throw away uneaten leftovers?", new String[]{"Never","Rarely", "Occasionally", "Frequently"}),
                new Question("q11", "What type of home do you live in?", new String[]{"Detached house",
                        "Semi-detached house",
                        "Townhouse",
                        "Condo/Apartment",
                        "Other "}),
                new Question("q12", "How many people live in your household?", new String[]{"1",
                        "2",
                        "3-4" ,
                        "5 or more"}),
                new Question("q13", " What is the size of your home?", new String[]{"Under 1000 sq. ft.",
                        "1000-2000 sq. ft.",
                        "Over 2000 sq. ft"}),
                new Question("q14", "What type of energy do you use to heat your home?", new String[]{"Natural Gas",
                        "Electricity",
                        "Oil" ,
                        "Propane",
                        "Wood",
                        "Other"}),
                new Question("q15", "What is your average monthly electricity bill?", new String[]{"Under $50" ,
                        "$50-$100",
                        "$100-$150",
                        "$150-$200",
                        "Over $200"}),
                new Question("q16", "What type of energy do you use to heat water in your home?", new String[]{"Natural Gas",
                        "Electricity",
                        "Oil",
                        "Propane",
                        "Solar",
                        "Other"}),
                new Question("q17", "Do you use any renewable energy sources for electricity or heating (e.g., solar,wind)?", new String[]{"Yes, primarily (more than 50% of energy use)",
                        "Yes, partially (less than 50% of energy use)",
                        "No"}),
                new Question("q18", "How often do you buy new clothes?", new String[]{"Monthly",
                        "Quarterly",
                        "Annually",
                        "Rarely"}),
                new Question("q19", "Do you buy second-hand or eco-friendly products?", new String[]{"Yes, regularly",
                        "Yes, occasionally",
                        "No"}),
                new Question("q20", "How many electronic devices (phones, laptops, etc.) have you purchased in the\n" +
                        "past year?", new String[]{"None",
                        "1",
                        "2",
                        "3 or more"}),
                new Question("q21", "How often do you recycle?", new String[]{"Never",
                        "Occasionally",
                        "Frequently",
                        "Always"})

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







