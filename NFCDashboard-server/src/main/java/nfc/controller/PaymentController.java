/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import nfc.model.Payment;
import nfc.model.Role;
import nfc.model.ViewModel.PaymentView;
import nfc.service.IPaymentService;
import nfc.serviceImpl.common.NFCHttpClient;
import nfc.serviceImpl.common.Utils;
import nfc.serviceImpl.payment.DanalFunction;
import org.apache.commons.httpclient.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
public class PaymentController {
    
    @Autowired
    private IPaymentService paymentDAO;
    
    @RequestMapping(value="payments",method=RequestMethod.GET)
    public List<Payment> getPayments(){
        return paymentDAO.getPayments();
    }
    @RequestMapping(value="payment/view/{id}", method=RequestMethod.GET)
    public PaymentView getPaymentView(@PathVariable("id") int paymentId){
        return paymentDAO.getPaymentView(paymentId);
    }
    @RequestMapping(value="payment/view/add", method=RequestMethod.POST)
    public @ResponseBody String insertPaymentView(@RequestBody PaymentView paymentView){
        String data = paymentDAO.insertPaymentView(paymentView) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value="payment/view/update", method=RequestMethod.PUT)
    public @ResponseBody String editPaymentView(@RequestBody PaymentView paymentView){
        String data = paymentDAO.updatePaymentView(paymentView) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value="payment/view/delete/{id}", method=RequestMethod.DELETE)
    public @ResponseBody String deletePaymentView(@PathVariable("id") int paymentId){
        String data = paymentDAO.deletePaymentView(paymentId) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    
    @RequestMapping(value="payment/update/default/{id}", method=RequestMethod.DELETE)
    public @ResponseBody String updatePaymentDefault(@PathVariable("id") int paymentId){
        String data = paymentDAO.updatePaymentDefault(paymentId) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    
//    @RequestMapping(value="payment/login", method=RequestMethod.GET)
//    public String loginPaymentAPI(){
//        NameValuePair[] data = {
//            new NameValuePair("username", "test12"),
//            new NameValuePair("password", "1111111111"),//gui lEN
//            new NameValuePair("grant_type", "password")
//        };
//        return NFCHttpClient.getInstance().sendPost("https://speed-pay.co.kr/oauth/token", data).toJSONString();
//    }
    @RequestMapping(value="app/danal/auth", method=RequestMethod.POST)
    public @ResponseBody Map danalAuthentication(HttpServletRequest request){
        Map REQ_DATA = new HashMap();
        return danalAuthentication(REQ_DATA, request);
    }
    
    private Map danalAuthentication(Map REQ_DATA, HttpServletRequest request){
        String paymentType = request.getParameter("PAYMENTTYPE");
        if(paymentType.equals("DANALTELE")){
            return danalTeleAuth(REQ_DATA, request);
        }
        else{
            return authWireTransferAndVirtualAccount(REQ_DATA, request);
        }
    }
    
    
    private Map authWireTransferAndVirtualAccount(Map REQ_DATA, HttpServletRequest request){
        Map RES_DATA = null;
        String paymentType = request.getParameter("PAYMENTTYPE");
        REQ_DATA.put("ORDERID", (String) request.getParameter("ORDERID"));
	REQ_DATA.put("AMOUNT", request.getParameter("AMOUNT"));
        REQ_DATA.put("USERID", (String) request.getParameter("USERID"));
	REQ_DATA.put("USEREMAIL", (String) request.getParameter("USEREMAIL"));
	REQ_DATA.put("USERAGENT", (String) request.getParameter("USERAGENT"));
        REQ_DATA.put("CANCELURL", Utils.DANAL_CANCEL_URL);
        REQ_DATA.put("TXTYPE", "AUTH");
        REQ_DATA.put("ITEMNAME", (String) request.getParameter("ITEMNAME"));
        REQ_DATA.put("RETURNURL", Utils.DANAL_RETURN_URL);
        if(paymentType.equals("DANALWIRETRANSFER")){
            REQ_DATA.put("ISNOTI", "N");
            REQ_DATA.put("SERVICETYPE", "WIRETRANSFER");
            REQ_DATA.put("USERNAME", (String) request.getParameter("USERNAME"));
            REQ_DATA.put("BYPASSVALUE", "a=b;c=d;");
            RES_DATA = DanalFunction.getInstance().CallDanalBank(REQ_DATA, false);
        }
        else{
            REQ_DATA.put("NOTIURL", Utils.DANAL_NOTI_URL);
            REQ_DATA.put("ACCOUNTHOLDER", request.getParameter("ACCOUNTHOLDER"));
            REQ_DATA.put("EXPIREDATE", (String)request.getParameter("EXPIREDATE"));
            REQ_DATA.put("SERVICETYPE", "DANALVACCOUNT");
            RES_DATA = DanalFunction.getInstance().CallVAccount(REQ_DATA, false);
        }
        return RES_DATA;
    }
    
    private Map danalTeleAuth(Map REQ_DATA, HttpServletRequest request){
        Map TransR = new HashMap();
	TransR.put( "Command", "ITEMSEND2" );
	TransR.put( "SERVICE", "TELEDIT" );
	TransR.put( "ItemCount", "1" );
	TransR.put( "OUTPUTOPTION", "DEFAULT" );
	TransR.put( "ID", DanalFunction.ID );
	TransR.put( "PWD", DanalFunction.PWD );
	String CPName = "CP명";
	String ItemAmt = request.getParameter("AMOUNT");
	String ItemName = "휴대폰결제";
	String ItemCode = "1111111111";
	String ItemInfo = DanalFunction.getInstance().MakeItemInfo( ItemAmt,ItemCode,ItemName );
	TransR.put( "ItemInfo", ItemInfo );
	TransR.put( "SUBCP", "" );
	TransR.put( "USERID", "USERID" );
	TransR.put( "ORDERID", "ORDERID" );
	TransR.put( "IsPreOtbill", "N" );
	TransR.put( "IsSubscript", "N" );
	Map ByPassValue = new HashMap();
	ByPassValue.put( "BgColor", "00" );
	ByPassValue.put( "TargetURL", "http://192.168.0.31:1234/NFCDashboard-server/app/danal/return" );
	ByPassValue.put( "BackURL", "http://localhost/Danal/Teledit/BackURL.jsp" );
	ByPassValue.put( "IsUseCI", "N" );
	ByPassValue.put( "CIURL", "http://localhost/Danal/Teledit/images/ci.gif" );
	ByPassValue.put( "Email", request.getParameter("Email"));
	ByPassValue.put( "IsCharSet", "" );
	ByPassValue.put( "ByBuffer", "This value bypass to CPCGI Page" ) ;
	ByPassValue.put( "ByAnyName", "AnyValue" );
        Map Res = DanalFunction.getInstance().CallTeledit(TransR);
        return Res;
    }
    
    
    
    
    @RequestMapping(value="app/danal/return", method=RequestMethod.POST)
    public @ResponseBody Map authenWireTransferSuccess(HttpServletRequest request){
        return request.getParameterMap();
    }
    @RequestMapping(value="app/danal/virtual/log", method=RequestMethod.POST)
    public @ResponseBody Map authenVirtualLog(HttpServletRequest request){
        //log virtual account transfer
        return request.getParameterMap();
    }
    
    @RequestMapping(value="app/danal/cancel", method=RequestMethod.POST)
    public @ResponseBody Map authenDanalCancel(HttpServletRequest request){
        return request.getParameterMap();
    }
}
