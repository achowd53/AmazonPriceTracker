package com.apt;

import com.apt.frame.mainFrame;
import javax.swing.SwingUtilities;

public class App {
    public static void main( String[] args ) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new mainFrame("Tom", "Hanks");
            }
        });
        System.out.println( "Hello World!" );
    }
}
