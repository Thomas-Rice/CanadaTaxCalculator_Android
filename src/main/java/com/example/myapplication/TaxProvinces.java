package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TaxProvinces implements Serializable {
    private float Alberta;
    @SerializedName("British Columbia")
    private float BritishColumbia;
    private float Manitoba;
    @SerializedName("New-Brunswick")
    private float NewBrunswick;
    @SerializedName("Newfoundland and Labrador")
    private float NewfoundlandNndLabrador;
    @SerializedName("Northwest Territories")
    private float NorthwestTerritories;
    @SerializedName("Nova Scotia")
    private float NovaScotia;
    private float Nunavut;
    private float Ontario;
    @SerializedName("Prince Edward Island")
    private float PrinceEdwardIsland;
    private float Quebec;
    private float Saskatchewan;
    private float Yukon;

}
