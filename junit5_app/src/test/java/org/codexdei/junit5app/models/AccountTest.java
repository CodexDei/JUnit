package org.codexdei.junit5app.models;

import org.codexdei.junit5app.exceptions.InsufficientFundsException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void testNameAccount() {

        Account account = new Account("Luis", new BigDecimal("1000.12345"));
//        account.setPerson("Pedro");
        String expectedValue = "Luis";
        String actual = account.getNamePerson();
        assertNotNull(actual);
        assertEquals(expectedValue, actual);
        //assertTrue solo como prueba, siempre es mejor assertEquals
        assertTrue(actual.equals("Luis"));
    }

    @Test
    void testBalanceAccount() {

        Account account = new Account("Luis", new BigDecimal("1000.12345"));
        assertNotNull(account.getBalance());
        assertEquals(1000.12345,account.getBalance().doubleValue());
        assertFalse(account.getBalance().compareTo(BigDecimal.ZERO) < 0);
        //lo siguiente es lo mismo que el anterior pero usando True, para ello hay que inventir el operador de relacion
        assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testReferenceAccount() {

        Account account = new Account("Pepe", new BigDecimal("899000"));
        Account account2 = new Account("Pepe", new BigDecimal("899000"));

//      assertNotEquals(account, account2);
        assertEquals(account, account2);
    }

    @Test
    void testDebitAccount() {

        Account account = new Account("Luis", new BigDecimal("1000.12345"));
        account.debit(new BigDecimal("100"));
        assertNotNull(account.getBalance());
        assertEquals(900, account.getBalance().intValue());
        assertEquals("900.12345", account.getBalance().toPlainString());
    }

    @Test
    void testCreditAccount() {

        Account account = new Account("Luis", new BigDecimal("1000.12345"));
        account.credit(new BigDecimal("100"));
        assertNotNull(account.getBalance());
        assertEquals(1100, account.getBalance().intValue());
        assertEquals("1100.12345", account.getBalance().toPlainString());
    }

    @Test
    void testInsufficientFundsExceptionAccount() {

        Account account = new Account("Luis", new BigDecimal("1000.12345"));

        Exception exception = assertThrows(InsufficientFundsException.class, () ->{

            account.debit(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String exceptedValue = "Insufficient funds";

        assertEquals(exceptedValue, actual);
    }

    @Test
    void testTransferMoneyAccount() {

        Account originAccount = new Account("Pedro", new BigDecimal("2000"));
        Account destinationAccount = new Account("Luis", new BigDecimal("1000"));

        Bank bank = new Bank();
        bank.setName("State Bank");
        bank.transfer(originAccount, destinationAccount, new BigDecimal(200));
        assertEquals("1800",originAccount.getBalance().toPlainString());
        assertEquals("1200",destinationAccount.getBalance().toPlainString());
    }

    @Test
    void testRelationBankAccount() {

        Account originAccount = new Account("Pedro", new BigDecimal("2000"));
        Account destinationAccount = new Account("Luis", new BigDecimal("1000"));

        Bank bank = new Bank();
        bank.addAccount(originAccount);
        bank.addAccount(destinationAccount);

        bank.setName("State Bank");
        bank.transfer(originAccount, destinationAccount, new BigDecimal(200));
        assertEquals("1800",originAccount.getBalance().toPlainString());
        assertEquals("1200",destinationAccount.getBalance().toPlainString());
        //probando relacion de cuentas a banco
        assertEquals(2, bank.getAccountList().size());
        assertEquals("State Bank", originAccount.getBank().getName());
        //probando relacio banco a cuentas
        assertEquals("Pedro", bank.getAccountList().stream()
                .filter(ac -> ac.getNamePerson().equals("Pedro"))
                .findFirst()
                .get().getNamePerson());

        assertTrue(bank.getAccountList().stream()
                .anyMatch(ac -> ac.getNamePerson().equals("Luis")));
    }
}