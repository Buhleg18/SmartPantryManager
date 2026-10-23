package com.richfield.smartpantry;

public class PantryItem {
    private int id;
    private String name;
    private double quantity;
    private String unit;
    private String expiry;

    public PantryItem(int id, String name, double quantity, String unit, String expiry) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiry = expiry;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getExpiry() { return expiry; }

    public void setName(String name) { this.name = name; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setExpiry(String expiry) { this.expiry = expiry; }

    @Override
    public String toString() {
        String s = name + " – " + quantity + " " + unit;
        if (expiry != null && !expiry.isEmpty()) {
            s += " (exp: " + expiry + ")";
        }
        return s;
    }
}
