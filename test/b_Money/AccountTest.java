package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;

	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		SweBank.openAccount("Hans");
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}

	@Test
	public void testAddRemoveTimedPayment() {
		// Test adding and removing timed payment
		testAccount.addTimedPayment("payment1", 2, 1, new Money(100, SEK), SweBank, "Alice");
		assertTrue(testAccount.timedPaymentExists("payment1"));

		testAccount.removeTimedPayment("payment1");
		assertFalse(testAccount.timedPaymentExists("payment1"));
	}

	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		testAccount.addTimedPayment("payment2", 1, 1, new Money(50, SEK), SweBank, "Alice");

		SweBank.tick();
		assertEquals(10000000 - 50, testAccount.getBalance().getAmount().intValue());

		SweBank.tick();
		assertEquals(10000000 - 100, testAccount.getBalance().getAmount().intValue());
	}

	@Test
	public void testAddWithdraw() {
		// Test deposit and withdraw
		testAccount.deposit(new Money(500, SEK));
		assertEquals(10000000 + 500, testAccount.getBalance().getAmount().intValue());

		testAccount.withdraw(new Money(200, SEK));
		assertEquals(10000000 + 500 - 200, testAccount.getBalance().getAmount().intValue());
	}

	@Test
	public void testGetBalance() {
		// Test get balance
		assertEquals(10000000, testAccount.getBalance().getAmount().intValue());

		// After deposit, balance should increase
		testAccount.deposit(new Money(500, SEK));
		assertEquals(10000000 + 500, testAccount.getBalance().getAmount().intValue());

		// After withdrawal, balance should decrease
		testAccount.withdraw(new Money(200, SEK));
		assertEquals(10000000 + 500 - 200, testAccount.getBalance().getAmount().intValue());
	}
}
