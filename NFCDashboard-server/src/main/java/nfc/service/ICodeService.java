package nfc.service;

import java.util.List;

import nfc.model.Code;

public interface ICodeService {
	List<Code> getListCode(String groupCode);
	List<Code> getAllCode();
	Code getCode(String groupCode, String subCode);
	boolean insertCode(Code code);
	boolean deleteCode(String groupCode, String subCode);
	boolean updateCode(Code code);
}
