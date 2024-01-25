package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;

	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
		assertEquals(Integer.valueOf(10000), SEK100.getAmount());
		assertEquals(Integer.valueOf(0), SEK0.getAmount());
		assertEquals(Integer.valueOf(-10000), SEKn100.getAmount());
	}

	@Test
	public void testGetCurrency() {
		assertEquals(SEK, SEK100.getCurrency());
		assertEquals(EUR, EUR10.getCurrency());
	}

	@Test
	public void testToString() {
		assertEquals("100,00 SEK", SEK100.toString());
		assertEquals("0,00 SEK", SEK0.toString());
		assertEquals("-100,00 SEK", SEKn100.toString());
	}

	@Test
	public void testGlobalValue() {
		assertEquals(Integer.valueOf(1500), SEK100.universalValue());
		assertEquals(Integer.valueOf(1500), EUR10.universalValue());
		assertEquals(Integer.valueOf(3000), SEK200.universalValue());
		assertEquals(Integer.valueOf(3000), EUR20.universalValue());
	}

	@Test
	public void testEqualsMoney() {
		Money SEK100Copy = new Money(10000, SEK);
		Money EUR10Copy = new Money(1000, EUR);

		assertTrue(SEK100.equals(SEK100Copy));
		assertTrue(EUR10.equals(EUR10Copy));
		assertFalse(SEK100.equals(SEK200));
		assertFalse(EUR10.equals(EUR20));
	}

	@Test
	public void testAdd() {
		Money result = SEK100.add(EUR10);
		assertEquals(Integer.valueOf(20000), result.getAmount());
		assertEquals(SEK, result.getCurrency());

		Money result2 = EUR10.add(SEK100);
		assertEquals(Integer.valueOf(2000), result2.getAmount());
		assertEquals(EUR, result2.getCurrency());
	}

	@Test
	public void testSub() {
		Money result = SEK100.sub(EUR10);
		assertEquals(Integer.valueOf(0), result.getAmount());
		assertEquals(SEK, result.getCurrency());

		Money result2 = EUR10.sub(SEK100);
		assertEquals(Integer.valueOf(0), result2.getAmount());
		assertEquals(EUR, result2.getCurrency());
	}

	@Test
	public void testIsZero() {
		assertTrue(SEK0.isZero());
		assertFalse(SEK100.isZero());
		assertFalse(SEKn100.isZero());
	}

	@Test
	public void testNegate() {
		Money negatedSEK100 = SEK100.negate();
		assertEquals(Integer.valueOf(-10000), negatedSEK100.getAmount());
		assertEquals(SEK, negatedSEK100.getCurrency());

		Money negatedEUR10 = EUR10.negate();
		assertEquals(Integer.valueOf(-1000), negatedEUR10.getAmount());
		assertEquals(EUR, negatedEUR10.getCurrency());
	}

	@Test
	public void testCompareTo() {
		assertTrue(SEK100.compareTo(SEK200) < 0);
		assertTrue(SEK200.compareTo(SEK100) > 0);
		assertTrue(SEK100.compareTo(SEK100) == 0);

		assertTrue(EUR10.compareTo(EUR20) < 0);
		assertTrue(EUR20.compareTo(EUR10) > 0);
		assertTrue(EUR10.compareTo(EUR10) == 0);

		assertTrue(SEK100.compareTo(EUR10) == 0); // Different currencies, should be considered equal
	}
}
