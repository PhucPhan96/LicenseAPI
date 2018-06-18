/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nfc.model;
import java.util.Date;
/**
 *
 * @author Admin
 */
public class Filter {
    int[] supplierId;
    String fromDate;
    String toDate;
    String[] status;
    String address;
    String phone_num;

    public String[] getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public int[] getSupplierId() {
        return supplierId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }
}
