/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.integration.endpoint;

import nfc.model.Delivery;
import nfc.model.ViewModel.OrderView;
import nfc.service.IDeliveryService;
import nfc.serviceImpl.common.Utils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Admin
 */
public class DeliveryRouter {
    
    @Autowired
    private IDeliveryService deliveryService;
    
    public String routeDelivery(OrderView orderView) {
        String result = Utils.ORDER_FAILED; 
        if(orderView.getOrder().getOrder_status().trim().equals(Utils.ORDER_FAILED)){
            result = "outputChannel";
        }
        else{
            Delivery delivery = deliveryService.getDeliveryFromStore(orderView.getOrder().getSuppl_id());
            if(delivery != null && delivery.getDelivery_url().equals("#")){
                result = "nonDeliveryChannel";
            }
            else{
                result = "deliveryChannel";
            }
        }
        return result;	
    }
}
