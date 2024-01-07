package com.example.demo2;

import java.sql.Date;

public class ExpenseData {
    private Integer no;
    private String purpose;
    private String Category;
    private double Amount;
    private Date date;
    private String description;

    public ExpenseData( Integer no,
                        String purpose,
                        String Category,
                        double Amount,
                        Date date,
                        String description){
        this.no = no;
        this.purpose = purpose;
        this.Category = Category;
        this.Amount = Amount;
        this.date = date;
        this.description = description;
    }

    public Integer getNo(){
        return no;
    }
    public String getPurpose(){
        return purpose;
    }
    public String getCategory(){
        return Category;
    }
    public double getAmount(){
        return Amount;
    }
    public Date getDate(){
        return date;
    }
    public String getDescription(){
        return description;
    }

}
