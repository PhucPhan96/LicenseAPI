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
public class CustomerBasePacket extends BasePacket {

    protected String uuid;
	
	
    public CustomerBasePacket(String uuid) {
        this.uuid = uuid;
    }
	
	
    public String getUUID() {
        return this.uuid;
    }
	
	
    @Override
    public CustomerBasePacket clone() throws CloneNotSupportedException {
        CustomerBasePacket data = (CustomerBasePacket) super.clone();
        return data;
    }
	
    @Override
    public String toString(){
        return String.format("%s uuid:%s", this.getClass().getSimpleName(), uuid);
    }
	
}
