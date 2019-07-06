package com.example.myapplication;

import java.io.Serializable;

class Bill implements Serializable {
    public String OriginalValue;
    public String ValueAfterTax;
    public double CurrencyConvertedValue;
    public int id;
}

