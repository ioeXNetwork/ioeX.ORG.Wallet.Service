/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos;

/**
 * clark
 * <p>
 * 10/18/18
 */
public class TestFunc {
    @org.junit.Test
    public void testByteReverse(){
        long a = 10000l;
        long b = Long.reverseBytes(a);
        long c = Long.reverseBytes(b);
        System.out.println(a + "  " + b + " " + c);
    }
}
