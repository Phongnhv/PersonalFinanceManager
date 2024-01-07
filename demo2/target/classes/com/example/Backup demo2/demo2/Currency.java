package com.example.demo2;

public class Currency {
    private final String[] CurrencyList = {"USD","Euro","VND","Yen", "Pound", "Aussie"};
    private final String type;
    public static double DefaultCurrencytoCustomize(String s, double amount){

        return switch (s) {

            case "USD" -> amount;

            case "Euro" -> amount * 0.92;

            case "VND" -> amount * 24180;

            case "Yen" -> amount * 146.52;

            case "Pound" -> amount * 0.79;

            case "Aussie" -> amount * 1.5;

            default -> 0;
        };
    }

    public static double CustomizeCurrencyToDefault(String s, double amount){
        return switch (s) {
            case "USD" -> amount;

            case "Euro" -> amount / 0.92;

            case "VND" -> amount / 24180;

            case "Yen" -> amount / 146.52;

            case "Pound" -> amount / 0.79;

            case "Aussie" -> amount / 1.5;

            default -> 0;
        };
    }

    public Currency (String type)
    {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
