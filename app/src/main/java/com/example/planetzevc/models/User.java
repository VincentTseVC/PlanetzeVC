package com.example.planetzevc.models;

import com.example.planetzevc.MainActivity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    public String email;
    public String name;
    public String userID;

    public Map<String, String> answers;


    public User() {
        this.answers = new HashMap<>();
    }

    public User (String userID, String email, String name) {
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.answers = new HashMap<>();
    }
}
