/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.service;

import java.util.List;
import nfc.model.Payment;
import nfc.model.ViewModel.PaymentView;
import org.json.simple.JSONObject;

/**
 *
 * @author Admin
 */
public interface IPaymentService {
    List<Payment> getPayments();
    PaymentView getPaymentView(int paymentId);
    boolean insertPaymentView(PaymentView paymentView);
    boolean updatePaymentView(PaymentView paymentView);
    boolean deletePaymentView(int paymentId);
    boolean updatePaymentDefault(int paymentId);
}
