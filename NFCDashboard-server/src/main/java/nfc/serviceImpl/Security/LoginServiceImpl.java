package nfc.serviceImpl.Security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nfc.model.Role;
import nfc.model.UserRole;
import nfc.service.ILoginService;
import nfc.service.IRoleService;
import nfc.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//@Service("loginService")
@Service
public class LoginServiceImpl implements UserDetailsService{
	@Autowired
    private IUserService userDAO;
	@Autowired
	private IRoleService roleDA0;
	
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		nfc.model.User user = (nfc.model.User)userDAO.findUserByUserName(username);
		 if (user == null) {
	            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
	        } else {
	        	List<Role> roles = roleDA0.getListRoleByUserId(user.getUser_id());
	        	//List<UserRole> userRoles = userDAO.getListUserRole(user.getUser_id());
	            return JwtUserFactory.create(user, roles);
	        }
		//List<GrantedAuthority> authorities = buildUserAuthority(userDAO.getListUserRole(user.getUser_id()));
	    //return buildUserForAuthentication(user, authorities);
	}
	private List<GrantedAuthority> buildUserAuthority(List<UserRole> appUserRole) {
		System.out.println("Size userrole" + appUserRole.size());
        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
        // Build user's authorities
        for (UserRole userRole : appUserRole) {
        	System.out.println("Role" + userRole.getRole_id());
            setAuths.add(new SimpleGrantedAuthority(userRole.getRole_id() + ""));
        }
        List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);
        return Result;
    }
	private User buildUserForAuthentication(nfc.model.User user, List<GrantedAuthority> authorities) {
        return new User(user.getUser_name(), user.getPassword(), true, true, true, true, authorities);
    }
}
