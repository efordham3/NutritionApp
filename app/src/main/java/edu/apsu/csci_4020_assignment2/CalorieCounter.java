//Eliot Fordham
//Jacob Bassel
package edu.apsu.csci_4020_assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Text;

import edu.apsu.csci_4020_assignment2.Search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Thread.sleep;


public class CalorieCounter extends AppCompatActivity implements View.OnClickListener {

    Search foodSearch = new Search(this);
    ArrayList<String> arrayList=new ArrayList<>();
    double calories = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button searchButton = findViewById(R.id.search_button);
        ListView lv = findViewById(R.id.list_view);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = findViewById(R.id.calorie_text_view);
                String str = arrayList.get(i);
                String temp = foodSearch.runDetailsSearch(str.substring(0,6));
                calories += Double.parseDouble(temp);
                tv.setText(String.valueOf(calories));
            }
        });

        searchButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        ListView lv = findViewById(R.id.list_view);
        EditText et = findViewById(R.id.edit_text);
        foodSearch.runIdSearch(et.getText().toString());
        String data = foodSearch.getData();

        if (data == null) {
            Toast.makeText(this, "The search was unsuccessful, try another one", Toast.LENGTH_LONG).show();
            return;
        }

        String[] dataArr = data.split("\\r?\\n");

        arrayList.clear();
        arrayList.addAll(Arrays.asList(dataArr));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrayList);

        lv.setAdapter(adapter);
    }
}

