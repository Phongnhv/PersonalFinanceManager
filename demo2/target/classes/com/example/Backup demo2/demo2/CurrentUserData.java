package com.example.demo2;

public class CurrentUserData {
    private final String username;
    private final Currency currency;

    public String getUsername(){
        return username;
    }
    public Currency getCurrency()
    {
        return currency;
    }

    public CurrentUserData(String username, Currency currency)
    {
        this.username = username;
        this.currency = currency;
    }

}
