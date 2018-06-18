/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.messages.request;

import nfc.messages.base.DeliveryRequestPacket;

/**
 *
 * @author Admin
 */
public class Delivery82WaRequest extends DeliveryRequestPacket{
    
    private String reg_no="1112233333";
    private String reach_x;
    private String reach_y;
    private String reach_memo;
    private String order_memo;
    private int pay_method = 2;

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }


    public String getReach_x() {
        return reach_x;
    }

    public void setReach_x(String reach_x) {
        this.reach_x = reach_x;
    }

    public String getReach_y() {
        return reach_y;
    }

    public void setReach_y(String reach_y) {
        this.reach_y = reach_y;
    }

    public String getReach_memo() {
        return reach_memo;
    }

    public void setReach_memo(String reach_memo) {
        this.reach_memo = reach_memo;
    }

    public String getOrder_memo() {
        return order_memo;
    }

    public void setOrder_memo(String order_memo) {
        this.order_memo = order_memo;
    }

    public int getPay_method() {
        return pay_method;
    }

    public void setPay_method(int pay_method) {
        this.pay_method = pay_method;
    }
    
    

}
