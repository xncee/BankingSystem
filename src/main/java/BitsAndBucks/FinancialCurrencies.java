package BitsAndBucks;

public class FinancialCurrencies {
    public static double convertToJOD(double amount, String currency) {
        double exchangeRate;

        switch (currency.toUpperCase()) {
            case "USD":
                exchangeRate = 0.709;
                break;
            case "KWD":
                exchangeRate = 2.33;
                break;
            case "EUR":
                exchangeRate = 0.77;
                break;
            case "SAR":
                exchangeRate = 0.189;
                break;
            case "GBP":
                exchangeRate = 0.87;
                break;
            default:
                throw new IllegalArgumentException("Unsupported currency: " + currency);
        }

        return amount * exchangeRate;
    }
}
