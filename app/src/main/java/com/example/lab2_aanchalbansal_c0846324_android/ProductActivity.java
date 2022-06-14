package com.example.lab2_aanchalbansal_c0846324_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.SearchView;

import com.example.lab2_aanchalbansal_c0846324_android.Database.DatabaseManager;
import com.example.lab2_aanchalbansal_c0846324_android.Model.ProductModel;
import com.example.lab2_aanchalbansal_c0846324_android.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    // instance of DatabaseOpenHelper class
    DatabaseManager sqLiteDatabase;

    List<ProductModel> productList;
    List<ProductModel> filteredList;

    ListView productsListView;
    SearchView searchView;
    ProductAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productsListView = findViewById(R.id.lv_products);
        productList = new ArrayList<>();
        filteredList = new ArrayList<>();


        findViewById(R.id.floatingActionButton).setOnClickListener(this);
        searchView =  (SearchView) findViewById(R.id.search);


        sqLiteDatabase = new DatabaseManager(this);

        Cursor cursor = sqLiteDatabase.getAllProducts();
        if (!cursor.moveToFirst()) {
            sqLiteDatabase.addProduct("iPhone", "Phones", Double.valueOf(100));
            sqLiteDatabase.addProduct("Samsung", "Tablets", Double.valueOf(200));
            sqLiteDatabase.addProduct("HCL", "Laptops", Double.valueOf(300));
            sqLiteDatabase.addProduct("Lenovo", "Computers", Double.valueOf(100));
            sqLiteDatabase.addProduct("Dell", "Laptops", Double.valueOf(100));
            sqLiteDatabase.addProduct("MacBook", "Mac mini", Double.valueOf(100));
            sqLiteDatabase.addProduct("Realme", "Mobile Phones", Double.valueOf(100));
            sqLiteDatabase.addProduct("Noise", "Earpods", Double.valueOf(100));
            sqLiteDatabase.addProduct("Headphones", "Wireless bluetooth", Double.valueOf(100));
            sqLiteDatabase.addProduct("Airtags", "Apple", Double.valueOf(100));
        }

        loadProducts();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override

            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()) {
                    filteredList = new ArrayList<ProductModel>(productList);
                }
                else {
                    List<ProductModel> temp = new ArrayList<ProductModel>(productList);
                    temp.clear();
                    for(ProductModel model : filteredList){
                        if (model.getName().toLowerCase().contains(s.toLowerCase()) || model.getDescription().toLowerCase().contains(s.toLowerCase())) {
                            temp.add(model);
                        }
                    }
                    filteredList = new ArrayList<ProductModel>(temp);
                }
                adapter = new ProductAdapter(ProductActivity.this,
                        R.layout.list_layout_product,
                        filteredList,
                        sqLiteDatabase);
                productsListView.setAdapter(adapter);
                return false;
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingActionButton:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

    }

    private void loadProducts() {

        Cursor cursor = sqLiteDatabase.getAllProducts();

        if (cursor.moveToFirst()) {
            do {
                // create an product instance
                productList.add(new ProductModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3)
                ));
                filteredList.add(new ProductModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3)
                ));

            } while (cursor.moveToNext());
            cursor.close();
        }
        // create an adapter to display the products
        adapter = new ProductAdapter(this,
                R.layout.list_layout_product,
                filteredList,
                sqLiteDatabase);
        productsListView.setAdapter(adapter);
    }
}