package com.example.myapplication;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PurchaseGoods extends AppCompatActivity {
    private String total = "0.00";
    private Boolean wasCalled = false;
    private double inputValue = 0.0;
    private BillList billList;
    private double currencyConversionRate;
    private String currencyConversionCountry;
    private double currencyConversionValue;
    private Map<String, String> taxes;
    private double countryTax;
    private Map<String, String> conversionRates;
    private String province;
    private JsonLoader jsonLoader;

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager viewPager;
    private SharedViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.background);

        jsonLoader = new JsonLoader(this);
        LoadProvinces(jsonLoader);

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


        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        mViewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
    }


    private void LoadProvinces(JsonLoader jsonLoader) {
        Spinner provinceSpinner = findViewById(R.id.provincesSpinner3);
        taxes = jsonLoader.GetProvinceTaxes();
        countryTax = jsonLoader.GetCountryTax();
        final List<String> provinces = new ArrayList(taxes.keySet());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinneritem, provinces);
        dataAdapter.setDropDownViewResource(R.layout.spinneritem);
        provinceSpinner.setAdapter(dataAdapter);

        provinceSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                                province = provinces.get(position);
//                                SetValues(view);
                    }

                    public void onNothingSelected(AdapterView<?> parent) { }
                });
    }

    public void showPopupCurrencyConversion(View view){
        ImageView currencyButton = findViewById(R.id.currencyIcon);
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(PurchaseGoods.this, currencyButton);

        conversionRates = jsonLoader.GetRates();
        for (String rate : conversionRates.keySet())
        {
            popup.getMenu().add(0, 0,0, rate);
        }

        popup.getMenuInflater().inflate(R.menu.currency_popup, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                currencyConversionRate = Double.parseDouble(conversionRates.get(item.toString()));
                currencyConversionCountry = item.toString();

                SetSelectedCurrencyText();
                return true;
            }
        });
        popup.show();
    }

    private void SetSelectedCurrencyText() {
        TextView currencyText = findViewById(R.id.currencyText);
        currencyText.setVisibility(View.VISIBLE);
        currencyText.setText(currencyConversionCountry + ":");

        TextView currencyValue = findViewById(R.id.currencyValue);
        if(currencyConversionCountry != null)
        {
            setCurrencyValueText();
            currencyValue.setVisibility(View.VISIBLE);
        }
    }

    public void showPopup(final View view){
            Button tipButton = findViewById(R.id.AddTipButton);
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(PurchaseGoods.this, tipButton);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.tip_popup, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    SetTotalWithTipText(item.getTitle().toString());
                    setCurrencyValueText();
                    return true;
                }
            });

            popup.show();//showing popup menu
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

    public void CreateBillItem(View view){
        int size = billList.Bills.size() + 1;
        Bill billItem = new Bill();
        billItem.OriginalValue = Double.toString(inputValue);
        billItem.id = size;
        billItem.ValueAfterTax = total;
        billItem.CurrencyConvertedValue = currencyConversionValue;
        billList.Bills.add(billItem);
        mViewModel.AddToBillList(billItem);

        //TODO don't keep converting back and forth
        double total = Double.parseDouble(billList.Total);
        double afterTax = Double.parseDouble(billItem.ValueAfterTax);
        double newTotal = total + afterTax;
        billList.Total = String.format("%.2f", newTotal);

        SetBillTotalText();
    }



    public void SetValues(View view) {
        TextView priceInputView = findViewById(R.id.priceInput);

        //This is to counteract error on startup
        //Todo try to stop needing this
        String priceInput = priceInputView.getText().toString();
        if(!priceInput.equals(""))
        {
            inputValue = Double.parseDouble(priceInput);
        }

        double gstValue = CalculateCanadaTax(inputValue);
        SetProvinceText(gstValue);

        double qstValue = CalculateProvinceTax(inputValue, province);
        SetCanadaText(qstValue);

        total = CalculateTotal(inputValue, gstValue, qstValue);
        SetTotalText(total);
        setCurrencyValueText();
    }

    public void SetBillTotalText(){
        TextView billTotalText = findViewById(R.id.BillTotalValue);
        billTotalText.setText(mViewModel.GetTotal());
    }

    public void SetTotalWithTipText(String percentage){
        double tip =  CalculateAddTip(percentage);
        total = String.format("%.2f", tip);
        SetTotalText(total);
    }

    //TODO add the option to choose tip amount
    public double CalculateAddTip(String tipValue){
        double totalValue = Double.parseDouble(total);
        String tip = tipValue.replace("%", "");
        double tipPercent = ConvertTipPercentageToValue(tip) + 1;
        return totalValue * tipPercent;
    }

    private double ConvertTipPercentageToValue(String tipValue) {
        return Double.parseDouble(tipValue)/100;
    }

    public void setCurrencyValueText(){
        TextView currencyValue = findViewById(R.id.currencyValue);
        double value = ConvertCurrency();
        currencyValue.setText(String.format("%.2f", value));
    }

    public double ConvertCurrency(){
        return Double.parseDouble(total) * currencyConversionRate;
    }

    public String CalculateTotal(double inputValue, double gstValue, double qstValue) {
        double result = inputValue +  gstValue + qstValue;

        return String.format("%.2f", result);
    }

    public double CalculateCanadaTax(double inputValue) {
        if (CheckIfGreaterThanZero(inputValue)) return 0.0;
        double GST = inputValue * (countryTax/100);
        double result = Math.RoundNumber(GST);
        return result;
    }

    public double CalculateProvinceTax(double inputValue, String province) {
        if (CheckIfGreaterThanZero(inputValue)) return 0.0;

        double tax = Double.parseDouble(taxes.get(province));
        double QST = inputValue * (tax/100);
        double result = Math.RoundNumber(QST);
        return result;
    }

    public void SetProvinceText(double inputValue){
        TextView provinceValueView = findViewById(R.id.provinceValue);
        provinceValueView.setText("$" + Double.toString(inputValue));
    }

    public void SetCanadaText(double inputValue){
        TextView canadaValueView = findViewById(R.id.canadaValue);
        canadaValueView.setText("$" + Double.toString(inputValue));
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

    public void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.AddFragment(new MainPageFragment(), "Converter");
        adapter.AddFragment(new BillFragment(), "Bill");
        viewPager.setAdapter(adapter);
    }
}
