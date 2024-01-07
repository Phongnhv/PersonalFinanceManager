package com.example.demo2;

import java.sql.Date;

public class IncomeData {
    private String Source;
    private double Amount;
    private String Category;
    private Date date;
    private int No;

    public IncomeData(int No, String Source, double Amount, String Category,Date date){
        this.No = No;
        this.Source = Source;
        this.Amount = Amount;
        this.Category = Category;
        this.date = date;
    }

    public int getNo() {
        return No;
    }

    public String getSource() {
        return Source;
    }

    public double getAmount() {
        return Amount;
    }

    public String getCategory() {
        return Category;
    }

    public Date getDate() {
        return date;
    }
}
