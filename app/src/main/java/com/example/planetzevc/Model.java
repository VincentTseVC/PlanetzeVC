package com.example.planetzevc;

import androidx.annotation.NonNull;

import com.example.planetzevc.models.Question;
import com.example.planetzevc.models.QuestionSet;
import com.example.planetzevc.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Model {
    private static Model instance;

    private DatabaseReference usersRef;
    private DatabaseReference questionSetsRef;
    private FirebaseAuth auth;

    private Model() {
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        questionSetsRef = FirebaseDatabase.getInstance().getReference("QuestionSets");
        auth = FirebaseAuth.getInstance();
    }

    public static Model getInstance() {
        if (instance == null)
            instance = new Model();
        return instance;
    }

    public void authenticate(String email, String password, Consumer<User> callback) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    callback.accept(null);
                }
                else {
                    usersRef.child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            callback.accept(user);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    public void register(String email, String password, Consumer<String> callback) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                System.out.println(task.isSuccessful());
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                callback.accept(task.isSuccessful() ? auth.getUid() : null);
            }
        });
    }

    public void postUser(User user, Consumer<Boolean> callback) {
        usersRef.child(user.userID).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println("@@@@@@ post User @@@@@@");
                System.out.println(task.isSuccessful());
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@");
                callback.accept(task.isSuccessful());
            }
        });
    }


    public void getUser(String userID, Consumer<User> callback) {
        usersRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                callback.accept(user);
            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {}
        });
    }

    public void postQuestionSet(QuestionSet questionSet, Consumer<Boolean> callback) {
        questionSetsRef.child(questionSet.questionSetID).setValue(questionSet).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.accept(task.isSuccessful());
            }
        });
    }

    public void getQuestionSet(String qsID, Consumer<QuestionSet> callback) {
        questionSetsRef.child(qsID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                QuestionSet qs = snapshot.getValue(QuestionSet.class);
                callback.accept(qs);
            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {}
        });
    }

}
