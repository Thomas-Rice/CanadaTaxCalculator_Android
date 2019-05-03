package com.example.myapplication;


import java.io.Serializable;

public class ConversionRates implements Serializable {

        private String base;
        Rates rates;
        private String date;


        // Getter Methods

        public String getBase() {
            return base;
        }

        public Rates getRates() {
            return rates;
        }

        public String getDate() {
            return date;
        }

        // Setter Methods

        public void setBase(String base) {
            this.base = base;
        }

        public void setRates(Rates ratesObject) {
            this.rates = ratesObject;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

