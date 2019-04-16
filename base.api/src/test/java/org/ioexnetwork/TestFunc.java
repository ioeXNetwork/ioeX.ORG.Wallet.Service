/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.ioexnetwork;


public class TestFunc {
    @org.junit.Test
    public void testByteReverse(){
        long b = Long.reverseBytes(9899999900l);
        System.out.println( "  " + b + " " );
    }

}
