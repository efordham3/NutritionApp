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
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.edit_text);
                id = et.getText().toString();
                doDownload();
            }
        });
    }

    private StackExchangeDownload dataDownload;

    private void doDownload() {
        if (dataDownload == null) {
            dataDownload = new StackExchangeDownload();
            dataDownload.execute();
        }
    }


    private class StackExchangeDownload extends AsyncTask<Void, Void, ResultData> {
        @Override
        protected ResultData doInBackground(Void... voids) {
            ResultData resultData = new ResultData();

            builder = Uri.parse("https://api.nal.usda.gov/fdc/v1/" + id).buildUpon();
            builder.appendQueryParameter("api_key", IdSearch.this.getResources().getString(R.string.api_key));

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
                // changed HashSet to TreeSet so that the results are sorted.
                TreeSet<String> taglist = new TreeSet<>();

                JSONObject reader = new JSONObject(jsonData.toString());
                JSONObject items = reader.getJSONObject("labelNutrients");



                titleBuilder.append(items);

                titleBuilder.toString();



                StringBuilder tagBuilder = new StringBuilder();

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



            dataDownload = null;
        }
    }

    private class ResultData {
        String titleStr = "";
        String tagStr = "";
    }
}