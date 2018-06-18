package nfc.model.ViewModel;

import java.util.ArrayList;
import java.util.List;

import nfc.model.ProgramRole;
import nfc.model.Thread;

public class ProgramRoleSubmit {
private int role_id =-1;
private List<ProgramRole> listProgRole = new ArrayList<ProgramRole>();;
public int getRole_id() {
	return role_id;
}
public void setRole_id(int role_id) {
	this.role_id = role_id;
}
public List<ProgramRole> getListProgRole() {
	return listProgRole;
}
public void setListProgRole(List<ProgramRole> listProgRole) {
	this.listProgRole = listProgRole;
}
}
