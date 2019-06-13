/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poldi.multispiel.gamew.lang;

import java.util.Map;

/**
 *
 * @author Paul
 */
public class console {

    public static void callMethod(String methodCall, Map LocalVars, Map GlobalVars) {

        if(methodCall.startsWith("log(")) {
            // Catch Parameter 1
            String statement = methodCall.substring( methodCall.indexOf("(")+1, methodCall.indexOf(")") );

            // Do the Java-Equivalent to method console.log(String);
            if(statement.contains("\"")) {
                System.err.println(statement.replaceAll("\"", ""));
            } else {
                if(GlobalVars.get(statement) != null) {
                    System.err.println(GlobalVars.get(statement));
                } else if(LocalVars.get(statement) != null) {
                    System.err.println(LocalVars.get(statement));
                }
            }

        }

    }

}
