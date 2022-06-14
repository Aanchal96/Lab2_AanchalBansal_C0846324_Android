package com.example.lab2_aanchalbansal_c0846324_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.database.sqlite.*;

import com.example.lab2_aanchalbansal_c0846324_android.Database.DatabaseManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseManager sqLiteDatabase;

    EditText etName, etDescription, etPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.et_name);
        etDescription = findViewById(R.id.et_description);
        etPrice = findViewById(R.id.et_price);

        findViewById(R.id.btn_add_product).setOnClickListener(this);
        findViewById(R.id.tv_view_products).setOnClickListener(this);

        // initializing the instance of DataBase Manager
        sqLiteDatabase = new DatabaseManager(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_product:
                addProduct();
                break;
            case R.id.tv_view_products:
                startActivity(new Intent(this, ProductActivity.class));
                break;
        }

    }

    private void addProduct() {
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String price = etPrice.getText().toString();

        if (name.isEmpty()) {
            etName.setError("Product name field cannot be empty");
            etName.requestFocus();
            return;
        }

        if (price.isEmpty()) {
            etPrice.setError("price cannot be empty");
            etPrice.requestFocus();
            return;
        }

        // insert product into database table with the help of database openHelper class
        if (sqLiteDatabase.addProduct(name, description, Double.valueOf(price)))
            Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show();

        else
            Toast.makeText(this, "Product NOT Added", Toast.LENGTH_SHORT).show();

        clearFields();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        clearFields();
    }

    private void clearFields() {
        etName.setText("");
        etDescription.setText("");
        etPrice.setText("");
        etName.clearFocus();
        etDescription.clearFocus();
        etPrice.clearFocus();
    }
}
