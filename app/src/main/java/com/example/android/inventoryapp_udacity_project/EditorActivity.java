package com.example.android.inventoryapp_udacity_project;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EditorActivity extends AppCompatActivity {

    public static int REQUEST_CODE_CREATE = 10;
    public static int REQUEST_CODE_EDIT = 11;
    public static int RESPONSE_CODE_TRUE = 20;
    public static int RESPONSE_CODE_DELETE = 21;
    public static String EXTRA_REQUEST_CODE = "extra_request_code";
    public static String EXTRA_PRODUCT_ID = "extra_product_id";
    public static String EXTRA_PRODUCT_NAME = "extra_product_name";
    public static String EXTRA_PRODUCT_QUANTITY = "extra_product_quantity";
    public static String EXTRA_PRODUCT_PRICE = "price";
    public static String EXTRA_SUPPLIER_NAME = "supplier_name";
    public static String EXTRA_SUPPLIER_PHONE = "supplier_phone";

    private static int CONFIRMATION_ALERT_DELETE = 0;

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
    private int quantity;
    private ImageView mSupplierCall;

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
        mSupplierCall = findViewById(R.id.iv_supplier_call);

        mRequestCode = getIntent().getIntExtra(EXTRA_REQUEST_CODE, -1);
        if (mRequestCode == REQUEST_CODE_EDIT) {
            setUpViews();
        }

        mDecreaseQuantity.setOnClickListener(onClickListener);
        mIncreaseQuantity.setOnClickListener(onClickListener);
        mSave.setOnClickListener(onClickListener);
        mDelete.setOnClickListener(onClickListener);
        mSupplierCall.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_decrease:
                    quantity = Integer.parseInt(mProductQuantityEnter.getText().toString());
                    quantity --;
                    if (quantity >= 0) mProductQuantityEnter.setText(String.valueOf(quantity));
                    else showErrorDialog(getString(R.string.quantity_error_msg));
                    break;
                case R.id.btn_increase:
                    quantity = Integer.parseInt(mProductQuantityEnter.getText().toString());
                    quantity ++;
                    mProductQuantityEnter.setText(String.valueOf(quantity));
                    break;
                case R.id.btn_delete:
                    onDeleteClick();
                    break;
                case R.id.btn_save:
                    onSaveClick();
                    break;
                case R.id.iv_supplier_call:
                    Log.d("EditorActivity", "CLICK");
                    onCallClick();
                    break;
            }
        }
    };

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
        showConfirmationDialog(getString(R.string.delete_alert), CONFIRMATION_ALERT_DELETE);
    }

    private void onCallClick(){
        String phoneNumber = String.format("tel: %s", mSupplierPhoneEnter.getText().toString());
        if (validCellPhone(phoneNumber)) {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse(phoneNumber));
            if (dialIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(dialIntent);
            } else {
                showErrorDialog(getString(R.string.no_dialer_error_msg));
            }
        }else {
            showErrorDialog(getString(R.string.wrong_number_error_msg));
        }
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

    private void showConfirmationDialog(String message, final int type) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setTitle(R.string.confirm_dialog_title)
                .setNegativeButton(R.string.cancel_dialog_title, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == CONFIRMATION_ALERT_DELETE) {
                            Intent resultIntent = new Intent();
                            if (mRequestCode == REQUEST_CODE_EDIT) {
                                int id = getIntent().getIntExtra(EXTRA_PRODUCT_ID, -1);
                                resultIntent.putExtra(EXTRA_PRODUCT_ID, id);
                            }
                            setResult(RESPONSE_CODE_DELETE, resultIntent);
                            finish();
                        } else {
                            dialog.cancel();
                        }
                    }
                })
                .create()
                .show();
    }

    private boolean validCellPhone(String number) {
        String Regex = "[^\\d]";
        String PhoneDigits = number.replaceAll(Regex, "");
        if (PhoneDigits == null || PhoneDigits.length() < 6 || PhoneDigits.length() > 13) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(PhoneDigits).matches();
        }
    }
}