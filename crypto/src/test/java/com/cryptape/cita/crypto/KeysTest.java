package com.cryptape.cita.crypto;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import org.junit.Test;

import com.cryptape.cita.utils.Numeric;
import com.cryptape.cita.utils.Strings;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class KeysTest {

    private static final byte[] ENCODED;

    static {
        byte[] privateKey = Numeric.hexStringToByteArray(SampleKeys.PRIVATE_KEY_STRING);
        byte[] publicKey = Numeric.hexStringToByteArray(SampleKeys.PUBLIC_KEY_STRING);
        ENCODED = Arrays.copyOf(privateKey, privateKey.length + publicKey.length);
        System.arraycopy(publicKey, 0, ENCODED, privateKey.length, publicKey.length);
    }

    @Test
    public void testCreateSecp256k1KeyPair() throws Exception {
        KeyPair keyPair = Keys.createSecp256k1KeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        assertNotNull(privateKey);
        assertNotNull(publicKey);

        assertThat(privateKey.getEncoded().length, is(144));
        assertThat(publicKey.getEncoded().length, is(88));
    }

    @Test
    public void testCreateEcKeyPair() throws Exception {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        assertThat(ecKeyPair.getPublicKey().signum(), is(1));
        assertThat(ecKeyPair.getPrivateKey().signum(), is(1));
    }

    @Test
    public void testGetAddressString() {
        assertThat(Keys.getAddress(SampleKeys.PUBLIC_KEY_STRING),
                is(SampleKeys.ADDRESS_NO_PREFIX));
    }

    @Test
    public void testGetAddressZeroPaddedAddress() {
        String publicKey =
                "0xa1b31be4d58a7ddd24b135db0da56a90fb5382077ae26b250e1dc9cd6232ce22"
                        + "70f4c995428bc76aa78e522316e95d7834d725efc9ca754d043233af6ca90113";
        assertThat(Keys.getAddress(publicKey),
                is("01c52b08330e05d731e38c856c1043288f7d9744"));
    }

    @Test
    public void testGetAddressBigInteger() {
        assertThat(Keys.getAddress(SampleKeys.PUBLIC_KEY),
                is(SampleKeys.ADDRESS_NO_PREFIX));
    }

    @Test
    public void testGetAddressSmallPublicKey() {
        byte[] address = Keys.getAddress(
                Numeric.toBytesPadded(BigInteger.valueOf(0x1234), Keys.PUBLIC_KEY_SIZE));
        String expected = Numeric.toHexStringNoPrefix(address);

        assertThat(Keys.getAddress("0x1234"), equalTo(expected));
    }

    @Test
    public void testGetAddressZeroPadded() {
        byte[] address = Keys.getAddress(
                Numeric.toBytesPadded(BigInteger.valueOf(0x1234), Keys.PUBLIC_KEY_SIZE));
        String expected = Numeric.toHexStringNoPrefix(address);

        String value = "1234";
        assertThat(Keys.getAddress("0x"
                        + Strings.zeros(Keys.PUBLIC_KEY_LENGTH_IN_HEX - value.length()) + value),
                equalTo(expected));
    }

    @Test
    public void testSerializeECKey() {
        assertThat(Keys.serialize(SampleKeys.KEY_PAIR), is(ENCODED));
    }

    @Test
    public void testDeserializeECKey() {
        assertThat(Keys.deserialize(ENCODED), is(SampleKeys.KEY_PAIR));
    }

    @Test(expected = RuntimeException.class)
    public void testDeserializeInvalidKey() {
        Keys.deserialize(new byte[0]);
    }

    @Test
    public void testVerifyAddress() {
        assertTrue(Keys.verifyAddress("0xbac68e5cb986ead0253e0632da1131a0a96efa18"));
        assertTrue(Keys.verifyAddress("0Xbac68e5cb986ead0253e0632da1131a0a96efa18"));
        assertTrue(Keys.verifyAddress("bac68e5cb986ead0253e0632da1131a0a96efa18"));
        assertFalse(Keys.verifyPrivateKey("xbac68e5cb986ead0253e0632da1131a0a96efa18"));
        assertFalse(Keys.verifyPrivateKey("0bac68re5cb986ead0253e0632da1131a0a96efa18"));
        assertFalse(Keys.verifyPrivateKey("gac68e5cb986ead0253e0632da1131a0a96efa18"));
    }

    @Test
    public void testVerifyPrivateKey() {
        assertTrue(Keys.verifyPrivateKey(
                "0x02b8f18d92354b055bd02cd51449b05b7b97104931f10485d1df905e4e70fbca"));
        assertTrue(Keys.verifyPrivateKey(
                "0X02b8f18d92354b055bd02cd51449b05b7b97104931f10485d1df905e4e70fbca"));
        assertTrue(Keys.verifyPrivateKey(
                "02b8f18d92354b055bd02cd51449b05b7b97104931f10485d1df905e4e70fbca"));
        assertFalse(Keys.verifyPrivateKey(
                "x02b8f18d92354b055bd02cd51449b05b7b97104931f10485d1df905e4e70fbca"));
        assertFalse(Keys.verifyPrivateKey(
                "00k2b8f18d92354b055bd02cd51449b05b7b97104931f10485d1df905e4e70fbca"));
        assertFalse(Keys.verifyPrivateKey(
                "h2b8f18d92354b055bd02cd51449b05b7b97104931f10485d1df905e4e70fbca"));
    }
}
