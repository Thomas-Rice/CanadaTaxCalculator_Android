package com.example.myapplication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonResponse {
        public ConversionRates parseJSON(String response) {
            Gson gson = new GsonBuilder().create();
            ConversionRates responseObject = gson.fromJson(response, ConversionRates.class);
            return responseObject;
        }
}
