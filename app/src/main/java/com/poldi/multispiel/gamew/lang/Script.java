package com.poldi.multispiel.gamew.lang;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

public class Script {

    private final HashMap<String,String> global_vars = new HashMap<>();

    private final ArrayList<String> code ;

    public Script(ArrayList<String> script) {
        // take the loaded Script as ArrayList
        this.code = script;
    }

    public void run() {

        HashMap<String,String> local_vars = new HashMap<>();

        // iterate through script lines
        for( int i = 0; i < code.size(); i++ ) {

            // interpreting lines here
            String line = code.get( i ).trim() ;
            System.out.print(line+" >");

            // Comment
            if(line.startsWith("//")) {
                // Ignore / Interpret as Comment
                System.out.println("Found Comment on Line "+(i+1));
                continue;
            }

            // Line Break
            if(line.startsWith("/r") || line.startsWith("/n") || line.equals("")) {
                // Just a empty line
                System.out.println("Found Line Break on Line "+(i+1));
                continue;
            }

            // Multi-Line Statements
            /*while(!line.endsWith(";") && !line.startsWith("//")) {
                i++;
                line += code.get(i);
            }*/

            /* * * * * VARIABLES * * * * */

            // LOCAL Variable Declaration
            // Example: var x = 0; or var x=0;
            if(line.startsWith("var")) {
                // Split Line in declaration and value
                // statement[0] should be: "var x"
                // statement[1] should be: "0;"
                String[] statement = line.split("=");

                // Remove excess blanks
                for(int n = 0; n< statement.length; n++) {
                    statement[n] = statement[n].trim();
                }

                // Split on blank (" "), should result in ["var","x"] and take second field to get name
                String variableName = (statement[0].split(" "))[1];
                // Take substring from first index of ";", so "0;651;" to prevent storing shit as value
                //                                              ^ Should stop here
                String value = statement[1].substring( 0, statement[1].indexOf(";") );
                // Store var in local variables field (only available in this run of the script)
                local_vars.put(variableName, value);
                System.out.println("Assign local var '"+variableName+"' = '"+value+"' on Line "+(i+1));
            } else

                // GLOBAL Variable Declaration
                // Example: global x = 0; or global x=0;
                if(line.startsWith("global")) {
                    // Split Line in declaration and value
                    // statement[0] should be: "global x"
                    // statement[1] should be: "0;"
                    String[] statement = line.split("=");

                    // Remove excess blanks
                    for(int n = 0; n< statement.length; n++) {
                        statement[n] = statement[n].trim();
                    }

                    // Split on blank (" "), should result in ["global","x"] and take second field to get name
                    String variableName = (statement[0].split(" "))[1];
                    // Take substring from first index of ";", so "0;651;" to prevent storing shit as value
                    //                                              ^ Should stop here
                    String value = statement[1].substring( 0, statement[1].indexOf(";") );
                    // Store var in global variables field (always available in runtime)
                    global_vars.put(variableName, value);
                    System.out.println("Assign global var '"+variableName+"' = '"+value+"' on Line "+(i+1));
                } else

                    // Line contains exactly one equal -> value assign to var (x = 0;) AND not a statement
                    if( (line.split("=",-1).length-1) == 1 && !line.contains("if") && !line.contains("for") && !line.contains("while") && !line.contains("if")) {

                        // Split Line in declaration and value
                        // statement[0] should be: "x"
                        // statement[1] should be: "0;"
                        String[] statement = line.split("=");

                        // Remove excess blanks
                        for(int n = 0; n< statement.length; n++) {
                            statement[n] = statement[n].trim();
                        }

                        // Split on blank (" "), should result in ["global","x"] and take second field to get name
                        String variableName = statement[0];
                        // Take substring from first index of ";", so "0;651;" to prevent storing shit as value
                        //                                              ^ Should stop here
                        String value = statement[1].substring( 0, statement[1].indexOf(";") );

                        if(!value.contains("+") && !value.contains("-") && !value.contains("/") && !value.contains("*") && !value.contains("%")) {
                            // Check if value is other var
                            if(global_vars.get(value)!=null) {
                                value = global_vars.get(value);
                            } else if(local_vars.get(value)!=null) {
                                value = local_vars.get(value);
                            }

                            // if its a global var, !has priority!
                            if(global_vars.get(variableName) != null) {
                                global_vars.put(variableName, value);  // Assign value
                            } else // Only if not a local var
                                if(local_vars.get(variableName) != null) {
                                    local_vars.put(variableName, value);  // Assign value
                                }

                        } else {
                            // Basic math operations
                            // 8 + 9 + 12 * 5

                        }
                        System.out.println("Assign '"+variableName+"' = '"+value+"' on Line "+(i+1));

                    }

            /* * * * * OPERATIONS * * * * */
            if((line.split("\\+",-1).length-1) == 2) { // Double + ( x++; )
                // Get var name
                String variableName = line.substring(0, line.indexOf("+"));

                if(global_vars.get(variableName) != null) {
                    int previousValue = Integer.parseInt( global_vars.get(variableName) );
                    global_vars.put(variableName, ""+ (previousValue + 1) );  // Assign value
                } else // Only if not a local var
                    if(local_vars.get(variableName) != null) {
                        int previousValue = Integer.parseInt( local_vars.get(variableName) );
                        local_vars.put(variableName, ""+ (previousValue + 1) );  // Assign value
                    }
            }

            if((line.split("\\-",-1).length-1) == 2) { // Double - ( x--; )
                // Get var name
                String variableName = line.substring(0, line.indexOf("-"));

                if(global_vars.get(variableName) != null) {
                    int previousValue = Integer.parseInt( global_vars.get(variableName) );
                    global_vars.put(variableName, ""+ (previousValue - 1) );  // Assign value
                } else // Only if not a local var
                    if(local_vars.get(variableName) != null) {
                        int previousValue = Integer.parseInt( local_vars.get(variableName) );
                        local_vars.put(variableName, ""+ (previousValue - 1) );  // Assign value
                    }
            }

            if((line.split("\\+",-1).length-1) == 1 && (line.split("=",-1).length-1) == 1) { // x+=  1;
                // Get var name
                String variableName = line.substring(0, line.indexOf("+"));
                String value        = line.substring(line.indexOf("=")+1, line.indexOf(";"));
                int val             = Integer.parseInt( value );

                if(global_vars.get(variableName) != null) {
                    int previousValue = Integer.parseInt( global_vars.get(variableName) );
                    global_vars.put(variableName, ""+ (previousValue+val) );  // Assign value
                } else // Only if not a local var
                    if(local_vars.get(variableName) != null) {
                        int previousValue = Integer.parseInt( local_vars.get(variableName) );
                        local_vars.put(variableName, ""+ (previousValue+val) );  // Assign value
                    }
            }

            if((line.split("\\-",-1).length-1) == 1 && (line.split("=",-1).length-1) == 1) { // x-=  1;
                // Get var name
                String variableName = line.substring(0, line.indexOf("-"));
                String value        = line.substring(line.indexOf("=")+1, line.indexOf(";"));
                int val             = Integer.parseInt( value );

                if(global_vars.get(variableName) != null) {
                    int previousValue = Integer.parseInt( global_vars.get(variableName) );
                    global_vars.put(variableName, ""+ (previousValue-val) );  // Assign value
                } else // Only if not a local var
                    if(local_vars.get(variableName) != null) {
                        int previousValue = Integer.parseInt( local_vars.get(variableName) );
                        local_vars.put(variableName, ""+ (previousValue-val) );  // Assign value
                    }
            }

            if((line.split("/",-1).length-1) == 1 && (line.split("=",-1).length-1) == 1) { // x/=  1;
                // Get var name
                String variableName = line.substring(0, line.indexOf("/"));
                String value        = line.substring(line.indexOf("=")+1, line.indexOf(";"));
                int val             = Integer.parseInt( value );

                if(global_vars.get(variableName) != null) {
                    int previousValue = Integer.parseInt( global_vars.get(variableName) );
                    global_vars.put(variableName, ""+ (previousValue/val) );  // Assign value
                } else // Only if not a local var
                    if(local_vars.get(variableName) != null) {
                        int previousValue = Integer.parseInt( local_vars.get(variableName) );
                        local_vars.put(variableName, ""+ (previousValue/val) );  // Assign value
                    }
            }

            if((line.split("\\*",-1).length-1) == 1 && (line.split("=",-1).length-1) == 1) { // x*=  1;
                // Get var name
                String variableName = line.substring(0, line.indexOf("*"));
                String value        = line.substring(line.indexOf("=")+1, line.indexOf(";"));
                int val             = Integer.parseInt( value );

                if(global_vars.get(variableName) != null) {
                    int previousValue = Integer.parseInt( global_vars.get(variableName) );
                    global_vars.put(variableName, ""+ (previousValue*val) );  // Assign value
                } else // Only if not a local var
                    if(local_vars.get(variableName) != null) {
                        int previousValue = Integer.parseInt( local_vars.get(variableName) );
                        local_vars.put(variableName, ""+ (previousValue*val) );  // Assign value
                    }
            }

            /* * * * * FUNCTIONS * * * * */
            // Select "console"-"class"
            if(line.startsWith("console.")) {
                console.callMethod(line.substring( line.indexOf(".") + 1 ), local_vars, global_vars);
            }

            /* * * * * STATEMENTS * * * * */

            // if statement, Example: if(x==0) {}
            if(line.startsWith("if")) {

                // Isolate Statement from "(" to ")" -> "x==0"
                String statement = line.substring( line.indexOf("(")+1, line.indexOf(")") );
                System.out.println(statement);

                // Remove first "=" for easier handling -> "x=0"
                statement = statement.substring(0, statement.indexOf("=") )+statement.substring(statement.indexOf("=")+1);

                String[] statement2 = statement.split("=");

                //  find out which site is the var



            }

        }

    }

}
