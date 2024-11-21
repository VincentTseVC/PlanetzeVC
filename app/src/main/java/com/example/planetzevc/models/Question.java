package com.example.planetzevc.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Question {

    public String questionID;
    public String topic;
    public List<String> options;

    public Question() {
        options = new ArrayList<>();
    }

    public Question(String questionID, String topic, String[] optionsArr) {
        this.questionID = questionID;
        this.topic = topic;
        this.options = Arrays.asList(optionsArr);
    }
}
