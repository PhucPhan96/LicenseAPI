package nfc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import nfc.model.Code;
import nfc.model.Role;
import nfc.service.ICodeService;
import nfc.serviceImpl.common.Utils;

@RestController
public class CodeManagementController {
	@Autowired
	private ICodeService codeDAO;
	
	@RequestMapping(value="codes", method=RequestMethod.GET)
	public List<Code> getAllCode(){
		return codeDAO.getAllCode();
	}
        @RequestMapping(value="codes/group/{groupCode}", method=RequestMethod.GET)
	public List<Code> getListCodeFromGroupCode(@PathVariable("groupCode") String groupCode){
            System.out.println("List Code "+ codeDAO.getListCode(groupCode) );
		return codeDAO.getListCode(groupCode);
	}
	@RequestMapping(value="code/{groupCode}/{subCode}", method=RequestMethod.GET)
	public Code getCode(@PathVariable("groupCode") String groupCode, @PathVariable("subCode") String subCode){
		System.out.println("GroupCode "+ groupCode + " SubCode " + subCode);
		Code code = codeDAO.getCode(groupCode, subCode);
		if(code == null)
			return new Code();
		return code;
	}
	@RequestMapping(value="code/add", method=RequestMethod.POST)
	public @ResponseBody String insertCode(@RequestBody Code code){
		String data = codeDAO.insertCode(code) + "";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="code/edit", method=RequestMethod.PUT)
	public @ResponseBody String editRole(@RequestBody Code code){
		String data = codeDAO.updateCode(code) + "";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="code/delete/{groupCode}/{subCode}", method=RequestMethod.DELETE)
	public String deleteCode(@PathVariable("groupCode") String groupCode, @PathVariable("subCode") String subCode){
		String data = codeDAO.deleteCode(groupCode, subCode)+ "";
		return "{\"result\":\"" + data + "\"}";
	}
        @RequestMapping(value="app/codes/group/{groupCode}", method=RequestMethod.GET)
	public List<Code> getListCodeByGroupCode(@PathVariable("groupCode") String groupCode){
            System.out.println("List Code "+ codeDAO.getListCode(groupCode) );
		return codeDAO.getListCode(groupCode);
	}
}
