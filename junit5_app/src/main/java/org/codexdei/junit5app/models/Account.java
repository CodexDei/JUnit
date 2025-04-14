package org.codexdei.junit5app.models;

import org.codexdei.junit5app.exceptions.InsufficientFundsException;

import java.math.BigDecimal;

public class Account {

    private String namePerson;
    private BigDecimal balance;
    private Bank bank;

    public Account(String person, BigDecimal balance){

        this.namePerson = person;
        this.balance = balance;
    }

    public String getNamePerson(){
        return this.namePerson;
    }
    public void setNamePerson(String namePerson){
        this.namePerson = namePerson;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public void debit(BigDecimal ammount){

         BigDecimal newBalance = this.balance.subtract(ammount);

         if (newBalance.compareTo(BigDecimal.ZERO) < 0){
             throw  new InsufficientFundsException("Insufficient funds");
         }

        this.balance = newBalance;

    }
    public void credit(BigDecimal ammount){

        this.balance = this.balance.add(ammount);

    }
    @Override
    public boolean equals(Object obj) {

        if(!(obj instanceof Account)){
            return false;
        }
        Account ac = (Account) obj;

        if (this.namePerson == null || this.balance == null){
            return false;
        }
        return this.namePerson.equals(ac.namePerson) && this.balance.equals(ac.balance);
    }
}
