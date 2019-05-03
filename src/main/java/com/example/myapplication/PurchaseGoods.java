package com.example.myapplication;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;


public class PurchaseGoods extends AppCompatActivity {
    private String total;
    private Boolean wasCalled = false;
    private double inputValue;
    private BillList billList;
    private double currencyConversionValue;
    private Map<String, String> conversionRates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_goods);
        BottomNavigationViewBehaviour();

        SetConversionRates("GBP");

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            if(bundle.getSerializable("test" ) != null){
                billList = (BillList) bundle.getSerializable("test");
            }
        }
        else{
            billList = new BillList();
            billList.Bills = new ArrayList<>();
            billList.Total = "0";
        }
        SetBillTotalText();
    }

    private void SetConversionRates(String gbp) {
        JsonLoader CurrencyConversions = new JsonLoader(this);
        conversionRates = CurrencyConversions.GetRates();

        currencyConversionValue = Double.parseDouble(conversionRates.get(gbp));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("wasCalled", wasCalled);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        wasCalled = savedInstanceState.getBoolean("wasCalled");
    }

    public void BottomNavigationViewBehaviour(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.BottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bill:
                                Intent intent = new Intent(PurchaseGoods.this, BillActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("test", billList);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                        }
                        return true;
                    }
                });
    }

    public void CreateBillItem(View view){
        int size = billList.Bills.size() + 1;
        Bill billItem = new Bill();
        billItem.OriginalValue = Double.toString(inputValue);
        billItem.id = size;
        billItem.ValueAfterTax = total;
        billItem.CurrencyConvertedValue = "£15";
        billList.Bills.add(billItem);

        //TODO don't keep converting back and forth
        double total = Double.parseDouble(billList.Total);
        double afterTax = Double.parseDouble(billItem.ValueAfterTax);
        double newTotal = total += afterTax;
        billList.Total = String.format("%.2f", newTotal);

        SetBillTotalText();
    }

    public void SetValues(View view) {
        TextView priceInputView = findViewById(R.id.priceInput);

        inputValue = Double.parseDouble(priceInputView.getText().toString());

        double gstValue = CalculateGST(inputValue);
        SetGSTText(gstValue);

        double qstValue = CalculateQST(inputValue);
        SetQSTText(qstValue);

        total = CalculateTotal(inputValue, gstValue, qstValue);
        SetTotalText(total);
        SetGBPText();
    }

    public void SetBillTotalText(){
        TextView billTotalText = findViewById(R.id.BillTotalValue);
        billTotalText.setText(billList.Total);
    }

    public void SetTotalWithTipText(View view){
        double tip =  CalculateAddTip();
        total = String.format("%.2f", tip);
        SetTotalText(total);
    }

    //TODO add the option to choose tip amount
    public double CalculateAddTip(){
        double totalValue = Double.parseDouble(total);
        return totalValue * 1.1;
    }

    public void SetGBPText(){
        TextView currencyValue = findViewById(R.id.currencyValue);
        double value = ConvertGBP();
        currencyValue.setText(String.format("%.2f", value));
    }

    public double ConvertGBP(){
        return Double.parseDouble(total) * currencyConversionValue;
    }

    public String CalculateTotal(double inputValue, double gstValue, double qstValue) {
        if (CheckIfValuesAreGreaterThanMaxValue(inputValue, gstValue, qstValue))
            return Double.toString(0.0);
        if (CheckIfGreaterThanZero(inputValue, gstValue, qstValue))
            return Double.toString(0.0);

        double result = inputValue +  gstValue + qstValue;

        return String.format("%.2f", result);
    }

    public double CalculateGST(double inputValue) {
        if (CheckIfGreaterThanZero(inputValue)) return 0.0;

        double GST = inputValue * 0.05;
        double result = Math.RoundNumber(GST);
        return result;
    }

    public double CalculateQST(double inputValue) {
        if (CheckIfGreaterThanZero(inputValue)) return 0.0;

        double QST = inputValue * 0.0975;
        double result = Math.RoundNumber(QST);
        return result;
    }

    public void SetGSTText(double inputValue){
        TextView gstview = findViewById(R.id.gstValue);
        gstview.setText(Double.toString(inputValue));
    }

    public void SetQSTText(double inputValue){
        TextView qstview = findViewById(R.id.qstValue);
        qstview.setText(Double.toString(inputValue));
    }

    public void SetTotalText(String inputString){
        TextView total = findViewById(R.id.total);
        total.setText("$" + inputString);
    }

    private boolean CheckIfGreaterThanZero(double inputValue) {
        if(inputValue <= 0.0){
            return true;
        }
        return false;
    }

    private boolean CheckIfGreaterThanZero(double inputValue, double gstValue, double qstValue) {
        if(inputValue <= 0.0){
            return true;
        }
        if(gstValue <= 0.0){
            return true;
        }
        if(qstValue <= 0.0){
            return true;
        }
        return false;
    }

    private boolean CheckIfValuesAreGreaterThanMaxValue(double inputValue, double gstValue, double qstValue) {
        if(inputValue > Integer.MAX_VALUE && gstValue > Integer.MAX_VALUE && qstValue > Integer.MAX_VALUE){
            return true;
        }
        return false;
    }
}
