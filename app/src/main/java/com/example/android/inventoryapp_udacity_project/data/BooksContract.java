package com.example.android.inventoryapp_udacity_project.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class BooksContract {

        private BooksContract() {}

        public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp_udacity_project";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_INVENTORY = "inventoryapp_udacity_project";

        public static final class BooksEntry implements BaseColumns {

            public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);
            public static final String CONTENT_LIST_TYPE =
                    ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;
            public static final String CONTENT_ITEM_TYPE =
                    ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

            public final static String TABLE_NAME = "books";
            public final static String _ID = BaseColumns._ID;
            public final static String COLUMN_PRODUCT_NAME ="product_name";
            public final static String COLUMN_PRODUCT_PRICE = "price";
            public final static String COLUMN_QUANTITY = "quantity";
            public final static String COLUMN_SUPPLIER_NAME = "supplier_name";
            public final static String COLUMN_SUPPLIER_PHONE = "supplier_phone";

        }
}
