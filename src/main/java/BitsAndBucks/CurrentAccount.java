package BitsAndBucks;

import java.time.LocalDateTime;

import static Main.Main.generateNumber;

public class CurrentAccount extends BankAccount {
    public CurrentAccount(String accountNumber, double balance, String accountHolderName) {
        super(accountNumber,balance,accountHolderName,"current",0.0,true);
    }

    @Override
    public double calculateInterestRate() {
        return balance*interestRate;
    }
}
