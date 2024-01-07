package com.example.demo2;

public class Currency {
    private String type;
    public static double USDRatio(String s){

        return switch (s) {

            case "USD" -> 1;

            case "Euro" ->  0.92;

            case "VND" ->  24180;

            case "Yen" ->  146.52;

            case "Pound" ->  0.79;

            case "Aussie" ->  1.5;

            default -> 0;
        };
    }

    public Currency (String type)
    {
        this.type = type;
    }

    public void setType  (String Type){
        this.type = Type;
    }

    public String getType() {
        return type;
    }
}
