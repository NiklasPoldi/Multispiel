/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poldi.multispiel.data;

import com.poldi.multispiel.data.login.LoginHandler;
import com.poldi.multispiel.data.wrapper.Freund;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
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
import org.xml.sax.SAXException;

/**
 *
 * @author Paul
 */
public class DataEngine extends Thread {


    protected static boolean VALUES_DECREASING = true;
    private static boolean SAVE = true;

    private static boolean stillLoading = false;

    private static ArrayList<Freund> list_frienshiplist = new ArrayList<>();

    // Listener
    private static ArrayList<Runnable> onLoadingStarted = new ArrayList<>();
    private static ArrayList<Runnable> onLoadingEnded = new ArrayList<>();
    private static ArrayList<Runnable> onNewDataAvailable = new ArrayList<>();
    private boolean loaded = true;
    private static ArrayList<LevelChangeListener> onLevelChange = new ArrayList<>();
    private static ArrayList<ValueChangeListener> onValueChange = new ArrayList<>();

    @Override
    public void run() {
        try {

            // Warten bis Login beendet ist ...
            while(!LoginHandler.isUserIDAvailable()) {
                Thread.sleep(500);
            }


            while(true) {

                run_OnRefreshStarted();

                // Ersten Datensatz catchen, Freundschaftsliste laden...
                //   URL Aufbauen
                String queryURL = "https://pm-mlg.de/connect/A8eCPA5nPV/query.php?query=friendlist";
                queryURL += "&i=" +  URLEncoder.encode( LoginHandler.getUserID() + "" , "UTF-8");

                // Lade Freundschaftsliste
                DocumentBuilderFactory dbFactory1 = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder1 = dbFactory1.newDocumentBuilder();
                Document doc1 = dBuilder1.parse( queryURL );
                doc1.getDocumentElement().normalize();

                // Liste aller "dataset"-Nodes finden
                NodeList nList = doc1.getElementsByTagName("dataset");
                // Ersten Eintrag auswählen, wir sollten nur einen Spielstand bekommen...!
                for(int i = 0; i< nList.getLength(); i++) {
                    Node nNode = nList.item(i);
                    final Element eElement = (Element) nNode;
                    Freund f = new Freund();
                    list_frienshiplist.add(f);

                    f.id = Integer.parseInt( eElement.getElementsByTagName("id").item(0).getTextContent() );
                    f.username = ( eElement.getElementsByTagName("username").item(0).getTextContent() );
                    f.gamename = ( eElement.getElementsByTagName("gamename").item(0).getTextContent() );
                }

                stillLoading = false;

                run_OnRefreshFinished();

                if(loaded) {
                    run_addOnLoadingFinished();
                    loaded = false;
                }


                Thread.sleep(5000);
            }

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DataEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DataEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(DataEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(DataEngine.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    // Listener

    public static void addOnLoadingFinished(Runnable r) {
        onNewDataAvailable.add(r);
    }

    public static void removeOnLoadingFinished(Runnable r) {
        onNewDataAvailable.remove(r);
    }

    private static void run_addOnLoadingFinished() {
        for(Runnable r : onNewDataAvailable) {
            r.run();
        }
    }


    public static void addOnRefreshStarted(Runnable r) {
        onLoadingStarted.add(r);
    }

    public static void removeOnRefreshStarted(Runnable r) {
        onLoadingStarted.remove(r);
    }

    private static void run_OnRefreshStarted() {
        for(Runnable r : onLoadingStarted) {
            r.run();
        }
    }


    public static void addOnRefreshFinished(Runnable r) {
        onLoadingEnded.add(r);
    }

    public static void removeOnRefreshFinished(Runnable r) {
        onLoadingEnded.remove(r);
    }

    private static void run_OnRefreshFinished() {
        for(Runnable r : onLoadingEnded) {
            r.run();
        }
    }


    public static void addOnLevelChanged(LevelChangeListener r) {
        onLevelChange.add(r);
    }

    public static void removeOnLevelChanged(LevelChangeListener r) {
        onLevelChange.remove(r);
    }

    private static void run_onLevelChanged(int old, int ne) {
        LevelChangeDataObject e = new LevelChangeDataObject(old, ne);
        for(LevelChangeListener r : onLevelChange) {
            r.levelChanged(e);
        }
    }


    public static void addOnValueChanged(ValueChangeListener r) {
        onValueChange.add(r);
    }

    public static void removeOnValueChanged(ValueChangeListener r) {
        onValueChange.remove(r);
    }

    private static void run_onValueChanged(int old, int ne, int type) {
        ValueChangeDataObject e = new ValueChangeDataObject(old, ne, type);
        for(ValueChangeListener r : onValueChange) {
            r.valueChanged(e);
        }
    }


    public static boolean isLoading() {
        return stillLoading;
    }

    /**
     * Prüft ob der Angegebene Geldwert auf dem Konto verfügbar ist und zieht ihn im erfolgsfall ab
     * @param value Geldwert der Abgezogen werden soll...
     * @param onAfterExcution DataReturnHandler um den Ausgang der Operation zu empfangen...
     */
    public static void makeTransaction(int value, DataReturnHandler onAfterExcution) {
        try {
            String queryURL = "https://connect.pm-mlg.de/A8eCPA5nPV/query.php?query=coinValue";
            queryURL += "&userid=" +  URLEncoder.encode( LoginHandler.getUserID() + "" , "UTF-8");

            String result = getURLContent(queryURL);

            int coinsLeft = Integer.parseInt(result);


            if(coinsLeft - value >= 0) {
                // Transaktion möglich...
                queryURL = "https://connect.pm-mlg.de/A8eCPA5nPV/query.php?query=makeTransaction";
                queryURL += "&userid=" +  URLEncoder.encode( LoginHandler.getUserID() + "" , "UTF-8");
                queryURL += "&value=" +   URLEncoder.encode( value + "" , "UTF-8");

                result = getURLContent(queryURL);

                if(result.equals("1")) {
                    if(onAfterExcution != null) {
                        DataReturnObject dro = new DataReturnObject(true);
                        onAfterExcution.onDataReturn(dro);
                    }
                } else {
                    if(onAfterExcution != null) {
                        DataReturnObject dro = new DataReturnObject(false);
                        onAfterExcution.onDataReturn(dro);
                    }
                }
            } else {
                // Zu wenig Geld
                if(onAfterExcution != null) {
                    DataReturnObject dro = new DataReturnObject(false, "Betrag auf dem Konto nicht ausreichend!");
                    onAfterExcution.onDataReturn(dro);
                }
            }

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DataEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String getURLContent(String queryURL) throws MalformedURLException, IOException {// Caching vermeiden
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

        return totalMessage;
    }


}

/*
 * LISTENERS
 */

class DataReturnObject {

    private boolean operationSuccessfull = false;
    private final Date time = new Date();
    private String message = "";

    protected DataReturnObject( boolean success ) {
        operationSuccessfull = success;
    }

    protected DataReturnObject( boolean success, String message ) {
        operationSuccessfull = success;
        this.message = message;
    }

    public boolean isOperationSuccessfull() {
        return operationSuccessfull;
    }

    public String getOperationMessage() {
        return message;
    }

    /**
     * @return Gibt den UNIX-Timestamp zurück zu der die Operation beendet wurde
     */
    public long getTimestamp() {
        return time.getTime();
    }
}

abstract class DataReturnHandler {
    public abstract void onDataReturn(DataReturnObject e);
}

/*
 * LEVEL CHANGES
 */
class LevelChangeDataObject {
    private int oldlevel = 0;
    private int newlevel = 0;
    private final Date time = new Date();

    protected LevelChangeDataObject(int oldLevel, int newLevel) {
        this.oldlevel = oldlevel;
        this.newlevel = newlevel;
    }

    public int getOldLevel() {
        return oldlevel;
    }

    public int getNewLevel() {
        return newlevel;
    }

    /**
     * @return Gibt den UNIX-Timestamp zurück zu der die Operation beendet wurde
     */
    public long getTimestamp() {
        return time.getTime();
    }
}
abstract class LevelChangeListener {
    public abstract void levelChanged(LevelChangeDataObject e);
}

/*
 * VALUE CHANGES
 */
class ValueChangeDataObject {
    public static final int VALUE_TYPE_COIN = 0;
    public static final int VALUE_TYPE_WASSER = 1;
    public static final int VALUE_TYPE_O2 = 2;
    public static final int VALUE_TYPE_XP = 3;

    private int oldvalue = 0;
    private int newvalue = 0;
    private int type = 0;
    private final Date time = new Date();

    protected ValueChangeDataObject(int oldvalue, int newvalue, int type) {
        this.oldvalue = oldvalue;
        this.newvalue = newvalue;
        this.type = type;
    }

    public int getOldLevel() {
        return oldvalue;
    }

    public int getNewLevel() {
        return newvalue;
    }

    /**
     * Gibt die Art des Werts gemäß ValueChangeDataObject.VALUE_TYPE zurück
     * @return Wert typ als int
     */
    public int getValueType() {
        return type;
    }

    /**
     * @return Gibt den UNIX-Timestamp zurück zu der die Operation beendet wurde
     */
    public long getTimestamp() {
        return time.getTime();
    }
}
abstract class ValueChangeListener {
    public abstract void valueChanged(ValueChangeDataObject e);
}