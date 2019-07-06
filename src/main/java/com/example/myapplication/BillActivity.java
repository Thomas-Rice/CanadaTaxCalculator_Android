package com.example.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class BillActivity extends AppCompatActivity {

    private BillList billListData;
    private double billTotal;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_activity);


        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        billListData = (BillList) bundle.getSerializable("test");

        SetGBPText();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        sendBundle();
    }

    private void sendBundle() {
        Intent intent = new Intent(BillActivity.this, PurchaseGoods.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("test", billListData);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    public void SetGBPText(){
        TextView currencyValue = findViewById(R.id.currencyValue);
        double value = ConvertGBP();
        currencyValue.setText(String.format("%.2f", value));
    }

    public double ConvertGBP(){
        return Double.parseDouble(billListData.Total) * 0.575304;
    }

    private void updateBillTotalText() {
        TextView billTotalText = findViewById(R.id.BillTotal);
        billTotal = Math.RoundNumber(billTotal);
        billTotalText.setText("$" + Double.toString(billTotal));
    }



}
