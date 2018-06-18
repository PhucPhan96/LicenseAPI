/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.messages.base;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class BasePacket implements Serializable, Cloneable {
    
    protected PacketType pkt_type;

    public void setPktType(PacketType type) {
        this.pkt_type = type;
    }

    public PacketType getType() {
        return this.pkt_type;
    }

    @Override
    public BasePacket clone() throws CloneNotSupportedException {
        BasePacket data = (BasePacket) super.clone();
        return data;
    }

    public enum PacketType {
        ORDER_DATA,
        REQUEST_CANCEL_ORDER,
        CANCEL_ORDER,
        COMPLETE_ORDER,
        COOKING_ORDER

    }
}
