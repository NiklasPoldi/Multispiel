package com.poldi.multispiel.data.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class LoginHandler {
	
    private static boolean loginCompleted = false;
    private static String nusername = "";
    private static String gamename = "";
    private static int userId = 0;
    
    public static boolean registerUser(String username, String gamename, String password, String email) {
        try {
            // Password
            String passwordSalt = LoginUtils.randomString(32);
            String totalPassword = password + passwordSalt;
            String hashedPassword  = LoginUtils.bin2hex(  LoginUtils.getHash( totalPassword )  );
            
            String queryURL = "https://connect.pm-mlg.de/A8eCPA5nPV/query.php?query=registerUser";
            queryURL += "&key=" +  URLEncoder.encode(hashedPassword, "UTF-8");
            queryURL += "&salt=" +  URLEncoder.encode(passwordSalt, "UTF-8");
            queryURL += "&username=" +  URLEncoder.encode(username, "UTF-8");
            queryURL += "&gamename=" +  URLEncoder.encode(gamename, "UTF-8");
            queryURL += "&email=" +  URLEncoder.encode(email, "UTF-8");
            
            // Caching vermeiden
            Random rand = new Random();
            queryURL += "&auth="+rand.nextInt(Integer.MAX_VALUE);
            
            
            return false;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LoginHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public static boolean checkUsernameExists(String username) {
        
        try {
            String queryURL = "https://connect.pm-mlg.de/A8eCPA5nPV/query.php?query=usernameExists";
            queryURL += "&username=" +  URLEncoder.encode(username, "UTF-8");
            // Caching vermeiden
            Random rand = new Random();
            queryURL += "&auth="+rand.nextInt(Integer.MAX_VALUE);
            
            // Abfrage vorbereiten
            URL url = new URL(queryURL);
            
            // Inhalt lesen
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            url.openStream()
                    )
            );
            
            // Bit-Stream in String umwandeln
            String inputLine;
            String totalMessage = "";
            
            while ((inputLine = in.readLine()) != null) {
                totalMessage+=inputLine;
            }
            in.close();
            
            if(totalMessage.equals("1")) {
                return true;
            } else {
                return false;
            }
                    
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LoginHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(LoginHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LoginHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
        
    }
    
    /**
     * Prüft Passwort und Nutzername und gibt im Erfolgsfall true zurück
     * @param username Nutzername
     * @param password Passwort in Klartext
     * @return true wenn Nutzername zum Passwort passt
     * @throws IllegalArgumentException wenn Nutzername oder Passwort falsch ist
     */
    public static boolean loginUser(String username, String password) throws IllegalArgumentException {
        try {
            String salt = catchSalt(username);
            String totalPassword = password + salt;
            
            // Passwort als SHA512 Hash umwandeln
            String hashedPassword  = LoginUtils.bin2hex(  LoginUtils.getHash( totalPassword )  );
            
            // URL Aufbauen
            String queryURL = "https://connect.pm-mlg.de/A8eCPA5nPV/login.php?query=login";
            queryURL += "&name=" +  URLEncoder.encode(username, "UTF-8");
            queryURL += "&key="  +  URLEncoder.encode(hashedPassword, "UTF-8");
            
            // Caching vermeiden
            Random rand = new Random();
            queryURL += "&auth="+rand.nextInt(Integer.MAX_VALUE);
            
            // Abfrage vorbereiten
            URL url = new URL(queryURL);
            
            // Inhalt lesen
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            url.openStream()
                    )
            );
            
            // Bit-Stream in String umwandeln
            String inputLine;
            String totalMessage = "";
            
            while ((inputLine = in.readLine()) != null) {
                totalMessage+=inputLine;
            }
            in.close();
            
            if(totalMessage.contains("<1001>")) {
                throw new IllegalArgumentException("Nutzername unbekannt oder inaktiv!");
            }
            if(totalMessage.contains("<1002>")) {
                throw new IllegalArgumentException("Passwort falsch");
            }
            if(totalMessage.contains("<1003>")) {
                throw new IllegalArgumentException("Anfrage falsch");
            }
            if(totalMessage.contains("<1004>")) {
                throw new IllegalArgumentException("Maximale Anzahl an falschen Anmeldungen erreicht");
            }
            
            // Lade Spielstand
            DocumentBuilderFactory dbFactory1 = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder1 = dbFactory1.newDocumentBuilder();
            Document doc1 = dBuilder1.parse( new InputSource( new StringReader( totalMessage ) ) );
            doc1.getDocumentElement().normalize();
            
            // Liste aller "dataset"-Nodes finden
            NodeList nList = doc1.getElementsByTagName("dataset");
            // Ersten Eintrag auswählen, wir sollten nur einen Spielstand bekommen...!
            Node nNode = nList.item(0);
            final Element eElement = (Element) nNode;
            
            userId = Integer.parseInt( eElement.getElementsByTagName("id").item(0).getTextContent() );
            nusername = eElement.getElementsByTagName("username").item(0).getTextContent();
            gamename = eElement.getElementsByTagName("gamename").item(0).getTextContent();
            
            loginCompleted = true;
            return true;
        } catch (MalformedURLException ex) {
            Logger.getLogger(LoginHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LoginHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(LoginHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(LoginHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    /**
     * Gibt nach erfolgreichem Login (loginUser() == true) die zugehörige Nutzerid zum Nutzernamen zurück
     * @return
     */
    public static int getUserID() {
        if(loginCompleted) {
            return userId;
        } else {
            throw new IllegalStateException("Erst anmelden, dann ID holen");
        }
    }
    
    /**
     * Gibt nach erfolgreichem Login (loginUser() == true) die zugehörige Nutzernamen zurück
     * @return
     */
    public static String getUsername() {
        if(loginCompleted) {
            return nusername;
        } else {
            throw new IllegalStateException("Erst anmelden, dann ID holen");
        }
    }
    
    /**
     * Gibt nach erfolgreichem Login (loginUser() == true) die zugehörige In-Game-Namen zurück zurück
     * @return
     */
    public static String getGamename() {
        if(loginCompleted) {
            return gamename;
        } else {
            throw new IllegalStateException("Erst anmelden, dann ID holen");
        }
    }
    
    public static boolean isUserIDAvailable()  {
        return loginCompleted;
    }
	
    private static String catchSalt(String username) throws UnsupportedEncodingException, MalformedURLException, IOException {
	// URL Aufbauen
        String queryURL = "https://connect.pm-mlg.de/A8eCPA5nPV/login.php?query=salt&name=";
        queryURL += URLEncoder.encode(username, "UTF-8");
                
        // Caching vermeiden
        Random rand = new Random();
        queryURL += "&auth="+rand.nextInt(Integer.MAX_VALUE);
                
        // Abfrage vorbereiten
	URL url = new URL(queryURL);
        
        // Inhalt lesen
	BufferedReader in = new BufferedReader(
            new InputStreamReader(
                url.openStream()
            )
	);

        // Bit-Stream in String umwandeln
	String inputLine;
        String totalMessage = "";

        while ((inputLine = in.readLine()) != null) {
            totalMessage+=inputLine;
	}
        in.close();
        
        if(totalMessage.contains("<1001>")) {
            throw new IllegalArgumentException("Nutzername unbekannt oder inaktiv!");
        }
        if(totalMessage.contains("<1002>")) {
            throw new IllegalArgumentException("Passwort falsch");
        }
        if(totalMessage.contains("<1003>")) {
            throw new IllegalArgumentException("Anfrage falsch");
        }
        if(totalMessage.contains("<1004>")) {
            throw new IllegalArgumentException("Maximale Anzahl an falschen Anmeldungen erreicht");
        }
        
        return totalMessage;
    }
	
}