/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filehelper;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import nu.xom.*;
import rsa.RSAWrapper;


/**
 *
 * @author Andrew
 */
public class XMLHelper {
    RSAWrapper engine;
    
    public XMLHelper(RSAWrapper r) {
        engine = r;
    }
    
    public void readXMLFile(File file) {
        BigInteger intN = null; //
        BigInteger intE = null; //Declaring null prevents uninitialized error
        BigInteger intD = null; //
        try {
            Builder parser = new Builder();
            Document doc = parser.build(file);
            Element n = doc.getRootElement().getFirstChildElement("Modulus");
            Element e = doc.getRootElement().getFirstChildElement("Exponent");
            Element d = doc.getRootElement().getFirstChildElement("D");
            Element p = doc.getRootElement().getFirstChildElement("P");
            Element q = doc.getRootElement().getFirstChildElement("Q");
            intN = new BigInteger(n.getValue());
            intE = new BigInteger(e.getValue());
            intD = new BigInteger(d.getValue());
            
            //Update the Encryption Engine
            engine.setPValue(new BigInteger(p.getValue()));
            engine.setQValue(new BigInteger(q.getValue())); 
            engine.setKeys(intN, intE, intD);
        }
        catch (ParsingException | IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
    

    public void writeXMLFile(File output) {
        Document doc = this.createDocument(); 
        FileHelper.writeDocumentToFile(doc, output);
    }
    
    private Document createDocument() {
        Element root     = new Element("RSAKeyValue");
        Element modulus  = new Element("Modulus");
        Element exponent = new Element("Exponent");
        Element pVal     = new Element("P");
        Element qVal     = new Element("Q");
        Element dP       = new Element("DP");
        Element dQ       = new Element("DQ");
        Element inverseQ = new Element("InverseQ");
        Element dVal     = new Element("D");
        
        BigInteger[] keys = engine.getKeys();
        
        modulus.appendChild(keys[0].toString());
        exponent.appendChild(keys[1].toString());
        pVal.appendChild((engine.getKeySeeds())[0].toString());
        qVal.appendChild((engine.getKeySeeds())[1].toString());
        dP.appendChild(engine.getDPValue().toString());
        dQ.appendChild(engine.getDQValue().toString());
        inverseQ.appendChild(engine.getInverseQ().toString());
        dVal.appendChild(keys[2].toString());
        
        root.appendChild(modulus);
        root.appendChild(exponent);
        root.appendChild(pVal);
        root.appendChild(qVal);
        root.appendChild(dP);
        root.appendChild(dQ);
        root.appendChild(inverseQ);
        root.appendChild(dVal);
        return new Document(root);       
    }
    

    
    
    
}
