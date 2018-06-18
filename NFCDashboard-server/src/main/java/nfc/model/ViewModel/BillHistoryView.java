/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model.ViewModel;

import java.util.ArrayList;
import java.util.List;
import nfc.model.Product;



/**
 *
 * @author Admin
 */
public class BillHistoryView {
    private List<ProductOptionalBH> listProductOptions = new ArrayList<>();
    private BillHistory billHistory = new BillHistory();

    public List<ProductOptionalBH> getListProductOptions() {
        return listProductOptions;
    }

    public void setListProductOptions(List<ProductOptionalBH> listProductOptions) {
        this.listProductOptions = listProductOptions;
    }

    public BillHistory getBillHistory() {
        return billHistory;
    }

    public void setBillHistory(BillHistory billHistory) {
        this.billHistory = billHistory;
    }
    
}
