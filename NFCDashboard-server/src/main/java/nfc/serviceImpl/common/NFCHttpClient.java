/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.common;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;import org.apache.commons.httpclient.methods.GetMethod;
;
/**
 *
 * @author Admin
 */
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class NFCHttpClient {
    
    private static final Logger log =  Logger.getLogger(NFCHttpClient.class.toString());
    
    private static class SingletonHelper{
        private static final NFCHttpClient INSTANCE = new NFCHttpClient(); 
    }
    
    public static NFCHttpClient getInstance(){
        return SingletonHelper.INSTANCE;
    }
    
    public JSONObject sendPost(JSONObject infoRequest, JSONObject json){
        JSONObject  response= new JSONObject ();
        try{
            System.err.println("data" + json.toJSONString());
            PostMethod post = new PostMethod(infoRequest.get("url").toString());
            StringRequestEntity requestEntity = new StringRequestEntity(json.toJSONString(),"application/json","UTF-8");
            if(infoRequest.get("isAuthorization").toString().equals("true") ){
                post.addRequestHeader("Authorization", "bearer " + infoRequest.get("access_token").toString());
            }
            else{
                post.setRequestEntity(requestEntity);
            }
            post.setRequestEntity(requestEntity);
            HttpClient client = new HttpClient();
            int statusCode = client.executeMethod(post);
            log.info("status code: " + statusCode);
            log.info("status text: " + HttpStatus.getStatusText(statusCode));
            log.info("response body: " + post.getResponseBodyAsString());
            response = (JSONObject)(new JSONParser().parse(new InputStreamReader(post.getResponseBodyAsStream())));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("error: " + e.getMessage());
        }
        return response;
    } 
    
    public JSONObject sendGet(JSONObject infoRequest, NameValuePair[] params){
        JSONObject  response= new JSONObject ();
        try{
            GetMethod get = new GetMethod(infoRequest.get("url").toString());
            get.setQueryString(params);
            HttpClient client = new HttpClient();
            int statusCode = client.executeMethod(get);
            log.info("status code: " + statusCode);
            log.info("status text: " + HttpStatus.getStatusText(statusCode));
            log.info("response body: " + get.getResponseBodyAsString());
            response = (JSONObject)(new JSONParser().parse(new InputStreamReader(get.getResponseBodyAsStream())));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("error: " + e.getMessage());
        }
        return response;
    } 
    
    
    public JSONObject sendPostRequestParam(NameValuePair[] params, String url) {

        JSONObject response = new JSONObject();
        PostMethod post = new PostMethod(url);
        //PostMethod post = new PostMethod("http://localhost:8080/smart.xpay/CardAuth.jsp");
        post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
//        NameValuePair[] data = {
//            new NameValuePair("CST_PLATFORM", req.CST_PLATFORM),
//            new NameValuePair("CST_MID", req.CST_MID),//MAIN CODE
//            //    		    new NameValuePair("LGD_OID", "tdm00007"),//자동 생성
//            new NameValuePair("LGD_OID", oid),//자동 생성
//
//            new NameValuePair("LGD_AMOUNT", req.LGD_AMOUNT),
//            new NameValuePair("LGD_PAN", req.LGD_PAN),//NHU DUOI
//            new NameValuePair("LGD_INSTALL", req.LGD_INSTALL),//NULL VAN HIEU
//            new NameValuePair("VBV_ECI", "010"),
//            new NameValuePair("LGD_BUYERPHONE", req.LGD_BUYERPHONE),//GUI LEN
//            new NameValuePair("LGD_BUYER", req.LGD_BUYER),//GUI LEN
//            new NameValuePair("LGD_BUYERID", req.LGD_BUYERID),
//            new NameValuePair("LGD_PRODUCTINFO", req.LGD_PRODUCTINFO),//gui lEN
//            new NameValuePair("LGD_DIVIDE_INFO", req.LGD_DIVIDE_INFO)
//        };
//    		post.setRequestBody(data);
        post.setQueryString(params);

        try {
//			InputStream in = post.getResponseBodyAsStream();
            HttpClient client = new HttpClient();
            int statusCode = client.executeMethod(post);

            log.info("status code: " + statusCode);
            log.info("status text: " + HttpStatus.getStatusText(statusCode));
            log.info("response body: " + post.getResponseBodyAsString());
            response = (JSONObject)(new JSONParser().parse(new InputStreamReader(post.getResponseBodyAsStream())));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //log.error("data_bean: " + e.getMessage());
        }
        return response;
    }
}
