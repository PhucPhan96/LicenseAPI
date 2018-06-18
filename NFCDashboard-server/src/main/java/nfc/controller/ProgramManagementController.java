package nfc.controller;

import java.util.Date;
import java.util.List;

import javax.jws.soap.SOAPBinding.Use;
import javax.servlet.http.HttpServletRequest;

import nfc.model.Category;
import nfc.model.Program;
import nfc.model.ProgramRole;
import nfc.model.ViewModel.ProgramRoleSubmit;
import nfc.service.IProgramService;
import nfc.serviceImpl.Security.JwtTokenUtil;
import nfc.serviceImpl.common.Utils;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProgramManagementController {
    
    @Value("Authorization")
    private String tokenHeader;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private IProgramService programDAO;

    @RequestMapping(value="program/user",method=RequestMethod.GET)
    public List<Program> getListProgramOfUserLogin(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        List<Program> programs = programDAO.getListProgram(username);
        return programs;
        //return Utils.convertObjectToJsonString(programs);
    }
    @RequestMapping(value="program/add", method=RequestMethod.POST)
    public @ResponseBody Program insertProgram(@RequestBody Program prog){
        prog.setApp_id(Utils.appId);
        return programDAO.insertProgram(prog);
    }
    @RequestMapping(value="program/delete/{id}", method=RequestMethod.DELETE)
    public @ResponseBody String deleteProgram(@PathVariable("id") String progID){
        String data = programDAO.deleteProgram(progID)+"";
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value="program/edit", method=RequestMethod.POST)
    public @ResponseBody String editProgram(@RequestBody Program prog){
        String data = programDAO.editProgram(prog)+"";
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value="programfull",method=RequestMethod.GET)
    public List<Program> getListProgramFull(HttpServletRequest request){		
        List<Program> programs = programDAO.getListProgramFull();
        return programs;
    }
    @RequestMapping(value="programRole",method=RequestMethod.POST)
    public List<ProgramRole> getListProgramRolebyRoleId(@RequestBody int roleId){
        List<ProgramRole> programs = programDAO.getListProgramRolebyRoleId(roleId);
        return programs;
        //return Utils.convertObjectToJsonString(programs);
    }

    @RequestMapping(value="SaveProgramRole",method=RequestMethod.POST)
    public @ResponseBody String SaveProgRole(@RequestBody ProgramRoleSubmit ProgRole){
        String data =programDAO.SaveProgRole(ProgRole)+"";
        return "{\"result\":\"" + data + "\"}";
    }
}
