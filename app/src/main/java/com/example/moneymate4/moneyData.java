package com.example.moneymate4;

public class moneyData {

    private String amount;
    private String name;
    private String type;




    public moneyData() {
        // Default constructor required for Firestore
    }

    public moneyData(String type, String name, String amount) {
        this.type = type;
        this.name = name;
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}