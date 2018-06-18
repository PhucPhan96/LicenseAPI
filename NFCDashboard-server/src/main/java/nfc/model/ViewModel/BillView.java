/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model.ViewModel;

import java.util.List;
import nfc.messages.filters.BillRequestFilter;
import nfc.model.Order;

/**
 *
 * @author Admin
 */
public class BillView {
    
    List<Order> orders;
    BillRequestFilter billRequest;
    BillSupplierInformation billSupplierInformation;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public BillRequestFilter getBillRequest() {
        return billRequest;
    }

    public void setBillRequest(BillRequestFilter billRequest) {
        this.billRequest = billRequest;
    }

    public BillSupplierInformation getBillSupplierInformation() {
        return billSupplierInformation;
    }

    public void setBillSupplierInformation(BillSupplierInformation billSupplierInformation) {
        this.billSupplierInformation = billSupplierInformation;
    }
    
    
    
}
