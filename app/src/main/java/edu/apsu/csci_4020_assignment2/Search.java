package edu.apsu.csci_4020_assignment2;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Search {
    private Context context;
    private Uri.Builder builder;
    private int id;
    private String data;

    public Search(){
        context = null;
    }
    public Search(Context current){
        this.context = current;//to access getResources()
    }

    public void setId(int num){
        id = num;
    }

    public String getData(){
        return data;
    }

    public String runIdSearch(String userText) {
        setUrl("search");
        builder.appendQueryParameter("generalSearchInput", userText);
        setIdBuilder();
        urlConnection();
        return data;
    }

    public String runDetailsSearch(){
        setUrl("" + id);
        setDetailsBuilder();
        urlConnection();
        return data;
    }

    private void urlConnection(){
        Background bg = new Background();
        bg.execute();
    }

    private class Background extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String result = null;
            try {
                URL url = new URL(builder.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                StringBuilder jsonSearchData = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    jsonSearchData.append(line);
                    jsonSearchData.append('\n');
                }

                if( builder.toString().startsWith("https://api.nal.usda.gov/fdc/v1/search")){
                    result = searchJsonParse(jsonSearchData);
                    Log.i("Hey", "The result data is" + result);
                }
                else{
                    detailsJsonParse();
                }

                br.close();
                connection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            data = result;
            return null;
        }
    }

    private void setUrl(String str){
        builder = Uri.parse("https://api.nal.usda.gov/fdc/v1/" + str).buildUpon();
        builder.appendQueryParameter("api_key", context.getResources().getString(R.string.api_key));
    }

    //Override to adjust the builder for details search
    private void setDetailsBuilder(){ }

    //Override to adjust the builder for id search
    private void setIdBuilder(){ }

    //Override to adjust the json parsing for the food id search
    private String searchJsonParse(StringBuilder jsonData){
        String res = "";
        try {
            JSONObject reader = new JSONObject(jsonData.toString());
            JSONArray foods = reader.getJSONArray("foods");

            for(int i = 0; i < foods.length(); i++){
                Log.i("Hey", "The length of the JSON array is" + foods.length());
                JSONObject food = foods.getJSONObject(i);
                String id = Integer.toString((food.getInt("fdcId")));
                String des = food.getString("description");
                String brand = food.getString("brandOwner");
                res += id + "-----" + des + "------" +  brand + "\n";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            res = null;
        }
        return res;
    }

    //Override to adjust the json parsing for the food details search
    private void detailsJsonParse() { }

}