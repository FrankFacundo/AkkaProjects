package demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger; 
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 

public class SHA1 {
	public SHA1() {
		
	}
	
    public Integer encryptString(String input) 
    { 
        try { 
            // getInstance() method is called with algorithm SHA-1 
            MessageDigest md = MessageDigest.getInstance("SHA-1"); 
  
            // digest() method is called 
            // to calculate message digest of the input string 
            // returned as array of byte 
            byte[] messageDigest = md.digest(input.getBytes()); 
  
            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest); 
            Integer modulo = (no.mod(BigInteger.valueOf(16))).intValue();
  
            // return the HashText 
            return modulo; 
        }
  
        // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 
        } 
    } 
    
    public Integer encryptFile(File file) throws Exception  {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        InputStream fis = new FileInputStream(file);
        int n = 0;
        byte[] buffer = new byte[8192];
        while (n != -1) {
            n = fis.read(buffer);
            if (n > 0) {
                digest.update(buffer, 0, n);
            }
        }
        
        byte[] messageDigest = digest.digest();
        
        BigInteger no = new BigInteger(1, messageDigest); 
        Integer modulo = (no.mod(BigInteger.valueOf(16))).intValue();

        // return the HashText 
        return modulo;
    }
}
