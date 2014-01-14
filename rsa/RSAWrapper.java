package rsa;
import java.math.BigInteger;

/**
 * <code>RSAWrapper</code> implements <code>RSAKeyGenerator</code> and 
 * provides encryption and decryption services
 * 
 * <br /><strong>TODO:</strong>Implement padding scheme and encrypt/decrypt functions
 * @version 0.01
 * @author Andrew H. Johnston
 */
public class RSAWrapper {
    
    BigInteger keys[] = new BigInteger[3];
    RSAKeyGenerator keygen;
    
    public RSAWrapper() {
        keygen = new RSAKeyGenerator();
        keys = keygen.getKeys(1024);
        
    }
    
    public RSAWrapper(BigInteger n, BigInteger e, BigInteger d) {
        this.setKeys(n, e, d);
    }
    
    public BigInteger[] getKeys() {
        return keys;
    }
    
    public void setKeys(BigInteger n, BigInteger e, BigInteger d) {
        keys[0] = n;
        keys[1] = e;
        keys[2] = d;
    }

    public BigInteger[] encrypt(String m) {
        BigInteger[] message = this.messageToBigInt(m);
        BigInteger[] c = new BigInteger[message.length];
        BigInteger n = keys[0];
        BigInteger e = keys[1];
        for (int i=0; i < c.length; i++){
          c[i]=message[i].modPow(e,n);
        }
        return c;    
    }
    
    public String decrypt (BigInteger[] c) {
        BigInteger[] m = new BigInteger[c.length];
        BigInteger d = keys[2];
        BigInteger n = keys[0];
        for (int i = 0; i < c.length; i++) {
          m[i] = c[i].modPow(d, n);
        }
        return this.bigIntToString(m);        
    }
    
    private BigInteger[] messageToBigInt(String message) {
        char [] messageCharArray;
        messageCharArray = message.toCharArray();
        int [] messageDigitsArray= new int[message.length()];
        BigInteger[] M= new BigInteger[messageCharArray.length];
        for(int i=0; i<messageCharArray.length; i++){
           messageDigitsArray [i] = (int) messageCharArray[i];
           M[i]=BigInteger.valueOf(messageDigitsArray [i]);
        }
        return M;        
    }
    
    private String bigIntToString(BigInteger[] M) {
        char [] messageCharArray= new char[M.length];
        int [] messageDigitsArray=new int[M.length];
        for(int i=0;i<M.length;i++){
            messageDigitsArray[i]=M[i].intValue();
            messageCharArray[i]= (char) messageDigitsArray[i];
        }
        String s= new String(messageCharArray);
        return s;        
    }
    
    public BigInteger[] getKeySeeds() {
        return keygen.getKeySeeds();
    }
    
    private BigInteger getDValue(BigInteger prime) {
        BigInteger dValue = keys[1].modInverse(prime.subtract(BigInteger.ONE));
        return dValue;
    }
    
    public BigInteger getDPValue() {
        BigInteger p = (this.getKeySeeds())[0];
        return this.getDValue(p);
    }
    
    public BigInteger getDQValue() {
        BigInteger q = (this.getKeySeeds())[1];
        return this.getDValue(q);
    }
    
    public BigInteger getInverseQ() {
        BigInteger p = (this.getKeySeeds())[0];
        BigInteger q = (this.getKeySeeds())[1];
        return q.modInverse(p);
        
    }
    
    public void setPValue(BigInteger _p) {
        keygen.setPValue(_p);
    }
    
    public void setQValue(BigInteger _q) {
        keygen.setQValue(_q);
    }
}
