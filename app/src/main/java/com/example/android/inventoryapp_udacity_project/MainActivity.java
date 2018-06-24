package com.example.android.inventoryapp_udacity_project;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.inventoryapp_udacity_project.data.BooksContract;
import com.example.android.inventoryapp_udacity_project.data.InventoryDbHelper;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private InventoryDbHelper mInventoryDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInventoryDbHelper = new InventoryDbHelper(this);

        insertData();
        queryData();
    }

    private void insertData() {

        SQLiteDatabase database = mInventoryDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(BooksContract.BooksEntry.COLUMN_PRODUCT_NAME, getString(R.string.book_name_1));
        contentValues.put(BooksContract.BooksEntry.COLUMN_PRODUCT_PRICE, getString(R.string.book_price_1));
        contentValues.put(BooksContract.BooksEntry.COLUMN_QUANTITY, Integer.parseInt(getString(R.string.book_quantity_1)));
        contentValues.put(BooksContract.BooksEntry.COLUMN_SUPPLIER_NAME, getString(R.string.supplier_name_1));
        contentValues.put(BooksContract.BooksEntry.COLUMN_SUPPLIER_PHONE, getString(R.string.supplier_phone_1));

        database.insert(BooksContract.BooksEntry.TABLE_NAME, null, contentValues);
    }

    private void queryData() {
        SQLiteDatabase database = mInventoryDbHelper.getReadableDatabase();
        String[] projection = {
                BooksContract.BooksEntry._ID,
                BooksContract.BooksEntry.COLUMN_PRODUCT_NAME,
                BooksContract.BooksEntry.COLUMN_PRODUCT_PRICE,
                BooksContract.BooksEntry.COLUMN_QUANTITY,
                BooksContract.BooksEntry.COLUMN_SUPPLIER_NAME,
                BooksContract.BooksEntry.COLUMN_SUPPLIER_PHONE
        };

        Cursor cursor = database.query(
                BooksContract.BooksEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        StringBuilder stringBuilder = new StringBuilder();
        try {
            // Append title for a message
            stringBuilder.append(getString(R.string.print_message_about_table) + " " + +cursor.getCount() + " " + getString(R.string.items) + "\n\n");
            // Append column names
            stringBuilder.append(BooksContract.BooksEntry._ID + " | " +
                    BooksContract.BooksEntry.COLUMN_PRODUCT_NAME + " | " +
                    BooksContract.BooksEntry.COLUMN_PRODUCT_PRICE + " | " +
                    BooksContract.BooksEntry.COLUMN_QUANTITY + " | " +
                    BooksContract.BooksEntry.COLUMN_SUPPLIER_NAME + " | " +
                    BooksContract.BooksEntry.COLUMN_SUPPLIER_PHONE + "\n");

            int idColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_SUPPLIER_PHONE);

            while (cursor.moveToNext()) {

                int currentId = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentPrice = cursor.getString(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);

                stringBuilder.append(
                        currentId + " | " +
                                currentName + " | " +
                                currentPrice + " | " +
                                currentQuantity + " | " +
                                currentSupplierName + " | " +
                                currentSupplierPhone + "\n");
            }
        } finally {
            Log.d(LOG_TAG, stringBuilder.toString());
            stringBuilder.delete(0, stringBuilder.length());
            cursor.close();
        }
    }
}
