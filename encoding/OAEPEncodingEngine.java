/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encoding;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrew
 */
public class OAEPEncodingEngine {
    
    final static int HASH_LENGTH = 256;
    
    
    public static String I2OSP(int octet, int length) {
        byte[] octetArray = ByteBuffer.allocate(length).putInt(octet).array();
        String octetString = octetArray.toString();
        return octetString;
    }
    
    public static int OS2IP(String octetString) {
        int octet = Integer.parseInt(octetString);
        return octet;
    }
    
    public static String encode(String message, String param, int emLen) {
        SecureRandom rndGenerator = new SecureRandom();
        if (param.getBytes().length > (Math.pow(2,64) - 1)/8) {
            return "Parameter string too long";
        }
        else if (param.getBytes().length > emLen - 2*HASH_LENGTH - 1) {
            return "Message too long";
        }
        String ps    = null; //Placeholder. Should be an octet string of len emLen - mLen - 2hlen - 1
        String pHash = OAEPEncodingEngine.hash(param);
        String DB    = pHash + ps + "01" + message;
        byte[] seedArray = new byte[HASH_LENGTH];
        rndGenerator.nextBytes(seedArray);
        String seed = seedArray.toString();
        String dbMask = OAEPEncodingEngine.maskGeneration(seed, param.getBytes().length - HASH_LENGTH);
        String maskedDB = null; //Placeholder: DB XOR dbMask
        String seedMask = OAEPEncodingEngine.maskGeneration(maskedDB, HASH_LENGTH);
        String maskedSeed = null; //Placeholder: seed XOR seedMask
        String EM = maskedSeed + maskedDB;
        return EM;
        
    }
    
    public static String decode(String message, String param) {
        
    }
    
    private static String maskGeneration(String z, int len) {
        String t = null;
        if (len <= Math.pow(2,32) * HASH_LENGTH) {
            for (int i = 0; i <= (len/HASH_LENGTH) - 1; i++) {
                String c = OAEPEncodingEngine.I2OSP(i, 4);
                t += OAEPEncodingEngine.hash(z + c);
            }
        }
        else {
            return "Mask too long";
        }
        return Arrays.copyOfRange(t.getBytes(),0, len).toString();
    }
    
    private static String hash(String str) {
        MessageDigest messageDigest;
        String encryptedString = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes());
            encryptedString = new String(messageDigest.digest());
        } catch (NoSuchAlgorithmException ex) {
            System.out.print(ex.getMessage());
        }
        return encryptedString;
    }
}
