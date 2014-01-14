/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filehelper;


import java.io.File;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.math.BigInteger;
import nu.xom.Document;


/**
 *
 * @author Andrew
 */
public class FileHelper {
    
    public static void writeEncryptedFile(BigInteger[] data, String fileName) {
        File saveFile = new File(fileName);
            if (!saveFile.exists()) {
                try {
                    saveFile.createNewFile();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
            try {
                FileWriter fw = new FileWriter(saveFile);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw);
                for( BigInteger value : data) {
                    out.println(value.toString());
                }
                out.close();
            } catch (IOException ex) {
                System.out.println(ex);
            }
    }
    
    public static void writeDocumentToFile(Document doc, File file) {
        File saveFile = file;
        if (!saveFile.exists()) {
            try {
                saveFile.createNewFile();
            }
            catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
            PrintWriter print = new PrintWriter(bw);
            print.print(doc.toXML());
            print.flush(); //Have to flush, not done automatically
            print.close();
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static String readFile(File fileName) {
        Scanner inFile = null;
        String message = "";
            try {
                inFile = new Scanner(new FileReader(fileName));
                while(inFile.hasNext()) {
                    message += inFile.nextLine();
                }
                inFile.close();
            } 
            catch (FileNotFoundException ex) { 
                System.out.println(ex);
            }
        return message;
    }
    
    public static BigInteger[] readFileToArray(File fileName) {
        BigInteger[] decrypted;
        Scanner inFile = null;
        String message = "";
            try {
                inFile = new Scanner(new FileReader(fileName));
                while(inFile.hasNext()) {
                    message += inFile.nextLine();
                }
                inFile.close();
            } 
            catch (FileNotFoundException ex) { 
                System.out.println(ex);
            }
        String newLine = System.getProperty("line.separator");
        String[] integers = newLine.split(newLine);
        decrypted = new BigInteger[integers.length];
        for (int i = 0; i < decrypted.length; i++) {
            decrypted[i] = new BigInteger(integers[i]);
        }
        return decrypted;
    }
    
}
