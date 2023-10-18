package com.apt.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Arrays;
import javax.swing.JButton;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class APTDataSingleton {

    public String user;
    public String password;

    public HashMap<String,String[]> data; // Link: {productName, currPrice, origPrice, histPrice}
    public HashSet<String> remove_track;
    public HashSet<String> add_track;
    public String email;

    public String[] links; // Sorted links currently in table

    public String API_ID = "API-ID-HERE";
    public String API_REGION = "us-east-1";

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
        try {
            // Start HTTP Client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            String[] temp;

            // PUT Request For updateUserDB
            HttpPut putRequest = new HttpPut("https://" + API_ID + ".execute-api." + API_REGION + ".amazonaws.com/prod/updateUserDB");
            String payload = "{\"username\":\"" + user + "\",\"password\":\"" + password + "\"";
            if (remove_track.size() > 0) {
                temp = new String[remove_track.size()];
                remove_track.toArray(temp);
                String removeLinks = String.join(",", temp);
                payload += ",\"remove_track\":\"" + removeLinks + "\"";
            }
            if (add_track.size() > 0) {
                temp = new String[add_track.size()];
                add_track.toArray(temp);
                String addLinks = String.join(",", temp);
                payload += ",\"add_track\":\"" + addLinks + "\"";
            }
            if (email != null) {
                payload += ",\"email\":\"" + email + "\"";
            }
            payload += "}";
            StringEntity input = new StringEntity(payload);
            input.setContentType("application/json");
            putRequest.setEntity(input);
            httpClient.execute(putRequest);
            httpClient.getConnectionManager().shutdown();
            
            // GET Request For getTrackedPrices
            httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet("https://" + API_ID + ".execute-api." + API_REGION + ".amazonaws.com/prod/getTrackedPrices?username=" + user + "&password=" + password);
            getRequest.addHeader("accept", "application/json");
            HttpResponse response = httpClient.execute(getRequest);
            httpClient.getConnectionManager().shutdown();
            if (response.getStatusLine().getStatusCode() == 200) {
                data.clear();
                remove_track.clear();
                add_track.clear();
                email = null;
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                JsonObject json_result = new Gson().fromJson(result, JsonObject.class);
                for (JsonElement elem: (JsonArray) json_result.get("Items")) {
                    JsonObject item = elem.getAsJsonObject();
                    for (Iterator<String> it = item.keySet().iterator(); it.hasNext();) {
                        String productLink = (String) it.next();
                        String[] aptInfo = new Gson().fromJson(item.get(productLink), String[].class); 
                        data.put(productLink, aptInfo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    public Object[][] getTableData() { 
        // Get all links to display as a sorted array
        APTDataSingleton dataCore = APTDataSingleton.getInstance();
        HashSet<String> links_set = new HashSet<>(dataCore.data.keySet());
        links_set.removeAll(remove_track);
        links_set.addAll(add_track);
        links = new String[links_set.size()];
        links_set.toArray(links);
        Arrays.sort(links);

        // Fill Object[][] based on results
        Object[][] tableData = new Object[links.length][6];
        for (int i = 0; i < links.length; i++) {
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
