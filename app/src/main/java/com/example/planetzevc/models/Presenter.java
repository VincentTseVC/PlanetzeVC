package com.example.planetzevc.models;

import com.example.planetzevc.MainActivity;
import com.example.planetzevc.Model;

public class Presenter {
    private Model model;
    private MainActivity view;

    public Presenter(Model model, MainActivity view) {
        this.model = model;
        this.view = view;
    }

    // API
    public void login(String email, String password) {

        model.authenticate(email, password, (User user) -> {
            if (user == null) view.failedToLogin();
            else view.redirectToDashboard(user.userID);
        });
    }
}
