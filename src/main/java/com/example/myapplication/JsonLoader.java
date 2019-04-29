package com.example.myapplication;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;

public class JsonLoader {
    private Context context;

    public JsonLoader(Context context) {
        this.context = context;
    }

    public void test(){
        String jsonResponse = loadJSONFromAsset();
        Gson gson = new GsonBuilder().create();
        ConversionRates.Rates foodMenuJsonResponse = gson.fromJson(jsonResponse, ConversionRates.Rates.class);
        System.out.println(foodMenuJsonResponse);
    }

    //Load JSON file from Assets folder.
    private String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = context.getAssets().open("Currencies.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}

