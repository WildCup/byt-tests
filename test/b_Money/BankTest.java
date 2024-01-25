package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;

	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	@Test
	public void testGetName() {
		assertEquals("SweBank", SweBank.getName());
		assertEquals("Nordea", Nordea.getName());
		assertEquals("DanskeBank", DanskeBank.getName());
	}

	@Test
	public void testGetCurrency() {
		assertEquals(SEK, SweBank.getCurrency());
		assertEquals(SEK, Nordea.getCurrency());
		assertEquals(DKK, DanskeBank.getCurrency());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
		try {
			SweBank.openAccount("Ulrika");
			fail("Expected AccountExistsException was not thrown");
		} catch (AccountExistsException e) {
			// Expected exception
		}
	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException {
		SweBank.deposit("Bob", new Money(100, SEK));
		assertEquals(100, SweBank.getBalance("Bob").intValue());
	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		SweBank.deposit("Bob", new Money(100, SEK));
		SweBank.withdraw("Bob", new Money(50, SEK));
		assertEquals(50, SweBank.getBalance("Bob").intValue());
	}

	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		assertEquals(0, SweBank.getBalance("Ulrika").intValue());
		SweBank.deposit("Ulrika", new Money(200, SEK));
		assertEquals(200, SweBank.getBalance("Ulrika").intValue());
	}

	@Test
	public void testTransfer() throws AccountDoesNotExistException {
		SweBank.deposit("Bob", new Money(100, SEK));
		SweBank.transfer("Bob", DanskeBank, "Gertrud", new Money(50, SEK));
		assertEquals(50, SweBank.getBalance("Bob").intValue());
		assertEquals(35, DanskeBank.getBalance("Gertrud").intValue());
	}

	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		SweBank.deposit("Bob", new Money(100, SEK));
		SweBank.addTimedPayment("Bob", "Salary", 1, 1, new Money(50, SEK), DanskeBank, "Gertrud");
		SweBank.tick(); // One tick to trigger the timed payment
		assertEquals(50, SweBank.getBalance("Bob").intValue());
		assertEquals(35, DanskeBank.getBalance("Gertrud").intValue());
	}
}
