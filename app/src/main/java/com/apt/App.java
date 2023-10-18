package com.apt;

import com.apt.data.APTDataSingleton;
import com.apt.frame.mainFrame;
import javax.swing.SwingUtilities;

public class App {
    public static void main( String[] args ) {
        // Get username and password from input
        if (args.length != 2) {
            System.out.println("Incorrect number of arguments entered, please enter username password");
            return;
        }

        // Add username and password to data core
        APTDataSingleton dataCore = APTDataSingleton.getInstance();
        dataCore.setUser(args[0]);
        dataCore.setPassword(args[1]);

        // Create main frame
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new mainFrame();
            }
        });
    }
}
