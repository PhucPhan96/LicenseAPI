package nfc.service;

import java.util.Set;

import nfc.model.UserRole;

public interface ILoginService {
	Object findUserByUsername(String username);
	Set<UserRole> getListUserRole(String username);
	
}
