package com.example.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PurchaseGoodsCalculations {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void gstIsCorrectPositive(){
        PurchaseGoods purchaseGoods = new PurchaseGoods();
        double result =  purchaseGoods.CalculateCanadaTax(100);

        assertEquals(5.0,result, 0  );
    }

    @Test
    public void gstIsCorrectNegative(){
        PurchaseGoods purchaseGoods = new PurchaseGoods();
        double result =  purchaseGoods.CalculateCanadaTax(-100);
        assertEquals(0,result, 0  );
    }

    @Test
    public void gstIsCorrectMaxValue(){
        PurchaseGoods purchaseGoods = new PurchaseGoods();
        double result =  purchaseGoods.CalculateCanadaTax(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE * 0.05,result, 0  );
    }

    @Test
    public void gstIsCorrectMinValue(){
        PurchaseGoods purchaseGoods = new PurchaseGoods();
        double result =  purchaseGoods.CalculateCanadaTax(Double.MIN_VALUE);
        assertEquals(0.0,result, 0  );
    }

    @Test
    public void qstIsCorrectPositive(){
        PurchaseGoods purchaseGoods = new PurchaseGoods();
        double result =  purchaseGoods.CalculateProvinceTax(100);

        assertEquals(9.75,result, 0  );
    }

    @Test
    public void qstIsCorrectNegative(){
        PurchaseGoods purchaseGoods = new PurchaseGoods();
        double result =  purchaseGoods.CalculateProvinceTax(-100);
        assertEquals(0,result, 0  );
    }

    @Test
    public void qstIsCorrectMaxValue(){
        PurchaseGoods purchaseGoods = new PurchaseGoods();
        double result =  purchaseGoods.CalculateProvinceTax(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE * 0.0975,result, 0  );
    }

    @Test
    public void qstIsCorrectMinValue(){
        PurchaseGoods purchaseGoods = new PurchaseGoods();
        double result =  purchaseGoods.CalculateProvinceTax(Double.MIN_VALUE);
        assertEquals(0.0,result, 0  );
    }

    @Test
    public void totalIsCorrectPositive(){
        PurchaseGoods purchaseGoods = new PurchaseGoods();
        String result =  purchaseGoods.CalculateTotal(1,1,1);

        assertEquals("3.0",result );
    }

    @Test
    public void totalIsCorrectNegative(){
        PurchaseGoods purchaseGoods = new PurchaseGoods();
        String result =  purchaseGoods.CalculateTotal(-1,-1,-1);
        assertEquals("0.0",result);
    }

    @Test
    public void totalIsCorrectWhenValuesAreGreaterThanMaxValue(){
        PurchaseGoods purchaseGoods = new PurchaseGoods();

        String result =  purchaseGoods.CalculateTotal(Double.MAX_VALUE+1, Double.MAX_VALUE+1, Double.MAX_VALUE+1);

        assertEquals("0.0",result);
    }

}