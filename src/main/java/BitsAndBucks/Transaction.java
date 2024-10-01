package BitsAndBucks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Transaction {

        private String transactionId;
        private String accountNumber;
        private double amount;
        private String transactionType;
        private LocalDateTime date;

        public Transaction(String transactionId, String accountNumber, double amount, String transactionType, LocalDateTime date) {
            this.transactionId = transactionId;
            this.accountNumber = accountNumber;
            this.amount = amount;
            this.transactionType = transactionType;
            this.date = date;
        }

        public String getTransactionId() {
            return transactionId;
        }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getAmount() {
            return amount;
        }
        public String getTransactionType() {
            return transactionType;
        }
        public String getDate() {
            return String.valueOf(LocalDateTime.now());
        }


    public String toString() {
            return "Transaction{" +
                    "transactionId='" + transactionId + '\'' +
                    ", amount=" + amount +
                    ", transactionType='" + transactionType + '\'' +
                    ", date='" + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a")) + '\'' +
                    '}';
        }
    }
