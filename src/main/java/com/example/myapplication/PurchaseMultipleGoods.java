package com.example.myapplication;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class PurchaseMultipleGoods extends AppCompatActivity {

    private String total;
    private TableLayout itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_multiple_goods);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void CreateTaleRow(View view){
        itemList = findViewById(R.id.ItemsList);
        TableRow item = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        item.setLayoutParams(lp);

        TextView valueToAdd = CreateValueField(lp);
        TextView currency = CreateCurrencyValueField(lp);
        ImageButton deleteButton = CreateDeleteButton();

        item.addView(valueToAdd);
        item.addView(currency);
        item.addView(deleteButton);
        itemList.addView(item);

        Toast.makeText(PurchaseMultipleGoods.this, "Created", Toast.LENGTH_SHORT).show();
    }

    private TextView CreateCurrencyValueField(TableRow.LayoutParams lp) {
        TextView valueToAdd = new TextView(this);
        valueToAdd.setLayoutParams(lp);
        valueToAdd.setGravity(Gravity.START);
        valueToAdd.setPadding(20,0,0,0);
        double value = Double.parseDouble(total);
        valueToAdd.setText(Double.toString(value));
        return valueToAdd;
    }

    private TextView CreateValueField(TableRow.LayoutParams lp) {
        TextView valueToAdd = new TextView(this);
        valueToAdd.setLayoutParams(lp);
        valueToAdd.setGravity(Gravity.START);
        valueToAdd.setPadding(20,0,0,0);
        valueToAdd.setText(total);
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
        View parentRow = (View)view.getParent();
        ViewGroup container = ((ViewGroup) parentRow.getParent());
        container.removeView(parentRow);
        container.invalidate();

        Toast.makeText(PurchaseMultipleGoods.this, "Deleted", Toast.LENGTH_SHORT).show();
    }

    public void SetValues(View view) {
        TextView priceInputView = findViewById(R.id.priceInput);
        String text = priceInputView.getText().toString();
        SetAmount(text);

        double inputValue = Integer.parseInt(priceInputView.getText().toString());


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
        return inputValue * 0.05;
    }

    public double CalculateQST(double inputValue) {
        if (CheckIfGreaterThanZero(inputValue)) return 0.0;
        return inputValue * 0.0975;
    }

    private void SetAmount(String text) {
        TextView inputView = findViewById(R.id.amountValue);
        inputView.setText(text);
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
        TextView total = findViewById(R.id.totalValue);
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
