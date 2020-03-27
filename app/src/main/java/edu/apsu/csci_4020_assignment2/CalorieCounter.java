package edu.apsu.csci_4020_assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.apsu.csci_4020_assignment2.Search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.Thread.sleep;

public class CalorieCounter extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button searchButton = (Button) findViewById(R.id.search_button);

        searchButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        Search foodSearch = new Search(this);
        EditText et = (EditText) findViewById(R.id.edit_text);
        TextView tv = (TextView) findViewById(R.id.text_view);
        foodSearch.runIdSearch(et.getText().toString());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        String data = foodSearch.getData();
        tv.setText(data);
    }

}
