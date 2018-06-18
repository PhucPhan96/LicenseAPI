package nfc.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import nfc.model.Role;
import nfc.service.IRoleService;
import nfc.serviceImpl.Security.JwtTokenUtil;
import nfc.serviceImpl.common.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class RoleManagementController {
    @Autowired
    private IRoleService roleDAO;
    @Value("Authorization")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @RequestMapping(value="roles",method=RequestMethod.GET)
    public List<Role> getRoles(){
        List<Role> roles = roleDAO.getListRole();
        return roles;
    }
    @RequestMapping(value="role/{id}", method=RequestMethod.GET)
    public Role getRole(@PathVariable("id") String roleId){
        Role role = roleDAO.getRole(roleId);
        return role;
    }
    @RequestMapping(value="role/add", method=RequestMethod.POST)
    public @ResponseBody String insertRole(@RequestBody Role role){
        role.setApp_id(Utils.appId);
        String data = roleDAO.insertRole(role) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value="role/edit", method=RequestMethod.PUT)
    public @ResponseBody String editRole(@RequestBody Role role){
        String data = roleDAO.updateRole(role) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value="role/delete/{id}", method=RequestMethod.DELETE)
    public @ResponseBody String deleteRole(@PathVariable("id") String roleId){
        System.out.println("Vao delete " + roleId);
        String data = roleDAO.deleteRole(roleId) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value="roles/permission",method=RequestMethod.GET)
    public List<Role> getRolesOfUser(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        List<Role> roles = roleDAO.getListRoleOfUserPermission(username);
        System.out.println("count role la "+ roles.size());
        return roles;
    }
}
