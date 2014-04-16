package org.atodd.jwaves.wav;

import java.util.Arrays;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

public class WavStringTest {

    @Test
    public void singleChar() throws Exception {
        String testString = RandomStringUtils.randomAscii(1);
        WavString wavString = WavString.from(testString);
        assertWavStringMatchesString(wavString, testString);
    }

    @Test
    public void maxChars() throws Exception {
        String testString = RandomStringUtils.randomAscii(WavString.MAX_CHARS);
        WavString wavString = WavString.from(testString);
        assertWavStringMatchesString(wavString, testString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void greaterThanMaxChars() throws Exception {
        String testString = RandomStringUtils.randomAscii(WavString.MAX_CHARS + 1);
        WavString.from(testString);
    }

    @Test
    public void charArray() throws Exception {
        String testString = RandomStringUtils.randomAscii(WavString.MAX_CHARS);
        WavString wavString = WavString.from(testString.toCharArray());
        assertWavStringMatchesString(wavString, testString);
    }

    @Test
    public void byteArray() throws Exception {
        String testString = RandomStringUtils.randomAscii(WavString.MAX_CHARS);
        WavString wavString = WavString.from(testString.getBytes());
        assertWavStringMatchesString(wavString, testString);
    }

    @Test
    public void emptyString() throws Exception {
        String testString = "";
        WavString wavString = WavString.from(testString);
        assertWavStringMatchesString(wavString, testString);
    }

    @Test
    public void emptyBytes() throws Exception {
        String testString = "";
        WavString wavString = WavString.from(new byte[] {});
        assertWavStringMatchesString(wavString, testString);
    }

    @Test
    public void emptyChars() throws Exception {
        String testString = "";
        WavString wavString = WavString.from(new char[] {});
        assertWavStringMatchesString(wavString, testString);
    }

    @Test
    public void maxEmptyBytes() throws Exception {
        String testString = "";
        WavString wavString = WavString.from(new byte[WavString.MAX_CHARS]);
        assertWavStringMatchesString(wavString, testString);
    }

    /**
     * Checks that the WavString represents the string provided
     * 
     * @param wavString
     * @param expected
     */
    public static void assertWavStringMatchesString(WavString wavString, String expected) {
        assertWavStringSizeBit(wavString, expected.length());
        byte[] waveStringBytes = wavString.getBytes();
        Assert.assertEquals("Wrong number of bytes", expected.length() + 1, waveStringBytes.length);
        byte[] wavStringValueBytes = Arrays.copyOfRange(waveStringBytes, 1, waveStringBytes.length);
        String wavStringValue = new String(wavStringValueBytes);
        Assert.assertEquals("WavString value does not match the expected String", expected,
                            wavStringValue);
        Assert.assertEquals("toString value is not formatted correctly",
                            String.valueOf(expected.length()) + expected, wavString.toString());
    }

    /**
     * Checks that the size bit matches the expected size provided
     * 
     * @param wavString
     * @param expected
     */
    public static void assertWavStringSizeBit(WavString wavString, int expected) {
        Assert.assertEquals("Length does not match the expected size", expected,
                            (int) wavString.length());
        Assert.assertEquals("Size does not match the expected size", expected,
                            wavString.getBytes()[0]);
    }
}
