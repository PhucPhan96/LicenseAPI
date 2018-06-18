package nfc.service;

import java.util.List;

import nfc.model.Group;
import nfc.model.Role;

public interface IGroupService {
	List<Group> getListGroup();
	boolean insertGroup(Group group);
	Group getGroup(String groupId);
	boolean updateGroup(Group group);
	boolean deleteGroup(String groupId);
}
