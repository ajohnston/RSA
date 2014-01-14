package rsa;

import java.security.SecureRandom;
import java.math.BigInteger;

/**
 * Objects constructed from <code>RSAKeyGenerator</code> class create RSA keys by securely generating large 
 * pseudorandom prime numbers.
 * All prime numbers are tested against Fermat's Theorem to get better certainty that the number is in fact prime.
 * FIPS183-3 was implemented as closely as possible to ensure secure and valid results.
 * @version 0.01
 * @author Andrew H. Johnston
 */
public class RSAKeyGenerator {
  final int FERMAT_MAX_ITERATIONS = 41; //Determined to be adequate for most secure standards according to FIPS 
  final SecureRandom randomNumberGenerator = new SecureRandom();
  BigInteger keys[] = new BigInteger[3];
  BigInteger p;
  BigInteger q;
  //Generate keys using primes generated with pseudorandom numbers
    /**
     *Basic constructor, does nothing
     */
    public RSAKeyGenerator() {
    //Default Constructor, no set up needed
  }
    /**
     *
     * @param bitlength Specifies the bitlength of the keys
     * @return keys[] Where key[0] is n, key[1] is e, and key[2] is d
     */
    public BigInteger[] getKeys(int bitlength) {
    boolean pNotPrime = true;
    boolean qNotPrime = true;
    p = new BigInteger(bitlength, randomNumberGenerator);
    q = new BigInteger(bitlength, randomNumberGenerator);
    //Generate p and q, the two primes
    while (pNotPrime) {
      p = BigInteger.probablePrime(bitlength, randomNumberGenerator);
      pNotPrime = checkNotPrime(p, FERMAT_MAX_ITERATIONS);
    }
    
    while (qNotPrime) {
      q = BigInteger.probablePrime(bitlength, randomNumberGenerator);
      qNotPrime = checkNotPrime(q, FERMAT_MAX_ITERATIONS);
    }
    //Calculate d, n, and phi(r) values
    BigInteger n = p.multiply(q);
    BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
    BigInteger e = BigInteger.valueOf(65537); //Low Hamming Weight and Coprime to phi
    BigInteger d = e.modInverse(phi);
    keys[0] = n;
    keys[1] = e;
    keys[2] = d;
    return keys;   
  }

  /*
   * Checks if a number is NOT prime using Fermat's Theorum. 
   * @param n             BigInteger to be tested for primality
   * @param maxIterations the maximum number of iterations to test
   * @return false        if the number is prime, else true
   */ 
  private boolean checkNotPrime(BigInteger n, int maxIterations) {
    if (n.equals(BigInteger.ONE)) {
            return true;
    }
        for (int i = 0; i < maxIterations; i++)
        {
            BigInteger a = getRandomFermatBase(n);
            a = a.modPow(n.subtract(BigInteger.ONE), n);

            if (!a.equals(BigInteger.ONE)) {
                return true;
            }
        }

        return false;  
  }
    /**
     * Uses rejection method to select a Fermat base that satisfies &lt;= a &lt; n
     * @param   n BigInteger maximum for base a
     * @return a- A Fermat base that satisfies 1 &lt;= a &lt; n
     */ 
    private BigInteger getRandomFermatBase(BigInteger n) {
      while (true) {
            final BigInteger a = new BigInteger (n.bitLength(), randomNumberGenerator);
            // must have 1 <= a < n
            if (BigInteger.ONE.compareTo(a) <= 0 && a.compareTo(n) < 0)
            {
                return a;
            }
        }
  }
    
    public BigInteger[] getKeys() {
        return keys;
    }
    
    public BigInteger[] getKeySeeds() {
        BigInteger[] primes = new BigInteger[2];
        primes[0] = p;
        primes[1] = q;
        return primes;
    }
    
    public void setPValue(BigInteger _p) {
        p = _p;
    }
    
    public void setQValue(BigInteger _q) {
        q = _q;
    }
  
}
