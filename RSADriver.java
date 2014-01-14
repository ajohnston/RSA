/**
 * Class <code>RSADriver</code> unit tests the <code>RSAKeyGenerator</code> class
 * by generating a public and a private key. 
 * @author Andrew H. Johnston
 * @version 0.01
 */
import rsa.RSAKeyGenerator;
import java.math.BigInteger;

public class RSADriver {
    
  public static void main(String[] args) { 
    //Generate and store the primes
    System.out.println("Generating keys....");
    RSAKeyGenerator keygen = new RSAKeyGenerator();
    BigInteger[] keys = new BigInteger[3]; 
    keys = keygen.getKeys(1024); //Should generate 128-bit 
    System.out.println("Keys Generated!");
    System.out.println("Public Key (n,e): (" + keys[0].toString() + "," + keys[1].toString() + ")");
    System.out.println("Private Key (n,d): (" + keys[0].toString() + "," + keys[2].toString() + ")");
    System.out.print("Length of n is ");
    System.out.println(keys[0].toString().length());
  }
  
  
}
