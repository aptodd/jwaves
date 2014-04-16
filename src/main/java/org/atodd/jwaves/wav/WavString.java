package org.atodd.jwaves.wav;

import com.google.common.base.Preconditions;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * A class the conforms to the structure of a String in a WAV file. Strings in WAV files are
 * structured as follows:
 * 
 * [<String Length>][char 1][char 2][char 3]...[char N]
 * 
 * getBytes() returns the WavString in byte format with characters encoded in UTF-8
 * 
 * Strings backed by this type must be less than 2^8 in size, since the length component can only be
 * 1 byte big
 */
public class WavString {
    private static final String CHAR_ENCODING = "UTF-8";
    protected static final int MAX_CHARS = 0x8;
    private final byte length;
    private final byte[] bytes;

    private WavString(byte[] bytes) {
        Preconditions.checkArgument(bytes.length <= MAX_CHARS);
        this.length = (byte) bytes.length;
        this.bytes = Arrays.copyOf(bytes, bytes.length);
    }

    /**
     * Generates a WavString from an input byte[]
     * 
     * @param value
     * @return
     * @throws IllegalArgumentException
     *             If the byte[] length is > 2^8
     */
    public static WavString from(byte[] value) {
        return new WavString(removeTrailingPadding(value));
    }

    /**
     * Generates a WavString from an input String
     * 
     * @param value
     * @return
     * @throws UnsupportedEncodingException
     *             If the system does not support UTF-8 encoding
     * @throws IllegalArgumentException
     *             If the String length is > 2^8
     */
    public static WavString from(String value) throws UnsupportedEncodingException {
        return from(value.getBytes(CHAR_ENCODING));
    }

    /**
     * Generates a WavString from an input char[]
     * 
     * @param value
     * @return
     * @throws UnsupportedEncodingException
     *             If the system does not support UTF-8 encoding
     * @throws IllegalArgumentException
     *             If the char[] length is > 2^8
     */
    public static WavString from(char[] value) throws UnsupportedEncodingException {
        return from(new String(value));
    }

    @Override
    public String toString() {
        return String.valueOf(length()) + new String(bytes);
    }

    /**
     * Returns the length of the WavString
     * 
     * @return
     */
    public int length() {
        return (int) length;
    }

    /**
     * Returns the value of this string encoded into Bytes for inclusion in a WavFile
     * 
     * @return
     */
    public byte[] getBytes() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length + 1);
        byteBuffer.put(length);
        byteBuffer.put(bytes);
        return byteBuffer.array();
    }

    /**
     * Removes any padding at the end of a byte[]
     * 
     * @param input
     * @return
     */
    private static byte[] removeTrailingPadding(byte[] input) {
        if (input.length == 0) {
            return input;
        }

        int lastNonZeroIndex = -1;
        for (int i = 0; i < input.length; i++) {
            if (input[i] != 0) {
                lastNonZeroIndex = i;
            }
        }

        return Arrays.copyOf(input, lastNonZeroIndex + 1);
    }
}
