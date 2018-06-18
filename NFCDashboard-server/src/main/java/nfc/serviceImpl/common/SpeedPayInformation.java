/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.common;

import java.util.Date;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class SpeedPayInformation {
    private static final Logger log =  Logger.getLogger(SpeedPayInformation.class.toString());
    
    private String token = "";
    private int expried;
    private Date dateLogin;
    private String username = "test12";
    private String password = "11111111";
    
    public enum PaymentAPI {
        SPEED_PAY
    }
    
    private static class SingletonHelper{
        private static final SpeedPayInformation INSTANCE = new SpeedPayInformation(); 
    }
    
    public static SpeedPayInformation getInstance(){
        return SingletonHelper.INSTANCE;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpried() {
        return expried;
    }

    public void setExpried(int expried) {
        this.expried = expried;
    }

    public Date getDateLogin() {
        return dateLogin;
    }

    public void setDateLogin(Date dateLogin) {
        this.dateLogin = dateLogin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
}
