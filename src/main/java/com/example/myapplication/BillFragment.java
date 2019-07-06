package com.example.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class BillFragment extends Fragment {
    private SharedViewModel model;
    private BillList billListData;
    private double billTotal;
    private TableLayout itemList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.billfagment, container, false);

        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            billListData = model.GetBillList();
            UpdateBillTotal();
            Toast.makeText(getContext(), "SetMenuVisibility" + billListData.Bills.size(), Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        billListData = model.billList;

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void UpdateBillTotal(){
        RemoveAllRows();
        for (Bill bill : billListData.Bills){
            if(bill.ValueAfterTax != null && bill.OriginalValue != "0.0"){
                CreateTableRow(bill.OriginalValue, bill.ValueAfterTax, Double.toString(Math.RoundNumber(bill.CurrencyConvertedValue)));
                billTotal = Double.parseDouble(model.GetTotal());
            }
        }
        updateBillTotalText();
        SetGBPText();
    }

    public void RemoveAllRows() {

        itemList = getView().findViewById(R.id.test);
        int count = itemList.getChildCount();
        itemList.removeViews(1,count-1);
    }

    private void updateBillTotalText() {
        TextView billTotalText = getView().findViewById(R.id.BillTotal);
        billTotal = Double.parseDouble(model.GetTotal());
        billTotal = Math.RoundNumber(billTotal);
        billTotalText.setText("$" + Double.toString(billTotal));
    }

    public void SetGBPText(){
        TextView currencyValue = getView().findViewById(R.id.currencyValue);
        currencyValue.setText(billListData.ConvertedTotal);
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void CreateTableRow(String originalValue, String afterTaxValue, String currencyConvertedValue){
        itemList = getView().findViewById(R.id.test);
        TableRow item = new TableRow(getActivity());
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
    }

    private TextView CreateOriginalValueField(TableRow.LayoutParams lp, String inputValue) {
        TextView valueToAdd = new TextView(getActivity());
        valueToAdd.setLayoutParams(lp);
        valueToAdd.setGravity(Gravity.CENTER);
        valueToAdd.setPadding(20,0,0,0);
        valueToAdd.setTextColor(Color.WHITE);
        valueToAdd.setText(inputValue);
        return valueToAdd;
    }

    private TextView CreateAfterTaxValueField(TableRow.LayoutParams lp, String inputValue) {
        TextView valueToAdd = new TextView(getActivity());
        valueToAdd.setLayoutParams(lp);
        valueToAdd.setGravity(Gravity.CENTER);
        valueToAdd.setPadding(20,0,0,0);
        valueToAdd.setTextColor(Color.WHITE);
        valueToAdd.setText(inputValue);
        return valueToAdd;
    }

    private TextView CreateCurrencyValueField(TableRow.LayoutParams lp, String currencyConvertedValue) {
        TextView valueToAdd = new TextView(getActivity());
        valueToAdd.setLayoutParams(lp);
        valueToAdd.setGravity(Gravity.CENTER);
        valueToAdd.setPadding(20,0,0,0);
        valueToAdd.setTextColor(Color.WHITE);
        valueToAdd.setText(currencyConvertedValue);
        return valueToAdd;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private ImageButton CreateDeleteButton() {
        ImageButton deleteButton = new ImageButton(getActivity());
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

        TextView textView = (TextView)parentRow.getChildAt(1);
        String valueToRemoveText = textView.getText().toString();
        double valueToRemove = Double.parseDouble(valueToRemoveText);

        container.removeView(parentRow);
        container.invalidate();

        TextView rowIdTextView = (TextView) parentRow.getChildAt(2);
        removeFromBillList(rowIdTextView);
        updateBillTotalText();

        SetGBPText();
        Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
    }

    private void removeFromBillList(TextView rowIdTextView) {
        double rowId = Double.parseDouble(rowIdTextView.getText().toString());
        int index = 0;

        for(Bill bill : billListData.Bills){
            if(bill.id == rowId){
                index = billListData.Bills.indexOf(bill);
            }
        }
        billListData.Bills.remove(index);
        model.billList.Bills = billListData.Bills;
    }

    private void RemoveFromBillTotal(double value) {
        billTotal -= value;
        billTotal = Math.RoundNumber(billTotal);

        updateBillTotalText();
        billListData.Total = Double.toString(billTotal);
    }

}
