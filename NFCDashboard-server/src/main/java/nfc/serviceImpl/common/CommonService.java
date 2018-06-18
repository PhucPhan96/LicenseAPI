package nfc.serviceImpl.common;

import java.lang.reflect.Field;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import nfc.service.common.ICommonService;

public class CommonService implements ICommonService {
	public Object createObject(String className,Map<String, String> map){
		try {
			Class c=Class.forName(className);
			Object instance = c.newInstance();
			for (Map.Entry<String, String> entry : map.entrySet()) {
			    String key = entry.getKey();
			    Object value = entry.getValue();
			    Field field = c.getDeclaredField(key);
	            field.setAccessible(true);
	            field.set(instance,value);
	            field.setAccessible(false);
			}
			return instance;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public void deleteCode(Session session, String group_code, String sub_code)
	{
		String deleteQuery = "delete from fg_codes where group_code = \""+group_code+"\" AND sub_code = \""+sub_code+"\"";
		Query query = session.createSQLQuery(deleteQuery);
	    query.executeUpdate();
	}
}
