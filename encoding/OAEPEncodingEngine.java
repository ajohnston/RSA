/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encoding;

import java.nio.ByteBuffer;

/**
 *
 * @author Andrew
 */
public class OAEPEncodingEngine {
    
    public String I2OSP(Integer octet) {
        byte[] octetArray = ByteBuffer.allocate(4).putInt(octet).array();
        String octetString = octetArray.toString();
        return octetString;
    }
    
    public Integer OS2IP(String octetString) {
        int octet = Integer.parseInt(octetString);
        return octet;
    }
}
