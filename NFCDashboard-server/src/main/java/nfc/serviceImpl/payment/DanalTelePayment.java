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
public class DanalTelePayment extends PaymentAbstract{
    
    public DanalTelePayment(){
        this.payment_code = "DANALTELE";
    }
    
    
    public boolean payment(LinkedHashMap<String, String> paymentRequest, String orderId) {
	Map TransR = new HashMap();
	Map Res = null;
	Map Res2 = null;
	String ServerInfo = (String)paymentRequest.get("ServerInfo");
	int nConfirmOption = 1;
	TransR.put( "Command", "NCONFIRM" );
	TransR.put( "OUTPUTOPTION", "DEFAULT" );
	TransR.put( "ServerInfo", ServerInfo );
	TransR.put( "IFVERSION", "V1.1.2");
	TransR.put( "ConfirmOption", Integer.toString(nConfirmOption) );

	if( nConfirmOption == 1 )
	{
		TransR.put( "CPID", DanalFunction.ID );
		TransR.put( "AMOUNT", paymentRequest.get("AMOUNT") );
	}

	Res = DanalFunction.getInstance().CallTeledit( TransR );
	if( Res.get("Result").equals("0") )
	{
            TransR.clear();
            int nBillOption = 0;
            TransR.put( "Command", "NBILL" );
            TransR.put( "OUTPUTOPTION", "DEFAULT" );
            TransR.put( "ServerInfo", ServerInfo );
            TransR.put( "IFVERSION", "V1.1.2");
            TransR.put( "BillOption", Integer.toString(nBillOption) );
            Res2 = DanalFunction.getInstance().CallTeledit( TransR );
            if( Res2 == null || !Res2.get("Result").equals("0") )
            {
                return false;
            }
	}

	if( Res.get("Result").equals("0") && Res2.get("Result").equals("0") ){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean cancel(PaymentCancel paymentCancelRequest) {
        Map TransR = new HashMap();
	TransR.put( "ID", DanalFunction.ID);
	TransR.put( "PWD", DanalFunction.PWD);
	TransR.put( "TID", paymentCancelRequest.getId());
	TransR.put( "Command", "BILL_CANCEL");
	TransR.put( "OUTPUTOPTION", "3" );
	Map Res = DanalFunction.getInstance().CallTeledit(TransR);
	if( Res.get("Result").equals("0") ){
		return true;
	}
	else{
		return false;
	}
    }
    
}
