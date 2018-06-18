package nfc.service;
import java.util.List;

import org.hibernate.Session;

import nfc.model.Role;

public interface IRoleService {
	List<Role> getListRole();
	boolean insertRole(Role role);
	Role getRole(String roleId);
	boolean updateRole(Role role);
	boolean deleteRole(String roleId);
	List<Role> getListRoleByUserId(String userId);
	List<Role> getListRoleOfUserPermission(String username);
	Role getRoleWithSeeion(Session session,String roleId);
}
