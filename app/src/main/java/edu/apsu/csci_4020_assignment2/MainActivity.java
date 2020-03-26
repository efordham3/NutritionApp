package edu.apsu.csci_4020_assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void foodSearch(){
        Uri.Builder builder = Uri.parse("https://api.nal.usda.gov/fdc/v1/search?api_key=").buildUpon();
        builder.appendQueryParameter("api_key", getResources().getString(R.string.api_key));
        builder.appendQueryParameter("generalSearchInput", "pepperoni pizza");

        try {
            URL url = new URL(builder.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
}
