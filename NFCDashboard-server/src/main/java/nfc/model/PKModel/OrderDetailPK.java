/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model.PKModel;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Admin
 */
public class OrderDetailPK implements Serializable{
    
    private String order_id;
    private int prod_id;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getProd_id() {
        return prod_id;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.order_id);
        hash = 97 * hash + this.prod_id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OrderDetailPK other = (OrderDetailPK) obj;
        if (this.prod_id != other.prod_id) {
            return false;
        }
        if (!Objects.equals(this.order_id, other.order_id)) {
            return false;
        }
        return true;
    }
    
    
    
}
