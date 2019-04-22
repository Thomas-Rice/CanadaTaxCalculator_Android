package com.example.myapplication;

public class Math {
    public static double  RoundNumber(double billTotal)
    {
        return (double) java.lang.Math.round(billTotal * 100) / 100;
    }

}
