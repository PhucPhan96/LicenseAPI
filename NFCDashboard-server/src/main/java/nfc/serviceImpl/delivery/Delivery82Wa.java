/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.delivery;

import nfc.messages.base.DeliveryRequestPacket;
import nfc.messages.request.Delivery82WaRequest;
import nfc.messages.request.PayRequest;
import nfc.serviceImpl.common.NFCHttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.json.simple.JSONObject;

/**
 *
 * @author Admin
 */
public class Delivery82Wa extends DeliveryAbstract{
    
    public Delivery82Wa(){
        this.deliveryCode="82WA";
    }

    public JSONObject order(DeliveryRequestPacket deliveryRequest) {
        Delivery82WaRequest request = new Delivery82WaRequest();
        request.setCust_tel(deliveryRequest.getCust_tel());
        request.setReach_addr(deliveryRequest.getReach_addr());
        request.setPrice(deliveryRequest.getPrice());
        return sendOrder(request);
    }
    
    private JSONObject sendOrder(Delivery82WaRequest request){
        JSONObject result = NFCHttpClient.getInstance().sendGet(initRequestHTTP(), initQueryString(request));
        System.err.println("delivery api " + result.toJSONString());
        return result;
    }
    
    private JSONObject initRequestHTTP(){
        JSONObject requestHttp = new JSONObject();
        requestHttp.put("url", "http://was.0030.co.kr/appOrder.php");
        return requestHttp;
    }
    
    private NameValuePair[] initQueryString(Delivery82WaRequest request){
        NameValuePair[] data = {
            new NameValuePair("reg_no", request.getReg_no()),
            new NameValuePair("cust_tel", request.getCust_tel()),
            new NameValuePair("reach_addr", request.getReach_addr()),
            new NameValuePair("price", request.getPrice() + ""),
            new NameValuePair("pay_method", request.getPay_method() + "")
        };
        return data;
    }
    
    
    
}
