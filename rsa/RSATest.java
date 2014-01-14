/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rsa;
import java.math.BigInteger;
/**
 *
 * @author Andrew
 */
public class RSATest {
   
  public static void main(String[] args) { 
    RSAWrapper keygen = new RSAWrapper();
    BigInteger[] keys = new BigInteger[3];
    keys = keygen.getKeys();
    String message = "He";
    BigInteger[] m = RSATest.messageToBigInt(message);
    BigInteger[] encrypted = RSATest.encrypt(m, keys);
    String em = "";
    for(BigInteger i : encrypted) {
        em += i.toString() + "\n";
    }
    System.out.println("Encrypted:\n" + em);
    String[] dec = em.split("\n");
    BigInteger[] decstring = new BigInteger[dec.length];
    for (int i = 0; i < dec.length; i++) {
        decstring[i] = new BigInteger(dec[i]);
    }
    BigInteger[] decrypted = RSATest.decrypt(decstring, keys);
    String answer = RSATest.bigIntToString(decrypted);
    System.out.println("Original Message:" + message);
    System.out.println("Decrypted Message:" + answer);
  }
  
 public static String bigIntToString(BigInteger[] M) {
    char [] messageCharArray= new char[M.length];
    int [] messageDigitsArray=new int[M.length];
    for(int i=0;i<M.length;i++){
        messageDigitsArray[i]=M[i].intValue();
        messageCharArray[i]= (char) messageDigitsArray[i];
    }
    String s= new String(messageCharArray);
    return s;
  }
 public static BigInteger[] messageToBigInt(String message) {
     char [] messageCharArray=null;
     messageCharArray = message.toCharArray();
     int [] messageDigitsArray= new int[message.length()];
     BigInteger[] M= new BigInteger[messageCharArray.length];
     for(int i=0; i<messageCharArray.length; i++){
        messageDigitsArray [i] = (int) messageCharArray[i];
        M[i]=BigInteger.valueOf(messageDigitsArray [i]);
     }
     return M;
  }
 
 public static BigInteger[] decrypt(BigInteger[] c, BigInteger[] keys) {
    BigInteger[] m = new BigInteger[c.length];
    BigInteger d = keys[2];
    BigInteger n = keys[0];
    for (int i = 0; i < c.length; i++) {
      m[i] = c[i].modPow(d, n);
    }
    return m;
  }
  
 public static BigInteger[] encrypt(BigInteger[] message, BigInteger[] keys) {
    BigInteger[] c = new BigInteger[message.length];
    BigInteger n = keys[0];
    BigInteger e = keys[1];
    for (int i=0; i < c.length; i++){
      c[i]=message[i].modPow(e,n);
    }
    return c;
  }
}
