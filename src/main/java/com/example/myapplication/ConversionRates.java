package com.example.myapplication;


import java.io.Serializable;

public class ConversionRates implements Serializable {

        private String base;
        Rates rates;
        private String date;


        public Rates getRates() {
            return rates;
        }
    }

