package com.example.myapplication;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PurchaseGoods extends AppCompatActivity {
    private String total;
    private TableLayout itemList;
    private double billTotal;
    private double inputValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_goods);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void CreateTaleRow(View view){
        itemList = findViewById(R.id.ItemsList);
        TableRow item = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        item.setLayoutParams(lp);

        TextView originalValue = CreateOriginalValueField(lp);
        TextView afterTaxValue = CreateAfterTaxValueField(lp);
        TextView currency = CreateCurrencyValueField(lp);
        ImageButton deleteButton = CreateDeleteButton();

        item.addView(originalValue);
        item.addView(afterTaxValue);
        item.addView(currency);
        item.addView(deleteButton);
        itemList.addView(item);

        AddToBillTotal();
        Toast.makeText(PurchaseGoods.this, "Created", Toast.LENGTH_SHORT).show();
    }

    private void AddToBillTotal() {
        double value = Double.parseDouble(total);
        billTotal += value;
        billTotal = RoundNumber(billTotal);

        updateBillTotalText();
    }

    private void updateBillTotalText() {
        TextView billTotalText = findViewById(R.id.BillTotal);
        billTotalText.setText("$" + Double.toString(billTotal));
    }

    private TextView CreateOriginalValueField(TableRow.LayoutParams lp) {
        TextView valueToAdd = new TextView(this);
        valueToAdd.setLayoutParams(lp);
        valueToAdd.setGravity(Gravity.CENTER);
        valueToAdd.setPadding(20,0,0,0);
        valueToAdd.setText(Double.toString(inputValue));
        return valueToAdd;
    }

    private TextView CreateAfterTaxValueField(TableRow.LayoutParams lp) {
        TextView valueToAdd = new TextView(this);
        valueToAdd.setLayoutParams(lp);
        valueToAdd.setGravity(Gravity.CENTER);
        valueToAdd.setPadding(20,0,0,0);
        valueToAdd.setText(total);
        return valueToAdd;
    }

    private TextView CreateCurrencyValueField(TableRow.LayoutParams lp) {
        TextView valueToAdd = new TextView(this);
        valueToAdd.setLayoutParams(lp);
        valueToAdd.setGravity(Gravity.CENTER);
        valueToAdd.setPadding(20,0,0,0);
        double value = Double.parseDouble(total);
        valueToAdd.setText(Double.toString(value));
        return valueToAdd;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private ImageButton CreateDeleteButton() {
        ImageButton deleteButton = new ImageButton(this);
        Drawable deleteImage = getResources().getDrawable(R.drawable.rectangle);
        Drawable imageBackground = getResources().getDrawable(android.R.color.transparent);
        deleteButton.setImageDrawable(deleteImage);
        deleteButton.setBackground(imageBackground);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteRow(v);
            }
        });
        return deleteButton;
    }

    public void DeleteRow(View view){

        TableRow parentRow = (TableRow) view.getParent();
        ViewGroup container = ((ViewGroup) parentRow.getParent());

        TextView textView = (TextView)parentRow.getChildAt(0);
        String valueToRemoveText = textView.getText().toString();
        double valueToRemove = Double.parseDouble(valueToRemoveText);

        RemoveFromBillTotal(valueToRemove);
        container.removeView(parentRow);
        container.invalidate();


        Toast.makeText(PurchaseGoods.this, "Deleted", Toast.LENGTH_SHORT).show();
    }

    private void RemoveFromBillTotal(Double value) {
        billTotal -= value;
        billTotal = RoundNumber(billTotal);

        updateBillTotalText();
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
    }

    public String CalculateTotal(double inputValue, double gstValue, double qstValue) {
        if (CheckIfValuesAreGreaterThanMaxValue(inputValue, gstValue, qstValue))
            return Double.toString(0.0);
        if (CheckIfGreaterThanZero(inputValue, gstValue, qstValue))
            return Double.toString(0.0);

        Double result = inputValue +  gstValue + qstValue;

        return String.format("%.2f", result);
    }

    public double CalculateGST(double inputValue) {
        if (CheckIfGreaterThanZero(inputValue)) return 0.0;

        double GST = inputValue * 0.05;
        double result = RoundNumber(GST);
        return result;
    }

    public double CalculateQST(double inputValue) {
        if (CheckIfGreaterThanZero(inputValue)) return 0.0;

        double QST = inputValue * 0.0975;
        double result = RoundNumber(QST);
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

    private double RoundNumber(double billTotal) {
        return (double) Math.round(billTotal * 100) / 100;
    }


}
