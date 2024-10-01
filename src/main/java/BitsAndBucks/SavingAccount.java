package BitsAndBucks;

public class SavingAccount extends BankAccount {
    public SavingAccount(String accountNumber, double balance, String accountHolderName) {
        super(accountNumber,balance,accountHolderName,"saving",0.025,true);
    }

    @Override
    public double calculateInterestRate() {
        return balance*interestRate;
    }
}
