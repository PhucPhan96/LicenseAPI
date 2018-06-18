/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.integration.endpoint;

import nfc.model.ViewModel.OrderView;

/**
 *
 * @author Admin
 */
public class DeliveryServiceEndpoint {
    public OrderView delivery(OrderView orderView){
        return orderView;
    }
    public OrderView nonDelivery(OrderView orderView){
        return orderView;
    }
}
