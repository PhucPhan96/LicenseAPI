/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.service;

import java.util.List;
import nfc.model.Delivery;
import nfc.model.ViewModel.DeliveryView;

/**
 *
 * @author Admin
 */
public interface IDeliveryService {
    List<Delivery> getDeliveries();
    DeliveryView getDeliveryView(int deliveryId);
    boolean insertDeliveryView(DeliveryView deliveryView);
    boolean updateDeliveryView(DeliveryView deliveryView);
    boolean deleteDeliveryView(int deliveryId);
    Delivery getDeliveryFromStore(int storeId);
}
