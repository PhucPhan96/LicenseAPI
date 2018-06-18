/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.payment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import nfc.serviceImpl.common.SpeedPayInformation;
import nfc.serviceImpl.common.Utils;
import org.reflections.Reflections;

/**
 *
 * @author Admin
 */
public class PaymentFactory{
    
    private static final Logger log =  Logger.getLogger(PaymentFactory.class.toString());
    private static List<PaymentAbstract> listPayment = new ArrayList<>();
    
    static {
        Reflections reflections = new Reflections("nfc.serviceImpl.payment");
        Set<Class<? extends PaymentAbstract>> paymentAPIs = reflections.getSubTypesOf(PaymentAbstract.class);
        for(Class<? extends PaymentAbstract> paymentAPI : paymentAPIs)
        {
            try {
                listPayment.add(paymentAPI.newInstance());
            } catch (InstantiationException ex) {
                log.info(ex.getMessage());
            } catch (IllegalAccessException ex) {
                log.log(Level.SEVERE, ex.getMessage());
            }
        }
    }
    
    public static PaymentAbstract getPaymentApi(String paymentCode){
        for(PaymentAbstract paymentAPI: listPayment){
            if(paymentAPI.payment_code.equals(paymentCode)){
                return paymentAPI;
            }
        }
        return null;
    }
    
}
