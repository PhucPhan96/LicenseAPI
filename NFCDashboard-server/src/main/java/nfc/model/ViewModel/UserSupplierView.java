/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model.ViewModel;

import nfc.model.User;

/**
 *
 * @author Admin
 */
public class UserSupplierView {
    User user;
    SupplierView supplierView;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SupplierView getSupplierView() {
        return supplierView;
    }

    public void setSupplierView(SupplierView supplierView) {
        this.supplierView = supplierView;
    }
    
}
