package com.example.myapplication;


import java.io.Serializable;

public class Taxes implements Serializable {

        private double Country;
        TaxProvinces Province;

        public TaxProvinces getTaxes() {
            return Province;
        }
        public double getCountryTax() { return Country; }
    }

