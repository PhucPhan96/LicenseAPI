/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model.ViewModel;

import java.util.List;
import nfc.model.Delivery;
import nfc.model.DeliveryMeta;

/**
 *
 * @author Admin
 */
public class DeliveryView {
    
    Delivery delivery;
    List<DeliveryMeta> deliveryMetas;

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public List<DeliveryMeta> getDeliveryMetas() {
        return deliveryMetas;
    }

    public void setDeliveryMetas(List<DeliveryMeta> deliveryMetas) {
        this.deliveryMetas = deliveryMetas;
    }
    
    
}
