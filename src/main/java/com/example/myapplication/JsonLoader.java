package com.example.myapplication;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;

public class JsonLoader {
    private Context context;

    public JsonLoader(Context context) {
        this.context = context;
    }

    public Map<String, String> GetRates(){
        String jsonResponse = loadJSONFromAsset("Currencies.json");

        Gson gson = new GsonBuilder().create();
        Rates rates = gson.fromJson(jsonResponse, ConversionRates.class).getRates();
        //ToDo this might need some work as it converts back to json
        String ratesString = gson.toJson(rates);

        Type mapType = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> mapObj = gson.fromJson(ratesString, mapType);

        return mapObj;
    }

    public double GetCountryTax(){
        String jsonResponse = loadJSONFromAsset("Taxes.json");
        Gson gson = new GsonBuilder().create();
        Taxes taxes =  gson.fromJson(jsonResponse, Taxes.class);

        return taxes.getCountryTax();
    }

    public Map<String, String> GetProvinceTaxes(){
        String jsonResponse = loadJSONFromAsset("Taxes.json");

        Gson gson = new GsonBuilder().create();
        Taxes taxes = gson.fromJson(jsonResponse, Taxes.class);
        String taxesString = gson.toJson(taxes.Province);

        Type mapType = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> mapObj = gson.fromJson(taxesString, mapType);

        return mapObj;
    }
    //Load JSON file from Assets folder.
    private String loadJSONFromAsset(String fileName) {
        String json;
        try {
            InputStream is = context.getAssets().open(fileName);
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

