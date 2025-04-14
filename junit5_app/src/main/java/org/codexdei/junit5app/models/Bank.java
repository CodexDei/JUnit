package org.codexdei.junit5app.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Bank {

    private List<Account> accountList;
    private String name;

    public Bank(){

        this.accountList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public void addAccount(Account account){

        accountList.add(account);
        account.setBank(this);
    }

    public void transfer(Account origin, Account destination, BigDecimal amount){

        origin.debit(amount);
        destination.credit(amount);
    }
}
