package nfc.model.ViewModel;
import nfc.model.Address;
public class UserAddressView {
	private Address addressOfUser = new Address();
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
	public Address getAddressOfUser() {
		return addressOfUser;
	}
	public void setAddressOfUser(Address addressOfUser) {
		this.addressOfUser = addressOfUser;
	}
}


