package BitsAndBucks;

import requests.Request;

import java.net.http.HttpResponse;

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
    public static double convert(double amount, String BASE_CODE, String to) {
        BASE_CODE = BASE_CODE.toUpperCase();
        String URL = "https://open.er-api.com/v6/latest/"+BASE_CODE;
        HttpResponse<String> req = new Request(URL ,"GET").send();
        double result;
        try {
            result = Double.parseDouble(String.valueOf(Request.convertToJson(req.body()).get("rates").get(to)));
        }
        catch (Exception e) {
            return -1;
        }
        //System.out.println(result);
        return amount*result;
    }
}
