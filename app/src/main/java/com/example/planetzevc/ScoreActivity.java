package com.example.planetzevc;

import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.planetzevc.models.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class ScoreActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        User user = (User) getIntent().getSerializableExtra("user");


//
//        Map<String, String> answers = user.answers;
//
//        if (answers.get("q2").equals("Gasoline")) {
//            // ...
//        }

        // ========
        Map<String, Float> countryAvgs = HashMapFromTextFile();


    }


    public Map<String, Float> HashMapFromTextFile()
    {

        Map<String, Float> map = new HashMap<>();
        BufferedReader br = null;

        try {

            // create file object
            File file = new File("Global_Averages.csv");
            System.out.println(file.getAbsolutePath());

            InputStream inputStream = getAssets().open("Global_Averages.csv");

            // create BufferedReader object from the File
            br = new BufferedReader(new FileReader(file));

            String line = null;

            // read file line by line
            while ((line = br.readLine()) != null) {

                // split the line by :
                String[] parts = line.split(",");

                // first part is name, second is number
                String country = parts[0].trim();
                Float avg = Float.parseFloat(parts[1].trim());
                map.put(country, avg);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {

            // Always close the BufferedReader
            if (br != null) {
                try {
                    br.close();
                }
                catch (Exception e) {
                };
            }
        }

        return map;
    }

}