package org.a2union;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.lang.StringUtils;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends AbstractDependencyInjectionSpringContextTests {
    private byte[] salt = new byte[]{
            (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
            (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99
    };
    private int count = 1;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    public static Map<Integer, Integer> map = new HashMap<Integer, Integer>(72);

    static {
        map.put(1, 32);
        map.put(2, 8);
        map.put(3, 8);
        map.put(4, 2);
        map.put(5, 1);
        map.put(6, 1);
        map.put(7, 1);
        map.put(8, 1);
        map.put(9, 1);
        map.put(10, 1);
        map.put(11, 1);
        map.put(12, 2);
        map.put(13, 1);
        map.put(14, 4);
        map.put(15, 48);
        map.put(16, 32);
        map.put(17, 32);
        map.put(18, 32);
        map.put(19, 2);
        map.put(20, 1);
        map.put(21, 1);
        map.put(22, 1);
        map.put(23, 3);
        map.put(24, 4);
        map.put(25, 4);
        map.put(26, 4);
        map.put(27, 4);
        map.put(28, 8);
        map.put(29, 80);
        map.put(30, 8);
        map.put(31, 80);
        map.put(32, 8);
        map.put(33, 80);
        map.put(34, 8);
        map.put(35, 80);
        map.put(36, 96);
        map.put(37, 16);
        map.put(38, 32);
        map.put(39, 32);
        map.put(40, 8);
        map.put(41, 8);
        map.put(42, 16);
        map.put(43, 16);
        map.put(44, 16);
        map.put(45, 16);
        map.put(46, 8);
        map.put(47, 8);
        map.put(48, 16);
        map.put(49, 16);
        map.put(50, 8);
        map.put(51, 8);
        map.put(52, 4);
        map.put(53, 4);
        map.put(54, 8);
        map.put(55, 16);
        map.put(56, 16);
        map.put(57, 32);
        map.put(58, 32);
        map.put(59, 32);
        map.put(60, 8);
        map.put(61, 4);
        map.put(62, 3);
        map.put(63, 9);
        map.put(64, 8);
        map.put(65, 8);
        map.put(66, 8);
        map.put(67, 8);
        map.put(68, 32);
        map.put(69, 32);
        map.put(70, 6);
        map.put(71, 3);
        map.put(72, 78);
    }

    /**
     * Rigourous Test :-)
     *
     * @throws Exception something wrong
     */
    public void testApp() throws Exception {
        assertTrue(true);
//        KeyPairGenerator generator = KeyPairGenerator.getInstance("AES");
//        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
//        random.setSeed(10000);
//        generator.initialize(1024, random);
//        KeyPair pair = generator.generateKeyPair();
//
//        byte[] data = new byte[154];
//        FileInputStream inputStream = new FileInputStream("C:\\temp\\for_send\\cdr\\cc\\20090324031.BIL");
//        if (inputStream.read(data) != data.length)
//            throw new IllegalStateException("Data length must be 154 byte");
//        if (inputStream.read(data) != data.length)
//            throw new IllegalStateException("Data length must be 154 byte");
//
//        int i = 0;
//        StringBuffer buffer = new StringBuffer();
//        try {
//            for (byte b : data) {
//                buffer.append(byteToBitString(b));
//            }
//        } catch (IllegalStateException e) {
//        }
//
//        //System.out.println(buffer.toString());
//        int position = 0;
//        for (int j = 1; j <= 72; j++) {
//            Integer integer = map.get(j);
//            String val = StringUtils.substring(buffer.toString(), position, position + integer);
//            BigInteger bigInteger = new BigInteger(StringUtils.reverse(val), 2);
//            System.out.println("j=" + j + " : " + StringUtils.reverse(val) + " : " +bigInteger);
//            position += integer;
//
//        }
//
//
//        inputStream.close();

    }

    

    private Long bitStringToNumber(String str) {
        assertTrue(str.length() % 8 == 0);
        long res = 0;
        for (int i = 0; i < str.length(); i++) {
            res += Integer.parseInt(StringUtils.substring(str, i, i + 1)) * (1 << i);
        }
        return res;
    }

    private void crypt() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        SecretKey aes = generator.generateKey();
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, aes);
        byte[] sf = cipher.doFinal("secret".getBytes());

        cipher.init(Cipher.DECRYPT_MODE, aes);

        byte[] cf = cipher.doFinal(sf);
        System.out.println(new String(cf));


        PBEKeySpec pbeKeySpec;
        PBEParameterSpec pbeParamSpec;
        SecretKeyFactory keyFac;
        // Create PBE parameter set
        pbeParamSpec = new PBEParameterSpec(salt, count);
        //setup date
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.YEAR, 2008);
        instance.set(Calendar.MONTH, 12);
        instance.set(Calendar.DATE, 31);
        instance.set(Calendar.HOUR, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        //using date as a password
        pbeKeySpec = new PBEKeySpec(String.valueOf(instance.getTimeInMillis()).toCharArray());
        keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
        // Create PBE Cipher
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        // Initialize PBE Cipher with key and parameters
        pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
        // Our cleartext
        byte[] cleartext = "Test text".getBytes();
        // Encrypt the cleartext
        byte[] ciphertext = pbeCipher.doFinal(cleartext);

        decrypt(ciphertext);
    }

    private void decrypt(byte[] ciphertext) throws Exception {
        PBEKeySpec pbeKeySpec;
        PBEParameterSpec pbeParameterSpec;
        SecretKeyFactory keyFac;
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.YEAR, 2008);
        instance.set(Calendar.MONTH, 12);
        instance.set(Calendar.DATE, 31);
        instance.set(Calendar.HOUR, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);

        // using same salt, count and date for decrypting
        pbeParameterSpec = new PBEParameterSpec(salt, count);
        pbeKeySpec = new PBEKeySpec(String.valueOf(instance.getTimeInMillis()).toCharArray());
        keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

        pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParameterSpec);
        byte[] cleartext = pbeCipher.doFinal(ciphertext);
        System.out.println(new String(cleartext, "UTF-8"));
    }

//    protected String[] getConfigLocations() {
//        return new String[] {"classpath:applicationContext.xml"};
//    }
}
