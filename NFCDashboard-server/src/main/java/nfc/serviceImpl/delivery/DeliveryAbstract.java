/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.delivery;

import nfc.messages.base.DeliveryRequestPacket;
import nfc.messages.base.PaymentRequestPacket;
import org.json.simple.JSONObject;

/**
 *
 * @author Admin
 */
public abstract class DeliveryAbstract {
    protected String deliveryCode = "DELIVERIED";
    public abstract JSONObject order(DeliveryRequestPacket deliveryRequest);
}
