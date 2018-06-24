package com.example.android.inventoryapp_udacity_project.data;

import android.provider.BaseColumns;

public class BooksContract {

    private BooksContract() {
    }

    public static final class BooksEntry implements BaseColumns {

        public final static String TABLE_NAME = "books";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PRODUCT_NAME = "product_name";
        public final static String COLUMN_PRODUCT_PRICE = "price";
        public final static String COLUMN_QUANTITY = "quantity";
        public final static String COLUMN_SUPPLIER_NAME = "supplier_name";
        public final static String COLUMN_SUPPLIER_PHONE = "supplier_phone";

    }
}
