/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.controller;

import java.util.List;
import nfc.model.Delivery;
import nfc.model.Payment;
import nfc.model.ViewModel.DeliveryView;
import nfc.model.ViewModel.PaymentView;
import nfc.service.IDeliveryService;
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
public class DeliveryManagementController {
    @Autowired
    private IDeliveryService deliveryService;
    
    @RequestMapping(value="deliveries",method=RequestMethod.GET)
    public List<Delivery> getPayments(){
        return deliveryService.getDeliveries();
    }
    @RequestMapping(value="delivery/view/{id}", method=RequestMethod.GET)
    public DeliveryView getDeliveryView(@PathVariable("id") int deliveryId){
        return deliveryService.getDeliveryView(deliveryId);
    }
    @RequestMapping(value="delivery/view/add", method=RequestMethod.POST)
    public @ResponseBody String insertDeliveryView(@RequestBody DeliveryView deliveryView){
        String data = deliveryService.insertDeliveryView(deliveryView) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value="delivery/view/update", method=RequestMethod.PUT)
    public @ResponseBody String editDeliveryView(@RequestBody DeliveryView deliveryView){
        String data = deliveryService.updateDeliveryView(deliveryView) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value="delivery/view/delete/{id}", method=RequestMethod.DELETE)
    public @ResponseBody String deleteDeliveryView(@PathVariable("id") int delvieryId){
        String data = deliveryService.deleteDeliveryView(delvieryId) + "";
        return "{\"result\":\"" + data + "\"}";
    }
}
