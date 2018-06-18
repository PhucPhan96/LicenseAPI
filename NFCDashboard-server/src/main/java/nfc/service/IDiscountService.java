/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.service;

import java.util.List;
import nfc.messages.request.DiscountRequest;
import nfc.model.Discount;

/**
 *
 * @author Admin
 */
public interface IDiscountService {
    List<Discount> getListDiscount(DiscountRequest discountRequest);
    Discount insertDiscount(Discount discount);
    boolean updateDiscount(Discount discount);
    boolean deleteDiscount(String discountId);
    Discount getDiscountOfStore(int supplierId);
    public Discount fgetDiscountByDate(String supplierId);
}
