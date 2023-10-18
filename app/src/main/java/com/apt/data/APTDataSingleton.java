package com.apt.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import javax.swing.JButton;

public class APTDataSingleton {

    public String user;
    public String password;

    public HashMap<String,String[]> data; // Link: {productName, currPrice, origPrice, histPrice}
    public HashSet<String> remove_track;
    public HashSet<String> add_track;
    public String email;

    public String[] links; // Sorted links currently in table

    private static APTDataSingleton instance = null;

    public APTDataSingleton() {
        this.data = new HashMap<>();
        this.remove_track = new HashSet<>();
        this.add_track = new HashSet<>();
        this.email = null;
    };

    public static APTDataSingleton getInstance() {
        if (instance == null) {
            instance = new APTDataSingleton();
        }
        return instance;
    };
    
    public void refreshData() {
        // Update AWS 
        // Get Data Back From AWS
        System.out.println("Still unimplemented refreshData() function in APTDataSingleton.java");
    };

    public Object[][] getTableData() { 
        // Get all links to display as a sorted array
        APTDataSingleton dataCore = APTDataSingleton.getInstance();
        HashSet<String> links_set = new HashSet<>(dataCore.data.keySet());
        links_set.removeAll(remove_track);
        links_set.addAll(add_track);
        links = new String[links_set.size()];
        int i = 0;
        for (String element: links_set) { 
            links[i++] = element; 
        } 
        Arrays.sort(links);

        // Fill Object[][] based on results
        Object[][] tableData = new Object[links.length][6];
        for (i = 0; i < links.length; i++) {
            // Add product number
            tableData[i][0] = new JButton(Integer.toString(i+1));
            ((JButton) tableData[i][0]).setFocusPainted(false);

            // Add cached values if they exist
            if (data.containsKey(links[i])) {
                tableData[i][1] = data.get(links[i])[0];
                tableData[i][2] = data.get(links[i])[1];
                tableData[i][3] = data.get(links[i])[2];
                tableData[i][4] = data.get(links[i])[3];
            } else {
                tableData[i][1] = links[i];
                tableData[i][2] = "";
                tableData[i][3] = "";
                tableData[i][4] = "";
            }

            // Add delete button
            tableData[i][5] = new JButton("Del");
            ((JButton) tableData[i][5]).setFocusPainted(false);
        };

        return tableData;
    };

    public void setUser(String user) {
        this.user = user;
    };

    public String getUser() {
        return this.user;
    };
    
    public void setPassword(String password) {
        this.password = password;
    };

    public void deleteEntry(String link) {
        if (this.add_track.contains(link)) {
            this.add_track.remove(link);
        } else {
            this.remove_track.add(link);
        }
    }

    public void addEntry(String link) {
        if (this.remove_track.contains(link)) {
            this.remove_track.remove(link);
        } else {
            this.add_track.add(link);
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
