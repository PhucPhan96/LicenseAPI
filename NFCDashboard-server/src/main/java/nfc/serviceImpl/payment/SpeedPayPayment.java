/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.payment;

import java.util.Date;
import java.util.LinkedHashMap;
import nfc.messages.request.PaymentCancel;
import nfc.messages.base.PaymentCancelPacket;
import nfc.messages.base.PaymentRequestPacket;
import nfc.messages.request.PayRequest;
import nfc.model.PaymentOrderHistory;
import nfc.model.ViewModel.PaymentView;
import nfc.service.IOrderService;
import nfc.serviceImpl.common.NFCHttpClient;
import nfc.serviceImpl.common.SpeedPayInformation;
import org.apache.commons.httpclient.NameValuePair;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 *
 * @author Admin
 */
public class SpeedPayPayment extends PaymentAbstract{
    
    @Autowired
    private IOrderService orderDAO;
    
    public SpeedPayPayment(){
        this.payment_code = "SPEEDPAY";
    }
    
    public boolean payment(LinkedHashMap<String, String> paymentRequest, String orderId) {
        JSONObject requestData = new JSONObject();
        requestData.put("card_no", paymentRequest.get("card_no"));
        requestData.put("card_ymd", paymentRequest.get("card_ymd"));
        requestData.put("amt", "500");//paymentRequest.getAmt()
        requestData.put("sell_nm", paymentRequest.get("sell_nm"));
        requestData.put("pg_type", "PG");
        requestData.put("pay_type", "O2B2_APP");
        requestData.put("product_nm", paymentRequest.get("product_nm"));
        requestData.put("buyer_nm", paymentRequest.get("buyer_nm"));
        requestData.put("buyer_phone_no", paymentRequest.get("buyer_phone_no"));
        requestData.put("buyer_email", paymentRequest.get("buyer_email"));
        requestData.put("tax_free_yn", "true");
        requestData.put("test_yn", "true");
        JSONObject requestHttp = new JSONObject();
        requestHttp.put("url", "https://speed-pay.co.kr/api/v1/orders");
        requestHttp.put("isAuthorization", "true");
        requestHttp.put("access_token", getTokenPaymentApi());
        JSONObject result = NFCHttpClient.getInstance().sendPost(requestHttp, requestData);
        if(result.containsKey("success") &&  result.get("success").toString().equals("true")){
            savePaymentOrderHistory(orderId, result);
            return true;
        }   
        else{
            return false;
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    private void savePaymentOrderHistory(String orderId, JSONObject resultPayment){
        PaymentOrderHistory paymentOrderHistory = new PaymentOrderHistory();
        paymentOrderHistory.setOrder_id(orderId);
        paymentOrderHistory.setPayment_unique_number(resultPayment.get("id").toString());
        JSONObject payDetail = (JSONObject)resultPayment.get("pay_det");
        paymentOrderHistory.setCard_id(payDetail.get("card_id").toString());
        paymentOrderHistory.setCard_nm(payDetail.get("card_nm").toString());
        paymentOrderHistory.setPayment_code(payment_code);
        orderDAO.savePaymentOrderHistory(paymentOrderHistory);
    }

    public boolean cancel(PaymentCancel paymentCancelRequest) {
        String url = "https://speed-pay.co.kr/api/v1/order/payments/"+ paymentCancelRequest.getId()+ "/cancel.json";
        JSONObject requestHttp = new JSONObject();
        requestHttp.put("url", url);
        requestHttp.put("isAuthorization", "true");
        requestHttp.put("access_token", getTokenPaymentApi());
        JSONObject requestJson = new JSONObject();
        JSONObject result = NFCHttpClient.getInstance().sendPost(requestHttp, requestJson);
        if(result.containsKey("success") && result.get("success").toString() == "true"){
            return true;
        }
        else{
            return false;
        }
        
    }
    
    private String getTokenPaymentApi(){
        if(checkTokenExpried()){
            loginSpeedPayAPI();
        }
        return SpeedPayInformation.getInstance().getToken();
    }
    
    private boolean checkTokenExpried(){
        if(SpeedPayInformation.getInstance().getToken() != ""){
            Date currentDate = new Date();
            Date loginDate = SpeedPayInformation.getInstance().getDateLogin();
            if(((currentDate.getTime() - loginDate.getTime())/1000) < SpeedPayInformation.getInstance().getExpried()){
                return false;
            }
        }
        return true;
    }
    
    private void loginSpeedPayAPI(){
        JSONObject requestHttp = new JSONObject();
        requestHttp.put("url", "https://speed-pay.co.kr/oauth/token");
        requestHttp.put("isAuthorization", "false");  
        JSONObject json = new JSONObject();
        json.put("username", SpeedPayInformation.getInstance().getUsername());
        json.put("password", SpeedPayInformation.getInstance().getPassword());
        json.put("grant_type", "password");
        JSONObject result = NFCHttpClient.getInstance().sendPost(requestHttp, json);
        setLoginInformation(result);
    }
    
    private void setLoginInformation(JSONObject result){
        if(result.containsKey("access_token") && (!StringUtils.isEmpty(result.get("access_token"))) ){
            SpeedPayInformation.getInstance().setDateLogin(new Date());
            SpeedPayInformation.getInstance().setExpried(Integer.parseInt(result.get("expires_in").toString()));
            SpeedPayInformation.getInstance().setToken(result.get("access_token").toString());
        }
        else{
            SpeedPayInformation.getInstance().setToken("");
        }
    }

    
}
