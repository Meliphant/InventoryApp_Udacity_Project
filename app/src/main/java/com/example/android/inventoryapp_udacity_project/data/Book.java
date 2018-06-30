package com.example.android.inventoryapp_udacity_project.data;

public class Book {
    private int id;
    private String name;
    private int quantity;
    private String supplierName;
    private String supplierPhone;
    private int price;

    public Book(int id, String name, int quantity, int price, String supplierName, String supplierPhone) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.supplierName = supplierName;
        this.supplierPhone = supplierPhone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public String getSupplierName() {
        return supplierName;
    }
    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", supplierName=" + supplierName +
                ", supplierPhone=" + supplierPhone +
                ", price=" + price +
                '}';
    }
}