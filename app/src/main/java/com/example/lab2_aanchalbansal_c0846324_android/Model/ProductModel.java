package com.example.lab2_aanchalbansal_c0846324_android.Model;

public class ProductModel {

    int productID;
    String name, description;
    double price;

    public ProductModel(int productID, String name, String description, double price) {
        this.productID = productID;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public int getProductId() {
        return productID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

}
