package com.example.moneymate4;

public class User {

    private String name;
    private String phone;

    private String email;
    private String password;

    private String cvv;
    private String username;
    private String cardnumber;

    // Default constructor (required by Firestore)
    public User(){

    }


    public User(String name, String phone, String email, String password, String cvv, String username, String cardnumber) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.cvv=cvv;
        this.username=username;
        this.cardnumber=cardnumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }


}

