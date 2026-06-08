package com.example.juliecreations.model;

public class CartItem {
    private int productId;
    private String name;
    private float price;
    private int quantity;
    private String instructions;

    public CartItem(int productId, String name, float price, int quantity, String instructions) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.instructions = instructions;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getInstructions() {
        return instructions;
    }

    public float getTotal() {
        return price * quantity;
    }
}