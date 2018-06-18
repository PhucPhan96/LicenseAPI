/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.payment;

import java.util.LinkedHashMap;
import nfc.messages.request.PayRequest;
import nfc.messages.request.PaymentCancel;
import nfc.model.PaymentOrderHistory;
import nfc.service.IOrderService;
import nfc.serviceImpl.common.NFCHttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Admin
 */
public class LGPayNowPayment extends PaymentAbstract{
    
    @Autowired
    private IOrderService orderDAO;
    
    private static String CST_PLATFORM = "test";
    private static String CST_MID = "o2osystem";//id store
    private static String URL_PAYMENT = "http://211.249.62.166/lg-paynow/payres.jsp";
    private static String URL_CANCEL = "http://211.249.62.166/lg-paynow/cancel.jsp";
    
    public LGPayNowPayment(){
        this.payment_code = "LGPAYNOW";
    }

    public boolean payment(LinkedHashMap<String, String> paymentRequest, String orderId) {
        NameValuePair[] requestData = {
            new NameValuePair("CST_PLATFORM", "test"),
            new NameValuePair("CST_MID", "o2osystem"),
            new NameValuePair("LGD_PAYKEY", paymentRequest.get("LGD_PAYKEY"))
        };
        JSONObject result = NFCHttpClient.getInstance().sendPostRequestParam(requestData, URL_PAYMENT);
        System.err.println("LGPayNow result" + result.toJSONString());
        if(result.containsKey("LGD_RESPCODE") &&  result.get("LGD_RESPCODE").toString().equals("0000")){
            String LGD_TID_RESPONSE = result.get("LGD_TID").toString();
            //savePaymentOrderHistory(orderId, LGD_TID_RESPONSE);
            return true;
        }   
        else{
            return false;
        }
    }
    
    
    private void savePaymentOrderHistory(String orderId, String LGD_TID_RESPONSE){
        PaymentOrderHistory paymentOrderHistory = new PaymentOrderHistory();
        paymentOrderHistory.setOrder_id(orderId);
        paymentOrderHistory.setPayment_unique_number(LGD_TID_RESPONSE);
        paymentOrderHistory.setPayment_code(payment_code);
        orderDAO.savePaymentOrderHistory(paymentOrderHistory);
    }
    
    public boolean cancel(PaymentCancel paymentCancelRequest) {
        JSONObject requestData = new JSONObject();
        requestData.put("LGD_TID", paymentCancelRequest.getId());
       
        JSONObject requestHttp = new JSONObject();
        requestHttp.put("url", URL_PAYMENT);
        requestHttp.put("isAuthorization", "false");
        JSONObject result = NFCHttpClient.getInstance().sendPost(requestHttp, requestData);
        if(result.containsKey("LGD_RESPCODE") &&  result.get("LGD_RESPCODE").toString().equals("0000")){
            return true;
        }   
        else{
            return false;
        }
    }
    
}
