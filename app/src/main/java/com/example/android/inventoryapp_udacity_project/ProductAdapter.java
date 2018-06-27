package com.example.android.inventoryapp_udacity_project;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.inventoryapp_udacity_project.data.Book;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context mContext;
    private OnProductItemClickListener mClickListener;
    private Cursor mCursor;

    public ProductAdapter(Context context, OnProductItemClickListener clickListener) {
        mContext = context;
        mClickListener = clickListener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    private Book getProduct(int position) {
        mCursor.moveToPosition(position);
        int id = mCursor.getInt(MainActivity.INDEX_PRODUCT_ID);
        String name = mCursor.getString(MainActivity.INDEX_PRODUCT_NAME);
        int quantity = mCursor.getInt(MainActivity.INDEX_PRODUCT_QUANTITY);
        return new Book(id, name, quantity);
    }

    public interface OnProductItemClickListener {
        void onProductSaleClick(Book product);
        void onProductEditClick(Book product);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mProductNumberDisplay;
        private TextView mProductNameDisplay;
        private TextView mProductQuantityDisplay;
        private ImageButton mProductMinusButton;
        private ImageButton mProductEditButton;

        ProductViewHolder(View itemView) {
            super(itemView);
            mProductNumberDisplay = itemView.findViewById(R.id.product_number);
            mProductNameDisplay = itemView.findViewById(R.id.product_name);
            mProductQuantityDisplay = itemView.findViewById(R.id.product_quantity);
            mProductMinusButton = itemView.findViewById(R.id.btn_quantity_min);
            mProductEditButton = itemView.findViewById(R.id.btn_edit);
            mProductMinusButton.setOnClickListener(this);
            mProductEditButton.setOnClickListener(this);
        }

        void bind(int position) {
            Book product = getProduct(position);
            mProductNumberDisplay.setText(String.valueOf(product.getId()));
            mProductNameDisplay.setText(product.getName());
            mProductQuantityDisplay.setText(String.valueOf(product.getQuantity()));
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Book product = getProduct(position);
            if (v.getId() == mProductMinusButton.getId()) {
                mClickListener.onProductSaleClick(product);
            } else if (v.getId() == mProductEditButton.getId()) {
                mClickListener.onProductEditClick(product);
            }
        }
    }
}