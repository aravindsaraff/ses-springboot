package com.fp.emailservice.util;

import java.util.Base64;

/**
 * Utility function to decode BASE64 encoded string
 *
 */
public final class Base64Decoder {
    // to prevent any instantiation
    private Base64Decoder() {

    }

    public static byte[] decode(String encodedString) {
        return Base64.getDecoder().decode(encodedString);
    }

}
