/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model.ViewModel;

import java.math.BigDecimal;

/**
 *
 * @author Admin
 */
public class VATReport {
    
    private BigDecimal prod_amt = BigDecimal.ZERO;
    private BigDecimal tax_amt = BigDecimal.ZERO;

    public BigDecimal getProd_amt() {
        return prod_amt;
    }

    public void setProd_amt(BigDecimal prod_amt) {
        this.prod_amt = prod_amt;
    }

    public BigDecimal getTax_amt() {
        return tax_amt;
    }

    public void setTax_amt(BigDecimal tax_amt) {
        this.tax_amt = tax_amt;
    }
    
    
}
