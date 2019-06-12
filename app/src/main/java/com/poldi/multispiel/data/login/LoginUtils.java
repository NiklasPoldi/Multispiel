/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.paulklubert.ms.data.login;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 * @author Paul
 */
public class LoginUtils {
    
    /**
     * Generiert den SHA-512 Hash aus dem übergebenen Passwort
     * @param password
     * @return
     */
    protected static byte[] getHash(String password) {
        MessageDigest digest=null;
        try {
            digest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        digest.reset();
        return digest.digest(password.getBytes());
    }
    
    /**
     * Wandelt Binärdaten in einen String um
     * @param data
     * @return
     */
    protected static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
    }
    
    
    protected static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    protected static SecureRandom rnd = new SecureRandom();

    protected static String randomString( int len ){
        
       StringBuilder sb = new StringBuilder( len );
       
       for( int i = 0; i < len; i++ ) 
          sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
       
       return sb.toString();
    }
    
}
