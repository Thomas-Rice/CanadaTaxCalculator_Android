package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class BillActivity extends AppCompatActivity {

    private BillList billListData;
    private TableLayout itemList;
    private double billTotal;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_activity);

        BottomNavigationViewBehaviour();

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        billListData = (BillList) bundle.getSerializable("test");

        Test();
    }

    public void BottomNavigationViewBehaviour(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.BottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.calculator:
                                Intent intent = new Intent(BillActivity.this, PurchaseGoods.class);

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("test", billListData);
                                intent.putExtras(bundle);

                                startActivity(intent);
                                break;
                        }
                        return true;
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void Test(){
        for (Bill bill : billListData.Bills){
            CreateTableRow(bill.OriginalValue, bill.ValueAfterTax, bill.CurrencyConvertedValue);
            billTotal += Double.parseDouble(bill.ValueAfterTax);
        }
        updateBillTotalText();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void CreateTableRow(String originalValue, String afterTaxValue, String currencyConvertedValue){
        itemList = findViewById(R.id.test);
        TableRow item = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        item.setLayoutParams(lp);

        TextView originalValueView = CreateOriginalValueField(lp, originalValue);
        TextView afterTaxValueView = CreateAfterTaxValueField(lp, afterTaxValue);
        TextView currencyView = CreateCurrencyValueField(lp, currencyConvertedValue);
        ImageButton deleteButton = CreateDeleteButton();

        item.addView(originalValueView);
        item.addView(afterTaxValueView);
        item.addView(currencyView);
        item.addView(deleteButton);
        itemList.addView(item);

        Toast.makeText(BillActivity.this, "Created", Toast.LENGTH_SHORT).show();
    }


    private TextView CreateOriginalValueField(TableRow.LayoutParams lp, String inputValue) {
        TextView valueToAdd = new TextView(this);
        valueToAdd.setLayoutParams(lp);
        valueToAdd.setGravity(Gravity.CENTER);
        valueToAdd.setPadding(20,0,0,0);
        valueToAdd.setText(inputValue);
        return valueToAdd;
    }

    private TextView CreateAfterTaxValueField(TableRow.LayoutParams lp, String inputValue) {
        TextView valueToAdd = new TextView(this);
        valueToAdd.setLayoutParams(lp);
        valueToAdd.setGravity(Gravity.CENTER);
        valueToAdd.setPadding(20,0,0,0);
        valueToAdd.setText(inputValue);
        return valueToAdd;
    }

    private TextView CreateCurrencyValueField(TableRow.LayoutParams lp, String currencyConvertedValue) {
        TextView valueToAdd = new TextView(this);
        valueToAdd.setLayoutParams(lp);
        valueToAdd.setGravity(Gravity.CENTER);
        valueToAdd.setPadding(20,0,0,0);
        valueToAdd.setText(currencyConvertedValue);
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

    private void updateBillTotalText() {
        TextView billTotalText = findViewById(R.id.BillTotal);
        billTotal = Math.RoundNumber(billTotal);
        billTotalText.setText("$" + Double.toString(billTotal));
    }

    public void DeleteRow(View view){

        TableRow parentRow = (TableRow) view.getParent();
        ViewGroup container = ((ViewGroup) parentRow.getParent());

        TextView textView = (TextView)parentRow.getChildAt(1);
        String valueToRemoveText = textView.getText().toString();
        double valueToRemove = Double.parseDouble(valueToRemoveText);

        RemoveFromBillTotal(valueToRemove);
        container.removeView(parentRow);
        container.invalidate();


        Toast.makeText(BillActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
    }

    private void RemoveFromBillTotal(Double value) {
        billTotal -= value;
        billTotal = Math.RoundNumber(billTotal);

//        billListData.Bills.remove();

//        When create row set an id and then delete from list via id
        updateBillTotalText();
    }


}
