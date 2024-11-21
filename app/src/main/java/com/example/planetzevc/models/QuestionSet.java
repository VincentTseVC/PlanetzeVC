package com.example.planetzevc.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionSet {

    String questionSetID;

    public List<Question> questions;

    public QuestionSet() {
        questions = new ArrayList<>();
    }

    public QuestionSet(String questionSetID, Question[] questionsArr) {
        this.questionSetID = questionSetID;
        questions = Arrays.asList(questionsArr);
    }
}
