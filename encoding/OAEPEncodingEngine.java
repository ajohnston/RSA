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
    
    public static String encode(String message, String param, int emLen) throws IllegalArgumentException {
        SecureRandom rndGenerator = new SecureRandom();
        if (param.getBytes().length > (Math.pow(2,64) - 1)/8) {
            throw new IllegalArgumentException("Parameter string too long");
        }
        else if (param.getBytes().length > emLen - 2*HASH_LENGTH - 1) {
            throw new IllegalArgumentException("Message too long");
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
   //Change Return type to String 
    public static String decode(String message, String param) throws IllegalArgumentException{
        if (param.length() > (Math.pow(2,64) -1)/8) {
            throw new  IllegalArgumentException("Decoding Error");
        }
        //This code might be wrong. Should be len of msg in bytes
        int emLen = OAEPEncodingEngine.I2OSP(message.length(), 4).length();
        if (emLen < 2*HASH_LENGTH+1) {
            throw new IllegalArgumentException("Decoding Error");
        }
        String maskedSeed = Arrays.copyOfRange(message.toCharArray(), 0, HASH_LENGTH).toString();
        String maskedDB = Arrays.copyOfRange(message.toCharArray(), HASH_LENGTH, message.length()).toString();
        String seedMask = OAEPEncodingEngine.maskGeneration(maskedDB, HASH_LENGTH);
        String seed = null; //Should be maskedSeed XOR seedMask
        String dbMask = OAEPEncodingEngine.maskGeneration(seed, emLen - HASH_LENGTH);
        String DB = null; //Should be maskedDB XOR dbMask
        String pHash = OAEPEncodingEngine.hash(param);
        String pHashPrime = pHash.substring(0, HASH_LENGTH);
        //DB Should match the following regex: pHashPrime(PS)?01(Message)
        //If not, set pHashPrime to zeros to avoid chosen plaintext vuln
        String decodedMessage = null; 
        if (!(pHashPrime.equals(pHash))) {
            throw new IllegalArgumentException("Decoding Error");
        }
        return decodedMessage;
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
