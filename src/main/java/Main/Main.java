package Main;

import IO.JsonFileWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import BitsAndBucks.*;
import java.util.Random;
import java.util.Scanner;

public class Main implements Data {

    static Scanner input = new Scanner(System.in);
    private static BankAccount account;

    public static void main(String[] args) {
        homePage();
    }

    public static void waitFor(int seconds) {
        try {
            Thread.sleep(seconds*1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean LogIn(String accountNumber){
        accountNumber = Encryption.encrypt(accountNumber);
        if (accountsNode.get(accountNumber)==null) {
            System.out.println("Account not found.");
            return false;
        }
        System.out.println("Welcome "+accountsNode.get(accountNumber).get("name").asText()+",\nPlease enter your password: ");
        String password = input.nextLine();
        password = Encryption.encrypt(password);
        if (!accountsNode.get(accountNumber).get("password").asText().equals(password)) {
            System.out.println("incorrect password");
            return false;
        }
        return true;
    }

    public static void homePage() {
        waitFor(1);
        System.out.println("\n# Home Page");
        System.out.println("1. My Account");
        System.out.println("2. Create Bank Account");
        System.out.println("99. Exit");
        int c = getUserInput(new int[] {1,2,99});
        switch (c) {
            case 1: {
                System.out.print("Enter your Account number: ");
                String accountNumber = input.nextLine();
                boolean loggedIn = LogIn(accountNumber);
                if (!loggedIn) break;

                account = getAccount(accountNumber);
                System.out.println("Logged in.");
                myAccountPage();
                break;
            }
            case 2: {
                createAccountPage();
                break;
            }
            case 99:
                System.exit(1);
                break;
        }
        homePage();
    }

    public static String generateNumber() {
        String output = "";
        Random rand = new Random();
        for (int i=0; i<10; i++) {
            output += rand.nextInt(0, 10);
        }
        return output;
    }

    public static BankAccount getAccount(String accountNumber) {
        accountNumber = Encryption.encrypt(accountNumber);
        if (accountsNode.get(accountNumber)==null) {
            return null;
        }

        String name = accountsNode.get(accountNumber).get("name").asText();
        String accountType = accountsNode.get(accountNumber).get("accountType").asText();
        double balance = accountsNode.get(accountNumber).get("balance").asDouble();
        BankAccount account;
        accountNumber = Encryption.decrypt(accountNumber);
        if (accountType.equalsIgnoreCase("current"))
            account = new CurrentAccount(accountNumber, balance, name);
        else
            account = new SavingAccount(accountNumber, balance, name);

        return account;
    }

    public static double convertAmount(String stringAmount) {
        stringAmount = stringAmount.toUpperCase();
        try {
            if (stringAmount.startsWith("JOD")) {
                return Double.parseDouble(stringAmount.split("JOD")[1]);
            }
            else if (stringAmount.startsWith("USD")) {
                double amount = Double.parseDouble(stringAmount.split("USD")[1]);
                return FinancialCurrencies.convertToJOD(amount, "USD");
            }
            else if (stringAmount.startsWith("EUR")) {
                double amount = Double.parseDouble(stringAmount.split("EUR")[1]);
                return FinancialCurrencies.convertToJOD(amount, "EUR");
            }
            else if (stringAmount.startsWith("KWD")) {
                double amount = Double.parseDouble(stringAmount.split("KWD")[1]);
                return FinancialCurrencies.convertToJOD(amount, "KWD");
            }
            else if (stringAmount.startsWith("SAR")) {
                double amount = Double.parseDouble(stringAmount.split("SAR")[1]);
                return FinancialCurrencies.convertToJOD(amount, "SAR");
            }
            else if (stringAmount.startsWith("GBP")) {
                double amount = Double.parseDouble(stringAmount.split("GBP")[1]);
                return FinancialCurrencies.convertToJOD(amount, "GBP");
            }
            else {
                try {
                    return Double.parseDouble(stringAmount);
                } catch (Exception e) {
                    return -1;
                }
            }
        }
        catch (Exception e) {
            return -1;
        }
    }

    public static double getAmount() {
        String stringAmount;
        double amount;
        while (true) {
            System.out.println("Enter amount: ");
            stringAmount = input.nextLine();
            amount = convertAmount(stringAmount);
            if (amount<0) { // ==-1 or < 0
                System.out.println("Invalid amount!");
                continue;
            }
            break;
        }

        return amount;
    }

    public static void myAccountPage() {
        waitFor(1);
        System.out.println("\n# My Account Page");
        System.out.println("*Your Balance : "+account.getBalance());
        System.out.println("1. Withdraw");
        System.out.println("2. Deposit");
        System.out.println("3. Transfer funds");
        System.out.println("4. Update your password");
        System.out.println("5. Print Account Statement");
        System.out.println("6. Close Account");
        System.out.println("7. Logout");
        System.out.println("99. <<");
        int c = getUserInput(new int[]{1,2,3,4,5,6,7,99});

        switch(c){
            case 1:{
                // withdraw
                double amount = getAmount();
                //double amount = input.nextDouble(); input.nextLine();
                if (account.withdraw(amount)) {
                    System.out.println("The withdraw of JOD"+amount+" has been done successfully.");
                }
                else {
                    System.out.println("Insufficient Balance.");
                }
                break;
            }
            case 2:{
                // deposit
                double amount = getAmount();
                //double amount = input.nextDouble(); input.nextLine();
                account.deposit(amount);
                System.out.println("The deposit of JOD"+amount+" has been done successfully.");
                break;
            }
            case 3: {
                System.out.println("Enter target account number: ");
                String targetAccountNumber = input.nextLine();
                BankAccount targetAccount = getAccount(targetAccountNumber);
                if (targetAccount==null) {
                    System.out.println("Account not found.");
                    break;
                }

                double amount = getAmount();
                //double amount = input.nextDouble(); input.nextLine();
                transferFunds(targetAccount, amount);
                break;
            }

            case 4: {
                // updatePassword
                System.out.print("Enter your current password : ");
                String currentPassword = input.nextLine();
                currentPassword = Encryption.encrypt(currentPassword);

                String accountNumber = Encryption.encrypt(account.getAccountNumber());
                String correctPassword = accountsNode.get(accountNumber).get("password").asText();
                if (!correctPassword.equals(currentPassword)) {
                    System.out.println("incorrect password");
                    break;
                }

                System.out.print("Crate new password : ");
                String newPassword = input.nextLine();
                newPassword = Encryption.encrypt(newPassword);
                ObjectNode tempNode = (ObjectNode) accountsNode.get(accountNumber); // accountNumber was already encrypted
                tempNode.put("password", newPassword);
                accountsWriter.save();
                System.out.println("Your password has been successfully changed.");
                break;
            }
            case 5:{
                // printAccountStatement
                account.printAccountStatement();
                break;
            }
            case 6: {
                System.out.print("Enter your Account number: ");
                String accountNumber = input.nextLine();
                boolean loggedIn = LogIn(accountNumber);
                if (!loggedIn) break;

                account = getAccount(accountNumber);
                if (account==null) {
                    System.out.println("Account not found.");
                    break;
                }

                account.closeAccount();
                if (!account.isActive()) {
                    System.out.println("Account closed");
                    account = null;
                    System.out.println("You have been logged out.");
                    homePage();
                }
                else {
                    System.out.println("Account wasn't closed.");
                }
                break;
            }
            case 7: {
                account = null;
                homePage();
                break;
            }
            case 99:
                homePage();
                break;
        }
        myAccountPage();
    }
    public static void depositPage(){}
    public static void withdrawPage(){}
    public static void transferFunds(BankAccount targetAccount, double amount) {
        if (targetAccount==null) {
            System.out.println("Account not found.");
            return;
        }

        if (!account.transfer(targetAccount, amount)) {
            System.out.println("Insufficient funds for transfer.");
            return;
        }
        System.out.println("JOD"+amount + " has been transferred successfully to "+targetAccount.getAccountNumber());
    }
    public static void createAccountPage() {
        System.out.println("\n# Create Account Page");
        System.out.println("1. Current Account");
        System.out.println("2. Saving Account");
        System.out.println("99. <<");
        int c = getUserInput(new int[] {1, 2, 99});

        switch(c) {
            case 1: {
                String accountNumber = "CUR"+generateNumber();
                accountNumber=Encryption.encrypt(accountNumber);
                double balance = 0.0;
                System.out.println("Enter your name: ");
                String name = input.nextLine();
                System.out.println("Now create your password ");
                String password = input.nextLine();
                password = Encryption.encrypt(password);
                ObjectNode tempNode = JsonFileWriter.getNewNode();
                tempNode.put("name", name);
                tempNode.put("password",password);
                tempNode.put("balance", balance);
                tempNode.put("accountType","current");
                tempNode.put("isActive",true);
                accountsNode.put(accountNumber, tempNode);
                accountsWriter.save();
                accountNumber=Encryption.decrypt(accountNumber);
                System.out.println("This is your account number: "+accountNumber);
                waitFor(1);
                System.out.println("Account successfully created.");
                break;
            }
            case 2: {
                String accountNumber = "SAV"+generateNumber();
                accountNumber=Encryption.encrypt(accountNumber);
                double balance = 0.0;
                System.out.println("Enter your name: ");
                String name = input.nextLine();
                System.out.println("Now create your password ");
                String password = input.nextLine();
                password = Encryption.encrypt(password);
                ObjectNode tempNode = JsonFileWriter.getNewNode();
                tempNode.put("name", name);
                tempNode.put("password",password);
                tempNode.put("balance", balance);
                tempNode.put("accountType","saving");
                tempNode.put("isActive",true);
                accountsNode.put(accountNumber, tempNode);
                accountsWriter.save();
                accountNumber=Encryption.decrypt(accountNumber);
                System.out.println("This is your account number : "+accountNumber);
                waitFor(1);
                System.out.println("Account successfully created");
                break;
            }
            case 99:
                homePage();
                break;
        }
        homePage();
    }
    public static int getUserInput(int[] choices) {
        int choice;
        try {
            choice = input.nextInt();
            input.nextLine();
            for (int c: choices) {
                if (c==choice) {
                    return choice;
                }
            }
        }
        catch (Exception e) {
            input.nextLine();
        }
        System.out.println("Invalid input!");
        return getUserInput(choices);
    }
}
