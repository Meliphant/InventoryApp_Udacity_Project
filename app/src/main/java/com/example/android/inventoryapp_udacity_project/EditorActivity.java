package com.example.android.inventoryapp_udacity_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class EditorActivity extends AppCompatActivity {

    public static int REQUEST_CODE_CREATE = 10;
    public static int REQUEST_CODE_EDIT = 11;
    public static int RESPONSE_CODE_TRUE = 20;
    public static int RESPONSE_CODE_DEL = 21;
    public static String EXTRA_REQUEST_CODE = "extra_request_code";
    public static String EXTRA_PRODUCT_ID = "extra_product_id";
    public static String EXTRA_PRODUCT_NAME = "extra_product_name";
    public static String EXTRA_PRODUCT_QUANTITY = "extra_product_quantity";

    private EditText mProductNameEnter;
    private EditText mProductQuantityEnter;
    private int mRequestCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mProductNameEnter = findViewById(R.id.edit_product_name);
        mProductQuantityEnter = findViewById(R.id.edit_quantity);

        mRequestCode = getIntent().getIntExtra(EXTRA_REQUEST_CODE, -1);
        if (mRequestCode == REQUEST_CODE_EDIT) {
            setUpViews();
        }
    }

    public void onSaveClick(View view) {
        Intent resultIntent = new Intent();
        String name = mProductNameEnter.getText().toString();
        int quantity;

        if (TextUtils.isEmpty(name)) {
            showErrorDialog(getString(R.string.name_error_msg));
            return;
        }

        try {
            quantity = Integer.parseInt(mProductQuantityEnter.getText().toString());
        } catch (NumberFormatException e) {
            showErrorDialog(getString(R.string.quantity_error_msg));
            return;
        }

        resultIntent.putExtra(EXTRA_PRODUCT_NAME, name);
        resultIntent.putExtra(EXTRA_PRODUCT_QUANTITY, quantity);

        if (mRequestCode == REQUEST_CODE_EDIT) {
            int id = getIntent().getIntExtra(EXTRA_PRODUCT_ID, -1);
            resultIntent.putExtra(EXTRA_PRODUCT_ID, id);
        }

        setResult(RESPONSE_CODE_TRUE, resultIntent);
        finish();
    }

    public void onDeleteClick(View view) {
        Intent resultIntent = new Intent();
        if (mRequestCode == REQUEST_CODE_EDIT) {
            int id = getIntent().getIntExtra(EXTRA_PRODUCT_ID, -1);
            resultIntent.putExtra(EXTRA_PRODUCT_ID, id);
        }
        setResult(RESPONSE_CODE_DEL, resultIntent);
        finish();
    }

    private void setUpViews() {
        Intent startIntent = getIntent();
        String name = startIntent.getStringExtra(EXTRA_PRODUCT_NAME);
        int quantity = startIntent.getIntExtra(EXTRA_PRODUCT_QUANTITY, -1);
        mProductNameEnter.setText(name);
        mProductQuantityEnter.setText(String.valueOf(quantity));
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