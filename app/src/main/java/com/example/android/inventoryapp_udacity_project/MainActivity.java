package com.example.android.inventoryapp_udacity_project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.inventoryapp_udacity_project.data.Book;
import com.example.android.inventoryapp_udacity_project.data.ProductContract;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, ProductAdapter.OnProductItemClickListener {

    public static final String[] MAIN_PRODUCT_PROJECTION = {
            ProductContract.ProductEntry._ID,
            ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
            ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
            ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
            ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME,
            ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE
    };
    public static final int INDEX_PRODUCT_ID = 0;
    public static final int INDEX_PRODUCT_NAME = 1;
    public static final int INDEX_PRODUCT_QUANTITY = 2;
    public static final int INDEX_PRODUCT_PRICE = 3;
    public static final int INDEX_SUPPLIER_NAME = 4;
    public static final int INDEX_SUPPLIER_PHONE = 5;
    private static final int ID_PRODUCT_LOADER = 42;
    private final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mProductsContainer;
    private ProductAdapter mProductAdapter;
    private int mPosition = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProductsContainer = findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mProductsContainer.setLayoutManager(layoutManager);

        mProductAdapter = new ProductAdapter(this, this);
        mProductsContainer.setAdapter(mProductAdapter);

        getSupportLoaderManager().initLoader(ID_PRODUCT_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_product) {
            Intent startEditor = new Intent(MainActivity.this, EditorActivity.class);
            startActivityForResult(startEditor, EditorActivity.REQUEST_CODE_CREATE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProductSaleClick(Book product) {
        ContentValues values = new ContentValues();
        int newQuantity = product.getQuantity() - 1;

        if (newQuantity < 0) {
            return;
        }

        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, newQuantity);

        int rowsUpdated = getContentResolver().update(ProductContract.ProductEntry.CONTENT_URI, values,
                "_id = ?", new String[]{String.valueOf(product.getId())});

        if (rowsUpdated == 1) {
            getSupportLoaderManager().restartLoader(ID_PRODUCT_LOADER, null, this);
        }
    }

    @Override
    public void onProductEditClick(Book product) {
        Intent startEditProduct = new Intent(MainActivity.this, EditorActivity.class);
        startEditProduct.putExtra(EditorActivity.EXTRA_PRODUCT_ID, product.getId());
        startEditProduct.putExtra(EditorActivity.EXTRA_PRODUCT_NAME, product.getName());
        startEditProduct.putExtra(EditorActivity.EXTRA_PRODUCT_QUANTITY, product.getQuantity());
        startEditProduct.putExtra(EditorActivity.EXTRA_PRODUCT_PRICE, product.getPrice());
        startEditProduct.putExtra(EditorActivity.EXTRA_SUPPLIER_NAME, product.getSupplierName());
        startEditProduct.putExtra(EditorActivity.EXTRA_SUPPLIER_PHONE, product.getSupplierPhone());

        startActivityForResult(startEditProduct, EditorActivity.REQUEST_CODE_EDIT);
    }

    @Override
    public void onProductListClick(Book product) {
        Intent startEditProduct = new Intent(MainActivity.this, EditorActivity.class);
        startEditProduct.putExtra(EditorActivity.EXTRA_PRODUCT_ID, product.getId());
        startEditProduct.putExtra(EditorActivity.EXTRA_PRODUCT_NAME, product.getName());
        startEditProduct.putExtra(EditorActivity.EXTRA_PRODUCT_QUANTITY, product.getQuantity());
        startEditProduct.putExtra(EditorActivity.EXTRA_PRODUCT_PRICE, product.getPrice());
        startEditProduct.putExtra(EditorActivity.EXTRA_SUPPLIER_NAME, product.getSupplierName());
        startEditProduct.putExtra(EditorActivity.EXTRA_SUPPLIER_PHONE, product.getSupplierPhone());

        startActivityForResult(startEditProduct, EditorActivity.REQUEST_CODE_EDIT);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra(EditorActivity.EXTRA_REQUEST_CODE, requestCode);
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!isCorrectResult(requestCode, resultCode)) {
            return;
        }

        int productId = data.getIntExtra(EditorActivity.EXTRA_PRODUCT_ID, -1);

        if (resultCode == EditorActivity.RESPONSE_CODE_DEL) {
            deleteProduct(productId);
        }

        String productName = data.getStringExtra(EditorActivity.EXTRA_PRODUCT_NAME);
        int productQuantity = data.getIntExtra(EditorActivity.EXTRA_PRODUCT_QUANTITY, -1);
        int productPrice = data.getIntExtra(EditorActivity.EXTRA_PRODUCT_PRICE,-1);
        String supplierName = data.getStringExtra(EditorActivity.EXTRA_SUPPLIER_NAME);
        String supplierPhone = data.getStringExtra(EditorActivity.EXTRA_SUPPLIER_PHONE);

        if (!isValidProduct(productName, productQuantity, productPrice, supplierName, supplierPhone)) {
            return;
        }

        if (requestCode == EditorActivity.REQUEST_CODE_CREATE) {
            insertProduct(productName, productQuantity, productPrice, supplierName, supplierPhone);
        } else {
            updateProduct(productId, productName, productQuantity, productPrice, supplierName, supplierPhone);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        if (loaderId != ID_PRODUCT_LOADER) {
            throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }

        String sortOrder = ProductContract.ProductEntry._ID + " ASC";

        return new CursorLoader(this,
                ProductContract.ProductEntry.CONTENT_URI,
                MAIN_PRODUCT_PROJECTION,
                null, null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mProductAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProductAdapter.swapCursor(null);
    }

    private boolean isValidProduct(String name, int quantity, int price,
                                   String supplierName, String supplierPhone) {
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(supplierName)
                && !TextUtils.isEmpty(supplierPhone) && price != -1 && quantity != -1;
    }

    private boolean isCorrectResult(int requestCode, int responseCode) {
        return (requestCode == EditorActivity.REQUEST_CODE_CREATE
                || requestCode == EditorActivity.REQUEST_CODE_EDIT)
                && (responseCode == EditorActivity.RESPONSE_CODE_TRUE
                || responseCode == EditorActivity.RESPONSE_CODE_DEL);
    }

    private void insertProduct(String name, int quantity, int price,
                               String supplierName, String supplierPhone) {
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, name);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, price);
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE, supplierPhone);

        Uri newUri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);

        if (newUri != null) {
            getSupportLoaderManager().restartLoader(ID_PRODUCT_LOADER, null, this);
        }
    }

    private void updateProduct(int id, String name, int quantity, int price,
                               String supplierName, String supplierPhone) {
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, name);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, price);
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE, supplierPhone);

        int rowsUpdated = getContentResolver().update(ProductContract.ProductEntry.CONTENT_URI, values,
                "_id = ?", new String[]{String.valueOf(id)});

        if (rowsUpdated == 1) {
            getSupportLoaderManager().restartLoader(ID_PRODUCT_LOADER, null, this);
        }
    }

    private void deleteProduct(int id) {
        int rowsUpdated = getContentResolver().delete(ProductContract.ProductEntry.CONTENT_URI,
                "_id = ?", new String[]{String.valueOf(id)});

        if (rowsUpdated == 1) {
            getSupportLoaderManager().restartLoader(ID_PRODUCT_LOADER, null, this);
        }
    }
}