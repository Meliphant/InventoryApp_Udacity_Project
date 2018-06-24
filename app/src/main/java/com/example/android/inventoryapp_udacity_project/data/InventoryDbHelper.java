package com.example.android.inventoryapp_udacity_project.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InventoryDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_BOOKS_TABLE =  "CREATE TABLE " + BooksContract.BooksEntry.TABLE_NAME + " ("
                + BooksContract.BooksEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BooksContract.BooksEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + BooksContract.BooksEntry.COLUMN_PRODUCT_PRICE + " TEXT NOT NULL DEFAULT 0, "
                + BooksContract.BooksEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, "
                + BooksContract.BooksEntry.COLUMN_SUPPLIER_NAME + " INTEGER, "
                + BooksContract.BooksEntry.COLUMN_SUPPLIER_PHONE + " INTEGER);";

        db.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
