/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model.ViewModel;

import java.util.List;
import nfc.model.Payment;
import nfc.model.PaymentMeta;

/**
 *
 * @author Admin
 */
public class PaymentView {
    
    Payment payment;
    List<PaymentMeta> paymentMetas;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public List<PaymentMeta> getPaymentMetas() {
        return paymentMetas;
    }

    public void setPaymentMetas(List<PaymentMeta> paymentMetas) {
        this.paymentMetas = paymentMetas;
    }
    
}
