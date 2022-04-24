package com.xxf.hash;

import java.math.BigInteger;
import java.util.zip.Checksum;

/** Checksum interface to access 128 bit in various ways. */
public interface Checksum128 extends Checksum {
    /** Returns the higher 64 bits of the 128 bit hash. */
    long getValueHigh();

    /** Positive value. */
    BigInteger getValueBigInteger();

    /** Padded with leading 0s to ensure length of 32. */
    String getValueHexString();

    /** Big endian is the default in Java / network byte order. */
    byte[] getValueBytesBigEndian();

    /** Big endian is used by most machines natively. */
    byte[] getValueBytesLittleEndian();
}
