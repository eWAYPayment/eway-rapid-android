package com.eway.payment.sdk.android.beans;

public class LineItem {
    private String SKU;
    private String Description;
    private int Quantity;
    private int UnitCost;
    private int Tax;
    private int Total;

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getUnitCost() {
        return UnitCost;
    }

    public void setUnitCost(int unitCost) {
        UnitCost = unitCost;
    }

    public int getTax() {
        return Tax;
    }

    public void setTax(int tax) {
        Tax = tax;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }
}
