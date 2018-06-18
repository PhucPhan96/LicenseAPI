/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.payment;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import nfc.messages.request.PaymentCancel;
import nfc.model.PaymentOrderHistory;
import nfc.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Admin
 */
public class DanalWireTransferPayment extends PaymentAbstract{
    
    @Autowired
    private IOrderService orderDAO;
    
    public DanalWireTransferPayment(){
        this.payment_code = "DANALWIRETRANSFER";
    }
    
    public boolean payment(LinkedHashMap<String, String> paymentRequest, String orderId) {
        String RES_STR = DanalFunction.getInstance().toDecrypt((String) paymentRequest.get("RETURNPARAMS"), DanalFunction.IV_WIRETRANSFER, DanalFunction.CRYPTOKEY_WIRETRANSFER);
	Map retMap = DanalFunction.getInstance().str2data(RES_STR);
	String returnCode = (String) retMap.get("RETURNCODE");
	String returnMsg = (String) retMap.get("RETURNMSG");

	if (returnCode == null || !"0000".equals(returnCode)) {
            System.out.println("trx failed. " + returnMsg + "["	+ returnCode + "]");
            return false;
	}

	Map REQ_DATA = new HashMap();
	Map RES_DATA = new HashMap();
	REQ_DATA.put("TID", (String) retMap.get("TID"));
	REQ_DATA.put("AMOUNT", retMap.get("AMOUNT"));
	REQ_DATA.put("TXTYPE", "BILL");
	REQ_DATA.put("SERVICETYPE", "WIRETRANSFER");
	RES_DATA = DanalFunction.getInstance().CallDanalBank(REQ_DATA, false);

	if ("0000".equals(RES_DATA.get("RETURNCODE"))) {
            savePaymentOrderHistory(orderId, retMap.get("TID").toString());
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
        Map REQ_DATA = new HashMap();
	Map RES_DATA = new HashMap();
	REQ_DATA.put("TID", paymentCancelRequest.getId()); 
	REQ_DATA.put("CANCELTYPE", "C");
  	REQ_DATA.put("AMOUNT", "100");
	REQ_DATA.put("CANCELREQUESTER", "Requester Name");
	REQ_DATA.put("CANCELDESC", "Duplicated transaction.");
	REQ_DATA.put("TXTYPE", "CANCEL");
	REQ_DATA.put("SERVICETYPE", "WIRETRANSFER");

	RES_DATA = DanalFunction.getInstance().CallDanalBank(REQ_DATA, false);
	if( "0000".equals(RES_DATA.get("RETURNCODE")) ){
            return true;
	} else {
            return false;
	}
    }
    
}
