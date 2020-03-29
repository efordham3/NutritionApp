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
    private String data;

    public Search(){
        context = null;
    }

    public Search(Context current){
        this.context = current;//to access getResources()
    }

    public String getData(){
        return data;
    }

    public String runIdSearch(String userText) {
        data = null;
        setUrl("search");
        builder.appendQueryParameter("generalSearchInput", userText);
        builder.appendQueryParameter("requireAllWords", "false");
        builder.appendQueryParameter("includeDataTypeList", "Branded");
        setIdBuilder();
        urlConnection();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data;

    }

    public String runDetailsSearch(String id){
        setUrl("" + id);
        setDetailsBuilder();
        urlConnection();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                Log.i("Hey", "The url is " + url);
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
                }
                else{
                    result = detailsJsonParse(jsonSearchData);
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
    private void setDetailsBuilder(){

    }

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
                String brand = food.getString("brandOwner");
                String des = food.getString("description");
                res += id + "--" + des + "--" +   brand + "\n";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            res = null;
        }
        return res;
    }

    //Override to adjust the json parsing for the food details search
    private String detailsJsonParse(StringBuilder jsonData) {
        double cal = 0;
        try {
            JSONObject reader = new JSONObject(jsonData.toString());
            JSONArray nutrients = reader.getJSONArray("foodNutrients");
            for(int i = 0; i < nutrients.length(); i++){
                JSONObject nutrient = nutrients.getJSONObject(i);
                JSONObject nutrientType = nutrient.getJSONObject("nutrient");
                String type = nutrientType.getString("name");
                if(type.equals("Energy")){
                    cal = nutrient.getDouble("amount");
                    break;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();

        }
        return String.valueOf(cal);
    }

}