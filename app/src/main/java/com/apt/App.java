package com.apt;

import com.apt.frame.mainFrame;

public class App {
    public static void main( String[] args ) {
        mainFrame window = new mainFrame();
        window.updateTitle("Tom Hanks");
        System.out.println( "Hello World!" );
    }
}
