package org.ioexnetwork.ioex;

import org.ioexnetwork.ioex.bitcoinj.Utils;

import javax.xml.bind.DatatypeConverter;


public class Common {
    public final static byte[] IOEX_ASSETID = Utils.reverseBytes(DatatypeConverter.parseHexBinary("61ccbfae9f8ce9660a71321041917139cb72cbb85bd105e92f0ed32cb1d1298f"));
}
