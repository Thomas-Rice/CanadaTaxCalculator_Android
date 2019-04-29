package com.example.myapplication;


public class ConversionRates {

        private String base;
        Rates RatesObject;
        private String date;


        // Getter Methods

        public String getBase() {
            return base;
        }

        public Rates getRates() {
            return RatesObject;
        }

        public String getDate() {
            return date;
        }

        // Setter Methods

        public void setBase(String base) {
            this.base = base;
        }

        public void setRates(Rates ratesObject) {
            this.RatesObject = ratesObject;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

