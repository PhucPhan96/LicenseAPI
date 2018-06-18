/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Admin
 */
public class PushNotification {
    
    private static final Logger log = LoggerFactory.getLogger(PushNotification.class);
    
    private final static String AUTH_KEY_FCM = "AIzaSyDh9QeXUiRy2WB4iKQCyeSttjVwWvGusP4";
    private final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    
    private static class SingletonHelper{
        private static final PushNotification INSTANCE = new PushNotification(); 
    }
    
    public static PushNotification getInstance(){
        return SingletonHelper.INSTANCE;
    }

    public boolean pushNotification(String token, String title, String message) {
        String authKey = AUTH_KEY_FCM;
        String FMCurl = API_URL_FCM;
        URL url;
        JSONObject result = null;
        HttpURLConnection conn;

        try {
            url = new URL(FMCurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=" + authKey);
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject json = new JSONObject();
            JSONObject info = new JSONObject();
            json.put("to", token);
            info.put("title", title);   // Notification title
            info.put("body", message); // Notification body
            info.put("sound", "default");
            json.put("notification", info);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = rd.readLine()) != null) {
                sb.append(line + '\n');
            }

            result = (JSONObject) new JSONParser().parse(sb.toString());

            if (Integer.parseInt(result.get("success").toString()) > 0) {
                return true;
            }
        } catch (Exception ex) {
            System.err.println("Error " + ex.getMessage() );
            log.error(ex.getMessage());
        } 

        return false;
    }
}
