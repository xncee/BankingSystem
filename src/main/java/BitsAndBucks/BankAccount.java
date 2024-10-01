package BitsAndBucks;

import IO.JsonFileWriter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static Main.Main.generateNumber;

public abstract class BankAccount implements Data {
    protected String accountNumber;
    protected double balance;
    protected String accountHolderName;
    protected final String accountType;
    protected final double interestRate;
    protected List<Transaction> transactionList =new ArrayList<>();
    private boolean isActive;
    public BankAccount(String accountNumber, double balance, String accountHolderName, String accountType, double interestRate, boolean isActive) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountHolderName = accountHolderName;
        this.accountType = accountType;
        this.interestRate = interestRate;
        this.isActive = isActive;
        initializeTransactions();
    }
    public void initializeTransactions() {
        String encAccountNumber = Encryption.encrypt(accountNumber);
        Iterator<String> ids = transactionsNode.fieldNames();
        for (JsonNode n: transactionsNode) {
            if (n==null) continue;
            String id = ids.next();
            if (n.get("accountNumber").asText().equals(encAccountNumber)) {
                double amount = n.get("amount").asDouble();
                String transactionType = n.get("transactionType").asText();
                LocalDateTime date = LocalDateTime.parse(n.get("date").asText());
                transactionList.add(new Transaction(id, accountNumber, amount, transactionType, date));
            }
        }
    }

    protected void saveTransaction(Transaction transaction) {
        transactionList.add(transaction);
        ObjectNode tempNode = JsonFileWriter.getNewNode();
        tempNode.put("amount", transaction.getAmount());
        tempNode.put("accountNumber", Encryption.encrypt(transaction.getAccountNumber()));
        tempNode.put("transactionType", transaction.getTransactionType());
        tempNode.put("date", String.valueOf(transaction.getDate()));

        transactionsNode.put(transaction.getTransactionId(), tempNode);
        transactionsWriter.save();
    }

    protected void updateBalance() {
        ObjectNode tempNode = (ObjectNode) accountsNode.get(Encryption.encrypt(accountNumber));
        tempNode.put("balance", balance);
        accountsWriter.save();
    }

    protected boolean checkSufficientBalance(double amount) {
        return balance>=amount;
    }

    public boolean withdraw(double amount){
        if (!checkSufficientBalance(amount)) return false;

        balance-=amount;
        updateBalance();
        Transaction transaction = new Transaction("WIT#"+ generateNumber(), accountNumber, -amount, "Withdraw", LocalDateTime.now());
        saveTransaction(transaction);
        return true;
    }

    public void deposit(double amount) {
        balance += amount;
        updateBalance();
        Transaction transaction = new Transaction("DEP#"+ generateNumber(), accountNumber, +amount, "Deposit", LocalDateTime.now());
        saveTransaction(transaction);
    }

    public boolean transfer(BankAccount receiver, double amount) {
        if (!checkSufficientBalance(amount))
            return false;

        balance -= amount;
        updateBalance();
        receiver.balance += amount;
        receiver.updateBalance();
        // no to need to encrypt account number here, as it will be encrypted in 'saveTransaction()' method.
        Transaction transaction1 = new Transaction("TRA#"+ generateNumber(), accountNumber, -amount, "Transfer", LocalDateTime.now());
        saveTransaction(transaction1);
        Transaction transaction2 = new Transaction("TRA#"+ generateNumber(), receiver.getAccountNumber(), +amount, "Transfer", LocalDateTime.now());
        receiver.saveTransaction(transaction2);
        return true;
    }

    public void closeAccount() {
        if (balance == 0) {
            isActive = false;
            accountsNode.remove(Encryption.encrypt(accountNumber));
            accountsWriter.save();
        } else {
            System.out.println("Please withdraw all funds before closing the account.");
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public abstract double calculateInterestRate();

    public void printAccountStatement() {
        if (transactionList.isEmpty()) {
            System.out.println("No Transactions.");
            return;
        }
        System.out.println("Account Statement for Account Number: " + accountNumber);
        for (Transaction transaction : transactionList) {
            System.out.println(transaction.toString());
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getAccountType() {
        return accountType.toLowerCase();
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public String toString() {
        return "Account Number: " + accountNumber +
                "\nAccount Holder Name: " + accountHolderName +
                "\nAccount Type: " + accountType +
                "\nBalance: " + balance;
    }
}
