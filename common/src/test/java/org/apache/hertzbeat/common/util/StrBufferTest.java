package org.apache.hertzbeat.common.util;

import org.junit.jupiter.api.Test;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * test case for {@link StrBuffer}
 */

class StrBufferTest {

	private static final String POSITIVE_INF = "+inf";
	private static final String NEGATIVE_INF = "-inf";
	private static final long POSITIVE_INF_VALUE = 0x7FF0000000000000L;
	private static final long NEGATIVE_INF_VALUE = 0xFFF0000000000000L;

	@Test
	void testRead() {

		StrBuffer buffer = new StrBuffer("hello");

		assertEquals('h', buffer.read());
		assertEquals('e', buffer.read());
		assertEquals('l', buffer.read());
		assertEquals('l', buffer.read());
		assertEquals('o', buffer.read());
		assertThrows(IndexOutOfBoundsException.class, buffer::read);
	}

	@Test
	void testRollback() {

		StrBuffer buffer = new StrBuffer("hello");

		assertEquals('h', buffer.read());
		buffer.rollback();
		assertEquals('h', buffer.read());
		buffer.read();
		buffer.read();
		buffer.rollback();
		assertEquals('l', buffer.read());
	}

	@Test
	void testCharAt() {

		StrBuffer buffer = new StrBuffer("hello");

		assertEquals('h', buffer.charAt(0));
		assertEquals('e', buffer.charAt(1));
		assertEquals('l', buffer.charAt(2));
		assertEquals('l', buffer.charAt(3));
		assertEquals('o', buffer.charAt(4));
		assertThrows(IndexOutOfBoundsException.class, () -> buffer.charAt(5));
	}

	@Test
	void testToStr() {

		StrBuffer buffer = new StrBuffer("hello");

		assertEquals("hello", buffer.toStr());
		buffer.read();
		assertEquals("ello", buffer.toStr());
	}

	@Test
	void testToDouble() {

		StrBuffer buffer = new StrBuffer("123.45");
		assertEquals(123.45, buffer.toDouble());

		buffer = new StrBuffer("+inf");
		assertEquals(POSITIVE_INFINITY, buffer.toDouble());

		buffer = new StrBuffer("-inf");
		assertEquals(NEGATIVE_INFINITY, buffer.toDouble());
	}

	@Test
	void testToLong() {

		StrBuffer buffer = new StrBuffer("12345");
		assertEquals(12345L, buffer.toLong());

		buffer = new StrBuffer("+inf");
		assertEquals(POSITIVE_INF_VALUE, buffer.toLong());

		buffer = new StrBuffer("-inf");
		assertEquals(NEGATIVE_INF_VALUE, buffer.toLong());
	}

	@Test
	void testSkipBlankTabs() {

		StrBuffer buffer = new StrBuffer("  \t  hello  \t  ");
		buffer.skipBlankTabs();
		assertEquals("hello", buffer.toStr());
	}

	@Test
	void testIsEmpty() {

		StrBuffer buffer = new StrBuffer("");
		assertTrue(buffer.isEmpty());

		buffer = new StrBuffer("  \t  ");
		buffer.skipBlankTabs();
		assertTrue(buffer.isEmpty());

		buffer = new StrBuffer("hello");
		assertFalse(buffer.isEmpty());
	}

	@Test
	void testParseLong() {

		assertEquals(12345L, StrBuffer.parseLong("12345"));
		assertEquals(POSITIVE_INF_VALUE, StrBuffer.parseLong("+inf"));
		assertEquals(NEGATIVE_INF_VALUE, StrBuffer.parseLong("-inf"));
	}

	@Test
	void testParseDouble() {

		assertEquals(123.45, StrBuffer.parseDouble("123.45"));
		assertEquals(POSITIVE_INFINITY, StrBuffer.parseDouble("+inf"));
		assertEquals(NEGATIVE_INFINITY, StrBuffer.parseDouble("-inf"));
	}

}
