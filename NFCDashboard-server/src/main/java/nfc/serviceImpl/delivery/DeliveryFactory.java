/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.delivery;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import nfc.serviceImpl.payment.PaymentFactory;
import org.reflections.Reflections;

/**
 *
 * @author Admin
 */
public class DeliveryFactory {
    private static final Logger log =  Logger.getLogger(PaymentFactory.class.toString());
    private static List<DeliveryAbstract> listDelivery = new ArrayList<>();
    
    static {
        Reflections reflections = new Reflections("nfc.serviceImpl.delivery");
        Set<Class<? extends DeliveryAbstract>> deliveryAPIs = reflections.getSubTypesOf(DeliveryAbstract.class);
        for(Class<? extends DeliveryAbstract> deliveryAPI : deliveryAPIs)
        {
            try {
                listDelivery.add(deliveryAPI.newInstance());
            } catch (InstantiationException ex) {
                log.info(ex.getMessage());
            } catch (IllegalAccessException ex) {
                log.log(Level.SEVERE, ex.getMessage());
            }
        }
    }
    
    public static DeliveryAbstract getDeliveryApi(String deliveryCode){
        System.err.println("deliveryCode " + deliveryCode);
        for(DeliveryAbstract deliveryAPI: listDelivery){
            if(deliveryAPI.deliveryCode.equals(deliveryCode)){
                return deliveryAPI;
            }
        }
        return null;
    }
}
