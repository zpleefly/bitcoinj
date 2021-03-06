/**
 * Copyright 2014 Andreas Schildbach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.bitcoin.core;

import static com.google.bitcoin.core.Coin.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

public class CoinTest {

    @Test
    public void testParseCoin() {
        // String version
        assertEquals(CENT, parseCoin("0.01"));
        assertEquals(CENT, parseCoin("1E-2"));
        assertEquals(COIN.add(CENT), parseCoin("1.01"));
        try {
            parseCoin("2E-20");
            org.junit.Assert.fail("should not have accepted fractional satoshis");
        } catch (ArithmeticException e) {
        }

        // int version
        assertEquals(CENT, valueOf(0, 1));

        try {
            valueOf(1, -1);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            valueOf(-1, 0);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            parseCoin("-1");
            fail();
        } catch (ArithmeticException e) {}
    }

    @Test
    public void testToFriendlyString() {
        assertEquals("1.00", COIN.toFriendlyString());
        assertEquals("1.23", valueOf(1, 23).toFriendlyString());
        assertEquals("0.001", COIN.divide(1000).toFriendlyString());
        assertEquals("-1.23", valueOf(1, 23).negate().toFriendlyString());
    }

    /**
     * Test the bitcoinValueToPlainString amount formatter
     */
    @Test
    public void testToPlainString() {
        assertEquals("0.0015", Coin.valueOf(150000).toPlainString());
        assertEquals("1.23", parseCoin("1.23").toPlainString());

        assertEquals("0.1", parseCoin("0.1").toPlainString());
        assertEquals("1.1", parseCoin("1.1").toPlainString());
        assertEquals("21.12", parseCoin("21.12").toPlainString());
        assertEquals("321.123", parseCoin("321.123").toPlainString());
        assertEquals("4321.1234", parseCoin("4321.1234").toPlainString());
        assertEquals("54321.12345", parseCoin("54321.12345").toPlainString());
        assertEquals("654321.123456", parseCoin("654321.123456").toPlainString());
        assertEquals("7654321.1234567", parseCoin("7654321.1234567").toPlainString());
        try {
            assertEquals("87654321.12345678", parseCoin("87654321.12345678").toPlainString());
            Assert.fail();  // More than MAX_MONEY
        } catch (Exception e) {}

        // check there are no trailing zeros
        assertEquals("1", parseCoin("1.0").toPlainString());
        assertEquals("2", parseCoin("2.00").toPlainString());
        assertEquals("3", parseCoin("3.000").toPlainString());
        assertEquals("4", parseCoin("4.0000").toPlainString());
        assertEquals("5", parseCoin("5.00000").toPlainString());
        assertEquals("6", parseCoin("6.000000").toPlainString());
        assertEquals("7", parseCoin("7.0000000").toPlainString());
        assertEquals("8", parseCoin("8.00000000").toPlainString());
    }
}
