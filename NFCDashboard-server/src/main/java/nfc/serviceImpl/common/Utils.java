package nfc.serviceImpl.common;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.Set;

import org.apache.commons.codec.binary.Hex;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import nfc.model.ViewModel.GridFiltering;
import nfc.model.ViewModel.GridView;
import org.springframework.util.StringUtils;

public class Utils {
    public static String appId="e6271952-d4b9-4ed3-b83b-63a56d47a713";
    public static String uploadUrl="/uploads/images/";
    public static String ORDER_FAILED = "FAILED";
    public static String ORDER_PAID = "PAID";
    public static String ORDER_COOKING = "COOKING";
    public static String ORDER_COMPLETE = "COMPLETE";
    public static String ORDER_DELIVERY_SUCCESS = "DELIVERY_SUCCESS";
    public static String ORDER_DELIVERY_FAILED = "DELIVERY_FAILED";
    public static String ORDER_CANCEL = "CANCEL";
    public static String ORDER_REQUEST_CANCEL = "REQUEST_CANCEL";
    
    public static String DANAL_RETURN_URL="http://192.168.0.31:1234/NFCDashboard-server/app/danal/return";
    public static String DANAL_CANCEL_URL="http://192.168.0.31:1234/NFCDashboard-server/app/danal/cancel";
    public static String DANAL_NOTI_URL="http://192.168.0.31:1234/NFCDashboard-server/app/danal/cancel";
    
    
    public static long orderId = -1;
    
    public static String convertObjectToJsonString(Object obj){
            Gson gson = new Gson();
            String jsonInString = gson.toJson(obj);
            return jsonInString;
            //return "";
    }
    public static String ConvertObjectToInsertSQL(Object obj, String table)
    {
        String sql = "";
        Field[] fields = obj.getClass().getDeclaredFields();
        String lstField="";
        String lstValue="";
        for (int i = 0; i < fields.length; i++) {
            try{
                if (!Collection.class.isAssignableFrom(fields[i].getType())) {
                    fields[i].setAccessible(true);
                    String name = fields[i].getName();
                    String value = fields[i].get(obj)!=null?fields[i].get(obj).toString():null;
                    if(i!=0){
                        lstField=lstField+",";
                        lstValue=lstValue+",";
                    }
                    if(fields[i].get(obj) instanceof String){
                        lstValue=lstValue+"'"+value+"'";
                    }
                    else{
                        lstValue=lstValue+value;
                    }
                    lstField=lstField+name;
                }
            } catch (IllegalAccessException ex) {
                System.out.println("Error " + ex.getMessage());
            }
        }
        sql="insert into " + table + "(" + lstField + ")values(" + lstValue + ");";
        return sql;
    }
    public static JSONObject convertStringToJsonObject(String jsonStr){
        JSONParser parser = new JSONParser();
        JSONObject json = new JSONObject() ;
        try {
            json = (JSONObject) parser.parse(jsonStr);
        } catch (ParseException e) {
            System.out.print("Error " + e.getMessage());
        }
        return json;
    }
    public static Object convertJsonObjectToClass(JSONObject jsonObj, String className, String[] keyRestrict){
        try {
            Class c=Class.forName(className);
            Object instance = c.newInstance();
            Set<String> keys =  jsonObj.keySet();
            for(String key:keys)
            {
                if(!Arrays.asList(keyRestrict).contains(key))
                {
                    Field field = c.getDeclaredField(key);
                    field.setAccessible(true);
                    field.set(instance,jsonObj.get(key));
                }
            }
                return instance;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String generationCode(){
        Random rNo = new Random();
        int code = rNo.nextInt((9999 - 1000) + 1) + 1000;
        return String.valueOf(code);
    }

    static final String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    public static String randomPassword(int len){
       StringBuilder sb = new StringBuilder(len);
       for( int i = 0; i < len; i++ ) 
          sb.append(characters.charAt( rnd.nextInt(characters.length())));
       return sb.toString();
    }
    public static String Sha1(String password){
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hash = sha.digest(password.getBytes());
            return Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
    public static String BCryptPasswordEncoder(String password){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
    
    public static String getCurrentDateYYYYMMDD(){
        Date date = new Date();
        String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
        return modifiedDate;
    }
    
    public static String convertDateToString(Date date){
        String modifiedDate= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        return modifiedDate;
    }
        
    
    public static StringBuilder generateGridFilterString(GridView gridView, String... rejectCoumns){
        StringBuilder builder = new StringBuilder();
        List<GridFiltering> filters = gridView.getFiltering();
        for(int i =0; i <filters.size() ; i++){
            if(!StringUtils.isEmpty(filters.get(i).getValue()) && !isColumReject(filters.get(i).getName(), rejectCoumns)){
                builder.append(filters.get(i).getName() +  " like '%" +filters.get(i).getValue()+"%' and ");
            }
        }
        if(builder.length()>0){
            builder.replace(builder.length()-4, builder.length(), "");
        }
        return builder;
    }
    
    
    private static boolean isColumReject(String column, String... rejectCoumns){
        for(String col : rejectCoumns){
            if(col.equals(column)){
                return true;
            }
        }
        return false;
    }
    
}
