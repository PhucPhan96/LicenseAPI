/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.controller;

import java.util.List;
import nfc.messages.request.DiscountRequest;
import nfc.model.Discount;
import nfc.model.Payment;
import nfc.model.ViewModel.PaymentView;
import nfc.service.IDiscountService;
import nfc.service.IPaymentService;
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
public class DiscountManagementController {
    @Autowired
    private IDiscountService discountDAO;
    
    @RequestMapping(value="discounts", method=RequestMethod.POST)
    public @ResponseBody List<Discount> getListDiscount(@RequestBody DiscountRequest request){
        return discountDAO.getListDiscount(request);
    }
    
    @RequestMapping(value="discount/add", method=RequestMethod.POST)
    public @ResponseBody Discount insertDiscount(@RequestBody Discount discount){
        return  discountDAO.insertDiscount(discount);
    }
    @RequestMapping(value="discount/update", method=RequestMethod.PUT)
    public @ResponseBody String editDiscount(@RequestBody Discount discount){
        String data = discountDAO.updateDiscount(discount) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value="discount/delete/{id}", method=RequestMethod.DELETE)
    public @ResponseBody String deleteDiscount(@PathVariable("id") String discountId){
        String data = discountDAO.deleteDiscount(discountId) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value = "app/getdiscount/{id}", method = RequestMethod.GET)
    public Discount fGetDiscount(@PathVariable("id") String supplierId) {
        Discount discount = discountDAO.fgetDiscountByDate(supplierId);
        return discount;
    }
}
