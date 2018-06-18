package nfc.serviceImpl.Security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import nfc.model.Role;
import nfc.model.User;
import nfc.model.UserRole;

public class JwtUserFactory {
	private JwtUserFactory() {
    }

    public static JwtUser create(User user, List<Role> roles) {
        return new JwtUser(
                user.getUser_id(),
                user.getUser_name(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getMiddle_name(),
                user.getEmail(),
                user.getPassword(),
                mapToGrantedAuthorities(roles),
                //user.getIs_active()==1?true:false,
                (user.getIs_active()==true) && (user.getIs_lockedout()==false),
                user.getLast_password_changed_date()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> authorities) {
    	Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
        // Build user's authorities
        for (Role role : authorities) {
            setAuths.add(new SimpleGrantedAuthority(role.getRole_name()));
        }
        List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);
        return Result;
        /*return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getRole_id()+""))
                .collect(Collectors.toList());*/
    }
}
