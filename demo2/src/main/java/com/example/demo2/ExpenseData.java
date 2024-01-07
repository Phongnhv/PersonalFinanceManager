package com.example.demo2;

import java.sql.Date;

public class ExpenseData {
    private final Integer no;
    private final String purpose;
    private final String Category;
    private final double Amount;
    private final Date date;
    private final String description;

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
