package com.example.myapplication;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class PurchaseGoods extends AppCompatActivity {
    private String total;
    private Boolean wasCalled = false;
    private double inputValue;
    private BillList billList = null;
    private final static String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_goods);
        BottomNavigationViewBehaviour();

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
        }

        Log.i(TAG, "On Create .....");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "On Destroy .....");
    }
    /* (non-Javadoc)
     * @see android.app.Activity#onPause()
     */

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "On Stop .....");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "On Pause .....");
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onRestart()
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "On Restart .....");
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "On Resume .....");
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onStart()
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "On Start .....");
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
                                CreateTaleRowValue();
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

    public void CreateTaleRowValue(){
        Bill test = new Bill();
        test.OriginalValue = Double.toString(inputValue);
        test.ValueAfterTax = total;
        test.CurrencyConvertedValue = "£15";
        billList.Bills.add(test);
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
