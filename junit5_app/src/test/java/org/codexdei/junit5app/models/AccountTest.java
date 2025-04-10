package org.codexdei.junit5app.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void testNameAccount() {

        Account account = new Account("Luis", new BigDecimal("1000.12345"));
//        account.setPerson("Pedro");
        String expectedValue = "Luis";
        String actual = account.getPerson();
        assertEquals(expectedValue, actual);
        //assertTrue solo como prueba, siempre es mejor assertEquals
        assertTrue(actual.equals("Luis"));
    }
}