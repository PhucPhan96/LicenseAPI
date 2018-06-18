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
public class StoreBasePacket  extends BasePacket {
	
	protected String user_id;
	
	public StoreBasePacket(String user_id) {
		this.user_id = user_id;
	}
	
	public String getUserId(){
		return this.user_id;
	}
	
	
	@Override
	public StoreBasePacket clone() throws CloneNotSupportedException {
            StoreBasePacket data = (StoreBasePacket) super.clone();
            return data;
	}
	
	@Override
	public String toString(){
		return String.format("%s user_id:%s", this.getClass().getSimpleName(), user_id);
	}
}
