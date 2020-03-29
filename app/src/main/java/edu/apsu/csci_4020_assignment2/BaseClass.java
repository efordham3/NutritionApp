package edu.apsu.csci_4020_assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BaseClass extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_class);

        TextView text1 = findViewById(R.id.textView2);
        text1.setOnClickListener(this);

        TextView text2 = findViewById(R.id.textView7);
        text2.setOnClickListener(this);

        Button button = findViewById(R.id.button3);
        button.setOnClickListener(this);

        Button button2 = findViewById(R.id.button4);
        button2.setOnClickListener(this);

        text1.setText("Welcome to our calorie counter app!");
        text2.setText("This app will allow you to calculate the amount of calories in a given set of food.  You can look for your food and add them to your calorie" +
                " counter!  Click on the first button to do this and to find the corresponding ID.  You can also view our nutritional counter for each food.  This will require that you know your food's respective ID." +
                " from the calorie counter.");



    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.button3) {

            Intent i=new Intent(getApplicationContext(),CalorieCounter.class);
            startActivity(i);
        }

        else if (view.getId() == R.id.button4) {

            Intent i=new Intent(getApplicationContext(),IdSearch.class);
            startActivity(i);
        }
    }
}
