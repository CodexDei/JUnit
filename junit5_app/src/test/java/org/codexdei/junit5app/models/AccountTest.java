package org.codexdei.junit5app.models;

import org.codexdei.junit5app.exceptions.InsufficientFundsException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

//Cada vez que se ejecuta un metodo de crea una instancia, al usar TestIntance se usa una sola instancia
//NO ES UN BUENA PRACTICA, se recomienda mejor que cada test sea independiente
//Solo usarlo cuando sea estrictamente necesario, por ello lo dejaremos comentado
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountTest {

    Account account;

    @BeforeAll
    static void beforeAll() {
        System.out.println("Starting the test");
    }

    @BeforeEach
    void initMethodTest() {

        this.account = new Account("Luis", new BigDecimal("1000.12345"));
        System.out.println("Starting the method");
    }

    @AfterEach
    void tearDown() {

        System.out.println("Finished the test method");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finished the test");
    }

    //Sirve para anidar test, si algun test de la clase falla, todos fallan
    @Nested
    @DisplayName("Testing atributes of the class Account")
    class AccountTestNameBalance {

        //Todo testing debe tenerlo
        @Test
        //En la ejecucion indica que hace el test
        @DisplayName("Holder's name 🤔")
        //Deshabilita es testing, en caso de que necesitemos arreglarlo y podamos seguir probando o
        //saltar test, por si falta arreglar algo y podamos seguir, en la ejecucion mistrara "ignored" y signo gris
        @Disabled
        void testNameAccount() {
            //Para que falle el metodo
            //fail();
//        account.setPerson("Pedro");
            String expectedValue = "Luis";
            String actual = account.getNamePerson();
            //El segundo argumento es el mensaje de error que se mostrara, es mejor usarlo con "() ->" ya que
            //No costruye el mensaje de error sino hay un error, lo que mejora el rendimiento
            assertNotNull(actual, () -> "Custom Error Message: The account cannot be null");
            assertEquals(expectedValue, actual, () -> "Custom Error Message: The account is not as expected");
            //assertTrue solo como prueba, siempre es mejor assertEquals
            assertTrue(actual.equals("Luis"), () -> "Custom Error Message: The account name is not as expected");
        }

        @Test
        @DisplayName("Balance is not null or negative 🤞")
        void testBalanceAccount() {

            assertNotNull(account.getBalance(), () -> "Custom Error Message: Balance cannot be null");
            assertEquals(1000.12345, account.getBalance().doubleValue(), () -> "Custom Error Message: Value is not as expected");
            assertFalse(account.getBalance().compareTo(BigDecimal.ZERO) < 0, () -> "Custom Error Message: Balance cannot be negative");
            //lo siguiente es lo mismo que el anterior pero usando True, para ello hay que inventir el operador de relacion
            assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0, () -> "Custom Error Message: The balance cannot be negative");
        }

        @Test
        @DisplayName("Verifying that the accounts are equals 😗")
        void testReferenceAccount() {

            Account account2 = new Account("Pepe", new BigDecimal("899000"));

            assertNotEquals(account, account2);
//        assertEquals(account, account2, () -> "Custom Error Message: The accounts is not equals");
        }
    }

    @Nested
    class AccountOperationsTest {


        @Test
        @DisplayName("Managing a debit transaction")
        void testDebitAccount() {

            account.debit(new BigDecimal("100"));
            assertNotNull(account.getBalance());
            assertEquals(900, account.getBalance().intValue());
            assertEquals("900.12345", account.getBalance().toPlainString());
        }

        @Test
        @DisplayName("Managing a credit transaction")
        void testCreditAccount() {

            account.credit(new BigDecimal("100"));
            assertNotNull(account.getBalance());
            assertEquals(1100, account.getBalance().intValue());
            assertEquals("1100.12345", account.getBalance().toPlainString());
        }

        @Test
        @DisplayName("Verifying funds")
        void testInsufficientFundsExceptionAccount() {

            Exception exception = assertThrows(InsufficientFundsException.class, () -> {

                account.debit(new BigDecimal(1500));
            });
            String actual = exception.getMessage();
            String exceptedValue = "Insufficient funds";

            assertEquals(exceptedValue, actual);
        }

        @Test
        @DisplayName("Testing transfer monetary between accounts")
        void testTransferMoneyAccount() {

            Account originAccount = new Account("Pedro", new BigDecimal("2000"));
            Account destinationAccount = new Account("Luis", new BigDecimal("1000"));

            Bank bank = new Bank();
            bank.setName("State Bank");
            bank.transfer(originAccount, destinationAccount, new BigDecimal(200));
            assertEquals("1800", originAccount.getBalance().toPlainString());
            assertEquals("1200", destinationAccount.getBalance().toPlainString());
        }

        //Todo testing debe tenerlo
        @Test
        //En la ejecucion indica que hace el test
        @DisplayName("Testing relationship between the accounts and with the bank using asserAll 🤝")
        //Deshabilita es testing, en caso de que necesitemos arreglarlo y podamos seguir probando o
        //trabjar en otro cosa y se lo salte
        @Disabled
        void testRelationBankAccount() {

            Account originAccount = new Account("Pedro", new BigDecimal("2000"));
            Account destinationAccount = new Account("Luis", new BigDecimal("1000"));

            Bank bank = new Bank();
            bank.addAccount(originAccount);
            bank.addAccount(destinationAccount);

            bank.setName("State Bank");
            bank.transfer(originAccount, destinationAccount, new BigDecimal(200));
            //agrupa varios assert y permite verificar la falla de cada uno, a diferencia que si se coloca
            //de manera independiente, el primero en fallar no dejara ejecutar las demas
            assertAll(
                    () -> assertEquals("1800", originAccount.getBalance().toPlainString(),
                            () -> "Custumer error Message: The balance is not as expected"),
                    () -> assertEquals("1200", destinationAccount.getBalance().toPlainString()),
                    //probando relacion de cuentas a banco
                    () -> assertEquals(2, bank.getAccountList().size()),
                    () -> assertEquals("State Bank", originAccount.getBank().getName()),
                    //probando relacio banco a cuentas
                    () -> assertEquals("Pedro", bank.getAccountList().stream()
                            .filter(ac -> ac.getNamePerson().equals("Pedro"))
                            .findFirst()
                            .get().getNamePerson()),
                    () -> assertTrue(bank.getAccountList().stream()
                            .anyMatch(ac -> ac.getNamePerson().equals("Luis")))
            );
        }
    }

    @Nested
    class OperatingSystemTest {

        @Test
        //Solo se aplica al sistema operativo que se indique
        @EnabledOnOs(OS.WINDOWS)
        void windowsOnlyTest() {
        }

        @Test
        @EnabledOnOs({OS.MAC, OS.LINUX, OS.SOLARIS})
        void otherOSOnlyTest() {
        }

        @Test
        //No se aplica al sistema operativo que se indique
        @DisabledOnOs(OS.WINDOWS)
        void windowsNoTest() {
        }
    }

    @Nested
    class JavaVersionTest {

        //Se aplica el test al JDK java que se indique
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void onlyJDK8() {
        }

        @Test
        @EnabledOnJre(JRE.JAVA_24)
        void onlyJDK24() {
        }

        //No se aplica el test al JDK java que se indique
        @Test
        @DisabledOnJre(JRE.JAVA_24)
        void testNoJDK24() {
        }
    }

    @Nested
    class SystemPropertiesTest {

        @Test
        void printSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((k, v) -> System.out.println("key:" + k + " --- " + "value:" + v));
        }

        @Test
        //se aplica a la propiedad que coincida
        @EnabledIfSystemProperty(named = "java.version", matches = ".*24.*")
        void testJavaVersion() {
        }

        @Test
        @EnabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
        void textWin64() {
        }

        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
        void textNoWin64() {
        }

        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "Yorki")
        void testUserName() {
        }

        @Test
        //hay que agregar esta propiedad, para ello se pulsa en la pestaña al lado de AccountTest y edit Configuration, la encontraras al
        //lado de "ea"
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDev() {
        }
    }

    @Nested
    class VariableEnvironmentTest {

        @Test
        void printEnviromentVaribles() {
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k, v) -> System.out.println("Key: " + k + " -- " + "variable: " + v));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "Os", matches = "Windows_NT")
        void Os() {
        }

        //hay que agregar esta propiedad, para ello se pulsa en la pestaña al lado de AccountTest y edit Configuration, la encontraras en
        //el campo con el mismo nombre ENVIRONMENT variables
        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
        void testEnv() {
        }

        @Test
        @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
        void testEnv2() {
        }

    }

    @Test
    @DisplayName("Testing balance Account Dev")
    void testBalanceAccountDev() {

        boolean dev = "dev".equals(System.getProperty("ENV"));
        //Si se cumple la condicion, lo que este debajo del 'assume' se ejecuta, sino, no genera error pero
        //Se deshabilita el test
        assumeTrue(dev);

        assertNotNull(account.getBalance(), () -> "Custom Error Message: Balance cannot be null");
        assertEquals(1000.12345, account.getBalance().doubleValue(), () -> "Custom Error Message: Value is not as expected");
        assertFalse(account.getBalance().compareTo(BigDecimal.ZERO) < 0, () -> "Custom Error Message: Balance cannot be negative");
        //lo siguiente es lo mismo que el anterior pero usando True, para ello hay que inventir el operador de relacion
        assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0, () -> "Custom Error Message: The balance cannot be negative");
    }

    @Test
    @DisplayName("Testing balance Account Dev 2")
    void testBalanceAccountDev2() {

        boolean isDev = "dev".equals(System.getProperty("ENV"));
        //Si se cumple la condicion, lo que este dentro del 'assuming' se ejecuta, si lo que esta dentro genera
        //algun error, falla toda el test, sino falla se ejecuta con normalidad
        assumingThat(isDev, () -> {

            assertNotNull(account.getBalance(), () -> "Custom Error Message: Balance cannot be null");
            assertEquals(1000.12345, account.getBalance().doubleValue(), () -> "Custom Error Message: Value is not as expected");

        });

        assertFalse(account.getBalance().compareTo(BigDecimal.ZERO) < 0, () -> "Custom Error Message: Balance cannot be negative");
        //lo siguiente es lo mismo que el anterior pero usando True, para ello hay que inventir el operador de relacion
        assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0, () -> "Custom Error Message: The balance cannot be negative");
    }
}