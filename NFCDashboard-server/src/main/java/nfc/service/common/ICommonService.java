package nfc.service.common;

import java.util.Map;

import org.hibernate.Session;

public interface ICommonService {
	Object createObject(String className,Map<String, String> map);
	void deleteCode(Session session, String group_code, String sub_code);
}
