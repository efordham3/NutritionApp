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
                dataDownload = new StackExchangeDownload();
                dataDownload.execute();
            }
        });
    }

    private StackExchangeDownload dataDownload;


    private class StackExchangeDownload extends AsyncTask<Void, Void, ResultData> {
        @Override
        protected ResultData doInBackground(Void... voids) {
            ResultData resultData = new ResultData();
            TextView st = (TextView) findViewById(R.id.textView3);

            builder = Uri.parse("https://api.nal.usda.gov/fdc/v1/" + id).buildUpon();
            builder.appendQueryParameter("api_key", IdSearch.this.getResources().getString(R.string.api_key));
            double cal = 0;


            try {
                URL url = new URL(builder.toString());
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                StringBuilder jsonData = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    jsonData.append(line);
                }

                StringBuilder titleBuilder = new StringBuilder();

                JSONObject reader = new JSONObject(jsonData.toString());
                JSONArray nutrients = reader.getJSONArray("foodNutrients");
                for(int i = 0; i < nutrients.length(); i++){
                    JSONObject nutrient = nutrients.getJSONObject(i);
                    JSONObject nutrientType = nutrient.getJSONObject("nutrient");
                    String type = nutrientType.getString("name");
                    String type2 = nutrientType.getString("unitName");
                    titleBuilder.append(type + " --------- ");

                    cal = nutrient.getDouble("amount");
                    titleBuilder.append(cal + " " + type2);
                    titleBuilder.append("\n\n");
                }

                resultData.titleStr = titleBuilder.toString();

                connection.disconnect();
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
        protected void onPostExecute(ResultData resultData) {
            TextView tv = findViewById(R.id.textView);
            tv.setText(resultData.titleStr);
        }
    }

    private class ResultData {
        String titleStr = "";
    }
}