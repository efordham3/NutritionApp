package edu.apsu.csci_4020_assignment2;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.TreeSet;

import javax.net.ssl.HttpsURLConnection;

public class IdSearch extends AppCompatActivity {

    private Uri.Builder builder;
    private Context context;
    String id;
    private FoodGroupDownload downloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_search);
        EditText et = (EditText) findViewById(R.id.edit_text);
        TextView st = (TextView) findViewById(R.id.textView3);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.edit_text);
                id = et.getText().toString();
                downloader = new FoodGroupDownload();
                downloader.execute();
            }
        });
    }

    public void URLCreator(String id) {
        TextView st = (TextView) findViewById(R.id.textView3);

        builder = Uri.parse("https://api.nal.usda.gov/fdc/v1/" + id).buildUpon();
        builder.appendQueryParameter("api_key", IdSearch.this.getResources().getString(R.string.api_key));
    }


    private class FoodGroupDownload extends AsyncTask<Void, Void, DataFinder> {
        @Override
        protected DataFinder doInBackground(Void... voids) {
            DataFinder resultData = new DataFinder();
            TextView st = (TextView) findViewById(R.id.textView3);

            URLCreator(id);

            double cal = 0;


            try {
                URL url = new URL(builder.toString());

                HttpsURLConnection connectionCreation = (HttpsURLConnection) url.openConnection();

                InputStream thing = connectionCreation.getInputStream();
                InputStreamReader inputstream = new InputStreamReader(thing);
                BufferedReader buffer = new BufferedReader(inputstream);

                StringBuilder jsonParcer = new StringBuilder();
                String line;
                while ((line = buffer.readLine()) != null) {
                    jsonParcer.append(line);
                }

                StringBuilder titleBuilder = new StringBuilder();

                JSONObject reader = new JSONObject(jsonParcer.toString());
                JSONArray nutrients = reader.getJSONArray("foodNutrients");
                for(int i = 0; i < nutrients.length(); i++){
                    JSONObject nutrient = nutrients.getJSONObject(i);
                    JSONObject nutrientType = nutrient.getJSONObject("nutrient");
                    String type = nutrientType.getString("name");
                    String type2 = nutrientType.getString("unitName");
                    titleBuilder.append(type + " --------- ");

                    cal = nutrient.getDouble("amount");
                    titleBuilder.append(cal + " " + type2);
                    titleBuilder.append("\n ------------------------------------------------------------------ \n");
                }

                resultData.titleStr = titleBuilder.toString();

                connectionCreation.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return resultData;
        }

        @Override
        protected void onPostExecute(DataFinder resultData) {
            TextView tv = findViewById(R.id.textView);
            tv.setText(resultData.titleStr);


        }
    }

    private class DataFinder {
        String titleStr = "";
    }
}