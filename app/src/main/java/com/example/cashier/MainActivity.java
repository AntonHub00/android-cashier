package com.example.cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

// My imports
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // Text edits
    private EditText price_box;
    private EditText amount_box;

    // Discount Spinner
    private Spinner discount_spinner;

    // Result text views (bills)
    private TextView thousand_text_view;
    private TextView five_hundred_text_view;
    private TextView two_hundred_text_view;
    private TextView one_hundred_text_view;
    private TextView fifty_text_view;
    private TextView twenty_text_view;

    // Result text views (coins)
    private TextView ten_text_view;
    private TextView five_text_view;
    private TextView two_text_view;
    private TextView one_text_view;
    private TextView cents_text_view;

    // Results
    private TextView change_text_view;
    private TextView discount_result_text_view;

    // Bills and coins checkboxes
    private CheckBox thousand_check;
    private CheckBox five_hundred_check;
    private CheckBox two_hundred_check;
    private CheckBox one_hundred_check;
    private CheckBox fifty_check;
    private CheckBox twenty_check;
    private CheckBox ten_check;
    private CheckBox five_check;
    private CheckBox two_check;
    private CheckBox one_check;
    private CheckBox cents_check;

    // Calculate button
    private Button calculate;

    private HashMap<String, Integer> bills_coins = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Text edits
        price_box = findViewById(R.id.price);
        amount_box = findViewById(R.id.amount);
        discount_spinner = findViewById(R.id.discounts);

        // Result text views (bills)
        thousand_text_view = findViewById(R.id.thousand);
        five_hundred_text_view = findViewById(R.id.five_hundred);
        two_hundred_text_view = findViewById(R.id.two_hundred);
        one_hundred_text_view = findViewById(R.id.one_hundred);
        fifty_text_view = findViewById(R.id.fifty);
        twenty_text_view = findViewById(R.id.twenty);

        // Result text views (coins)
        ten_text_view = findViewById(R.id.ten);
        five_text_view = findViewById(R.id.five);
        two_text_view = findViewById(R.id.two);
        one_text_view = findViewById(R.id.one);
        cents_text_view = findViewById(R.id.cents);

        // Results
        change_text_view = findViewById(R.id.change_result);
        discount_result_text_view = findViewById(R.id.discount_result);

        // Bills and coins checkboxes
        thousand_check = findViewById(R.id.thousand_check);
        five_hundred_check = findViewById(R.id.five_hundred_check);
        two_hundred_check = findViewById(R.id.two_hundred_check);
        one_hundred_check = findViewById(R.id.one_hundred_check);
        fifty_check = findViewById(R.id.fifty_check);
        twenty_check = findViewById(R.id.twenty_check);
        ten_check = findViewById(R.id.ten_check);
        five_check = findViewById(R.id.five_check);
        two_check = findViewById(R.id.two_check);
        one_check = findViewById(R.id.one_check);
        cents_check = findViewById(R.id.cents_check);
    }

    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean formValid() {

        float price;
        float amount;
        int discount;

        // Check if price is set
        if (TextUtils.isEmpty(price_box.getText())) {
            price_box.setError("This filed is required");
            return false;
        } else {
            price = Float.parseFloat(price_box.getText().toString());
        }

        // Check if amount is set
        if (TextUtils.isEmpty(amount_box.getText())) {
            amount_box.setError("This filed is required");
            return false;
        } else {
            amount = Float.parseFloat(amount_box.getText().toString());
        }

        // Amount-price validation
        if (amount < (price - (price * ((Integer.parseInt(discount_spinner.getSelectedItem().toString())) / 100)))) {
            amount_box.setError("Amount can not be less than price");
            return false;
        } else {
            return true;
        }
    }

    private float calculateBillsAndCoins(float change, float billCoinValue, String hashMapKey) {
        int billCoinAmount = (int) (change / billCoinValue);
        if (billCoinAmount >= 0) {
            change -= billCoinAmount * billCoinValue;
            bills_coins.put(hashMapKey, billCoinAmount);
        }

        return change;
    }

    private void resetBillsAndCoins() {
        bills_coins.put("thousand", 0);
        bills_coins.put("fiveH", 0);
        bills_coins.put("twoH", 0);
        bills_coins.put("oneH", 0);
        bills_coins.put("fifty", 0);
        bills_coins.put("twenty", 0);
        bills_coins.put("ten", 0);
        bills_coins.put("five", 0);
        bills_coins.put("two", 0);
        bills_coins.put("one", 0);
        bills_coins.put("cents", 0);
    }

    public void calculateChange(View view) {

        closeKeyboard();

        float price;
        float amount;
        int discount;
        float discount_result;
        float change;

        if (!formValid()) return;

        resetBillsAndCoins();

        price = Float.parseFloat(price_box.getText().toString());
        amount = Float.parseFloat(amount_box.getText().toString());
        discount = Integer.parseInt(discount_spinner.getSelectedItem().toString());
        discount_result = (price * ((float) discount / 100));
        change = amount - (price - discount_result);

        change_text_view.setText(String.format("%.2f", change));
        discount_result_text_view.setText(String.format("%.2f", discount_result));

        if (thousand_check.isChecked()) change = calculateBillsAndCoins(change, 1000, "thousand");
        if (five_hundred_check.isChecked()) change = calculateBillsAndCoins(change, 500, "fiveH");
        if (two_hundred_check.isChecked()) change = calculateBillsAndCoins(change, 200, "twoH");
        if (one_hundred_check.isChecked()) change = calculateBillsAndCoins(change, 100, "oneH");
        if (fifty_check.isChecked()) change = calculateBillsAndCoins(change, 50, "fifty");
        if (twenty_check.isChecked()) change = calculateBillsAndCoins(change, 20, "twenty");
        if (ten_check.isChecked()) change = calculateBillsAndCoins(change, 10, "ten");
        if (five_check.isChecked()) change = calculateBillsAndCoins(change, 5, "five");
        if (two_check.isChecked()) change = calculateBillsAndCoins(change, 2, "two");
        if (one_check.isChecked()) change = calculateBillsAndCoins(change, 1, "one");
        if (cents_check.isChecked()) change = calculateBillsAndCoins(change, (float) 0.50, "cents");


        thousand_text_view.setText(String.valueOf(String.valueOf(bills_coins.get("thousand"))));
        five_hundred_text_view.setText(String.valueOf(String.valueOf(bills_coins.get("fiveH"))));
        two_hundred_text_view.setText(String.valueOf(String.valueOf(bills_coins.get("twoH"))));
        one_hundred_text_view.setText(String.valueOf(String.valueOf(bills_coins.get("oneH"))));
        fifty_text_view.setText(String.valueOf(String.valueOf(bills_coins.get("fifty"))));
        twenty_text_view.setText(String.valueOf(String.valueOf(bills_coins.get("twenty"))));
        ten_text_view.setText(String.valueOf(String.valueOf(bills_coins.get("ten"))));
        five_text_view.setText(String.valueOf(String.valueOf(bills_coins.get("five"))));
        two_text_view.setText(String.valueOf(String.valueOf(bills_coins.get("two"))));
        one_text_view.setText(String.valueOf(String.valueOf(bills_coins.get("one"))));
        cents_text_view.setText(String.valueOf(String.valueOf(bills_coins.get("cents"))));
    }

}