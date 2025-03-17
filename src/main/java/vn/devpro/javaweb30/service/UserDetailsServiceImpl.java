package vn.devpro.javaweb30.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vn.devpro.javaweb30.model.User;

@Service
public class UserDetailsServiceImpl extends BaseService<User> implements UserDetailsService{
	
	@Override
	public Class<User> clazz() {
		// TODO Auto-generated method stub
		return User.class;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//Lay user trong tbl_user theo username (username tu form login)
		String sql = "SELECT * FROM tbl_user u WHERE u.username='"+username+"' AND u.status=1";
		User user = this.getEntityByNativeSQL(sql);
		if(user!=null) {
			//System.out.println("username: "+user.getUsername());
			//System.out.println("Password: "+user.getPassword());
			return user;
		}
		else return new User(); // UserDetails khong duoc null

	}

	
	
	
	
}
