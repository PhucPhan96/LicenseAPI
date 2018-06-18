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

/**
 *
 * @author Admin
 */
public class DanalVirtualAccountPayment extends PaymentAbstract{
    
    public DanalVirtualAccountPayment(){
        this.payment_code = "DANALVIRTUALACCOUNT";
    }

    public boolean payment(LinkedHashMap<String, String> paymentRequest, String orderId) {
        String RES_STR = DanalFunction.getInstance().toDecrypt((String) paymentRequest.get("RETURNPARAMS"), DanalFunction.IV_VIRTUAL_ACCOUNT, DanalFunction.CRYPTOKEY_VIRTUAL_ACCOUNT);
	Map retMap = DanalFunction.getInstance().str2data(RES_STR);

	String returnCode = (String) retMap.get("RETURNCODE");
	String returnMsg = (String) retMap.get("RETURNMSG");
	if (returnCode == null || !"0000".equals(returnCode)) {
		// returnCode가 없거나 또는 그 결과가 성공이 아니라면 발급요청을 하지 않아야 함.
		System.out.println("Authentication failed. " + returnMsg + "[" + returnCode + "]");
		return false;
	}
	Map REQ_DATA = new HashMap();
	Map RES_DATA = new HashMap();
	REQ_DATA.put("TID", (String) retMap.get("TID"));
	REQ_DATA.put("AMOUNT", retMap.get("AMOUNT"));
	REQ_DATA.put("TXTYPE", "ISSUEVACCOUNT");
	REQ_DATA.put("SERVICETYPE", "DANALVACCOUNT");

	RES_DATA = DanalFunction.getInstance().CallVAccount(REQ_DATA, false);

	if ("0000".equals(RES_DATA.get("RETURNCODE"))) {
            
            //save data transfer here
            return true;
        }
        else{
            return false;
        }
    }

    public boolean cancel(PaymentCancel paymentCancelRequest) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
