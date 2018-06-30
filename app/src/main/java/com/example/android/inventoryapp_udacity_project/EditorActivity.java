package com.example.android.inventoryapp_udacity_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity {

    public static int REQUEST_CODE_CREATE = 10;
    public static int REQUEST_CODE_EDIT = 11;
    public static int RESPONSE_CODE_TRUE = 20;
    public static int RESPONSE_CODE_DEL = 21;
    public static String EXTRA_REQUEST_CODE = "extra_request_code";
    public static String EXTRA_PRODUCT_ID = "extra_product_id";
    public static String EXTRA_PRODUCT_NAME = "extra_product_name";
    public static String EXTRA_PRODUCT_QUANTITY = "extra_product_quantity";
    public static String EXTRA_PRODUCT_PRICE = "price";
    public static String EXTRA_SUPPLIER_NAME = "supplier_name";
    public static String EXTRA_SUPPLIER_PHONE = "supplier_phone";

    private EditText mProductNameEnter;
    private EditText mProductQuantityEnter;
    private EditText mProductPriceEnter;
    private EditText mSupplierNameEnter;
    private EditText mSupplierPhoneEnter;
    private int mRequestCode;
    private Button mDecreaseQuantity;
    private Button mIncreaseQuantity;
    private Button mSave;
    private Button mDelete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mProductNameEnter = findViewById(R.id.edit_product_name);
        mProductQuantityEnter = findViewById(R.id.edit_quantity);
        mProductPriceEnter = findViewById(R.id.edit_price);
        mSupplierNameEnter = findViewById(R.id.edit_supplier_name);
        mSupplierPhoneEnter = findViewById(R.id.edit_supplier_phone);
        mDecreaseQuantity = findViewById(R.id.btn_decrease);
        mIncreaseQuantity = findViewById(R.id.btn_increase);
        mSave = findViewById(R.id.btn_save);
        mDelete = findViewById(R.id.btn_delete);

        mRequestCode = getIntent().getIntExtra(EXTRA_REQUEST_CODE, -1);
        if (mRequestCode == REQUEST_CODE_EDIT) {
            setUpViews();
        }

        mDecreaseQuantity.setOnClickListener(new buttonClick());
        mIncreaseQuantity.setOnClickListener(new buttonClick());
        mSave.setOnClickListener(new buttonClick());
        mDelete.setOnClickListener(new buttonClick());
    }

    class buttonClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_decrease:
                    Toast.makeText(EditorActivity.this, "btn_decrease", Toast.LENGTH_LONG).show();
                    break;
                case R.id.btn_increase:
                    Toast.makeText(EditorActivity.this, "btn_increase", Toast.LENGTH_LONG).show();
                    break;
                case R.id.btn_delete:
                    onDeleteClick();
                    break;
                case R.id.btn_save:
                    onSaveClick();
                    break;
            }
        }
    }

    private void setUpViews() {
        Intent startIntent = getIntent();
        String name = startIntent.getStringExtra(EXTRA_PRODUCT_NAME);
        int quantity = startIntent.getIntExtra(EXTRA_PRODUCT_QUANTITY, -1);
        int price = startIntent.getIntExtra(EXTRA_PRODUCT_PRICE, -1);
        String supplierName = startIntent.getStringExtra(EXTRA_SUPPLIER_NAME);
        String supplierPhone = startIntent.getStringExtra(EXTRA_SUPPLIER_PHONE);
        mProductNameEnter.setText(name);
        mProductQuantityEnter.setText(String.valueOf(quantity));
        mProductPriceEnter.setText(String.valueOf(price));
        mSupplierNameEnter.setText(supplierName);
        mSupplierPhoneEnter.setText(supplierPhone);
    }

    public void onSaveClick() {
        Intent resultIntent = new Intent();
        String name = mProductNameEnter.getText().toString();
        String supplierName = mSupplierNameEnter.getText().toString();
        String supplierPhone = mSupplierPhoneEnter.getText().toString();
        int quantity;
        int price;

        if (TextUtils.isEmpty(name)) {
            showErrorDialog(getString(R.string.name_error_msg));
            return;
        }

        if (TextUtils.isEmpty(supplierName)) {
            showErrorDialog(getString(R.string.supplier_name_error_msg));
            return;
        }

        if (TextUtils.isEmpty(supplierPhone)) {
            showErrorDialog(getString(R.string.supplier_phone_error_msg));
            return;
        }

        try {
            quantity = Integer.parseInt(mProductQuantityEnter.getText().toString());
        } catch (NumberFormatException e) {
            showErrorDialog(getString(R.string.quantity_error_msg));
            return;
        }

        try {
            price = Integer.parseInt(mProductPriceEnter.getText().toString());
        } catch (NumberFormatException e) {
            showErrorDialog(getString(R.string.price_error_msg));
            return;
        }

        resultIntent.putExtra(EXTRA_PRODUCT_NAME, name);
        resultIntent.putExtra(EXTRA_PRODUCT_QUANTITY, quantity);
        resultIntent.putExtra(EXTRA_PRODUCT_PRICE, price);
        resultIntent.putExtra(EXTRA_SUPPLIER_NAME, supplierName);
        resultIntent.putExtra(EXTRA_SUPPLIER_PHONE, supplierPhone);

        if (mRequestCode == REQUEST_CODE_EDIT) {
            int id = getIntent().getIntExtra(EXTRA_PRODUCT_ID, -1);
            resultIntent.putExtra(EXTRA_PRODUCT_ID, id);
        }

        setResult(RESPONSE_CODE_TRUE, resultIntent);
        finish();
    }

    public void onDeleteClick() {
        Intent resultIntent = new Intent();
        if (mRequestCode == REQUEST_CODE_EDIT) {
            int id = getIntent().getIntExtra(EXTRA_PRODUCT_ID, -1);
            resultIntent.putExtra(EXTRA_PRODUCT_ID, id);
        }
        setResult(RESPONSE_CODE_DEL, resultIntent);
        finish();
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setTitle(R.string.error_dialog_title)
                .setCancelable(false)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }
}