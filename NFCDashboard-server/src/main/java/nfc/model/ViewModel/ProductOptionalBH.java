/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model.ViewModel;

import nfc.model.Product;

/**
 *
 * @author Admin
 */
public class ProductOptionalBH {
    private Product product = new Product();
    private String optionalQuanlity;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getOptionalQuanlity() {
        return optionalQuanlity;
    }

    public void setOptionalQuanlity(String optionalQuanlity) {
        this.optionalQuanlity = optionalQuanlity;
    }
}
