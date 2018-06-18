package nfc.service;

import java.util.List;

import nfc.model.Program;
import nfc.model.ProgramRole;
import nfc.model.ViewModel.ProgramRoleSubmit;

public interface IProgramService {
	List<Program> getListProgram(String username);
	Program insertProgram(Program prog);
	boolean deleteProgram(String ProgId);
	boolean editProgram(Program prog);
	List<Program> getListProgramFull();
	List<ProgramRole> getListProgramRolebyRoleId(int roleId);
	boolean SaveProgRole(ProgramRoleSubmit progRole);
}
