/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.messages.base;

/**
 *
 * @author Admin
 */
public class DeliveryRequestPacket {
    
    private String cust_tel;
    private String reach_addr;
    private long price;

    
    public String getCust_tel() {
        return cust_tel;
    }

    public void setCust_tel(String cust_tel) {
        this.cust_tel = cust_tel;
    }

    public String getReach_addr() {
        return reach_addr;
    }

    public void setReach_addr(String reach_addr) {
        this.reach_addr = reach_addr;
    }

    
    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

}
