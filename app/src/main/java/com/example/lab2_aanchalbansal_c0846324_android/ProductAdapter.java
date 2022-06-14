package com.example.lab2_aanchalbansal_c0846324_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.database.Cursor;

import com.example.lab2_aanchalbansal_c0846324_android.Database.DatabaseManager;
import com.example.lab2_aanchalbansal_c0846324_android.Model.ProductModel;


import java.util.Arrays;
import java.util.List;

public class ProductAdapter extends ArrayAdapter {

    private static final String TAG = "ProductAdapter";

    Context context;
    int layoutRes;
    List<ProductModel> productList;
    DatabaseManager sqLiteDatabase;

    public ProductAdapter(@NonNull Context context, int resource, List<ProductModel> productList, DatabaseManager sqLiteDatabase) {
        super(context, resource, productList);
        this.productList = productList;
        this.sqLiteDatabase = sqLiteDatabase;
        this.context = context;
        this.layoutRes = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = convertView;
        if (v == null) v = inflater.inflate(layoutRes, null);
        TextView nameTV = v.findViewById(R.id.tv_name);
        TextView priceTV = v.findViewById(R.id.tv_price);
        TextView descriptionTV = v.findViewById(R.id.tv_description);

        final ProductModel product = productList.get(position);
        nameTV.setText(product.getName());
        priceTV.setText(String.valueOf(product.getPrice()));
        descriptionTV.setText(product.getDescription());

        v.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct(product);
            }

            private void updateProduct(final ProductModel productModel) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View view = layoutInflater.inflate(R.layout.dialog_update_product, null);
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                final EditText etName = view.findViewById(R.id.et_name);
                final EditText etPrice = view.findViewById(R.id.et_price);
                final EditText etDescription = view.findViewById(R.id.et_description);

                etName.setText(product.getName());
                etPrice.setText(String.valueOf(product.getPrice()));
                etDescription.setText(product.getDescription());

                view.findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = etName.getText().toString().trim();
                        String price = etPrice.getText().toString().trim();
                        String description = etDescription.getText().toString().trim();

                        if (name.isEmpty()) {
                            etName.setError("name field cannot be empty");
                            etName.requestFocus();
                            return;
                        }

                        if (price.isEmpty()) {
                            etPrice.setError("price cannot be empty");
                            etPrice.requestFocus();
                            return;
                        }

                        if (description.isEmpty()) {
                            etDescription.setError("description cannot be empty");
                            etDescription.requestFocus();
                            return;
                        }

                        if (sqLiteDatabase.updateProduct(product.getProductId(), name, description, Double.parseDouble(price)))
                            loadProducts();

                        alertDialog.dismiss();
                    }
                });
            }
        });

        v.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(product);
            }

            private void deleteProduct(final ProductModel product) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (sqLiteDatabase.deleteProduct(product.getProductId()))
                            loadProducts();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "The product (" + product.getName() + ") is not deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        Log.d(TAG, "getView: " + getCount());
        return v;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    private void loadProducts() {

        Cursor cursor = sqLiteDatabase.getAllProducts();
        productList.clear();
        if (cursor.moveToFirst()) {
            do {
                // create an product instance
                productList.add(new ProductModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3)
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }

        notifyDataSetChanged();
    }
}
