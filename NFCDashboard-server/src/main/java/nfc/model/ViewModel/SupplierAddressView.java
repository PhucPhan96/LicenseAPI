package nfc.model.ViewModel;

import nfc.model.Address;

public class SupplierAddressView {
	private Address addressOfSuppl = new Address();
	private boolean is_main;
	private boolean is_deliver;
	public boolean isIs_main() {
		return is_main;
	}
	public void setIs_main(boolean is_main) {
		this.is_main = is_main;
	}
	public boolean isIs_deliver() {
		return is_deliver;
	}
	public void setIs_deliver(boolean is_deliver) {
		this.is_deliver = is_deliver;
	}
	public Address getAddressOfSuppl() {
		return addressOfSuppl;
	}
	public void setAddressOfSuppl(Address addressOfSuppl) {
		this.addressOfSuppl = addressOfSuppl;
	}
}
