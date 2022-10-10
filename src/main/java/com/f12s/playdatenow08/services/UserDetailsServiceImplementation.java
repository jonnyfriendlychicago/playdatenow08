package com.f12s.playdatenow08.services;

//public class UserDetailsServiceImplementation {
//}

import java.util.ArrayList;
import java.util.List;
import com.f12s.playdatenow08.models.RoleMdl;
import com.f12s.playdatenow08.models.UserMdl;
import com.f12s.playdatenow08.repositories.UserRpo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    //	private UserRpo userRpo;
//    public UserDetailsServiceImplementation(UserRpo userRpo){this.userRpo = userRpo;}
    // above replaced by below: make below an autowire, after everything working
    @Autowired
    UserRpo userRpo;

    @Override // loadUserByUsername is a misleading name here because we are using email for login credentials

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserMdl userMdl = userRpo.findByEmail(email);

        if(userMdl == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(userMdl.getEmail(), userMdl.getPassword(), getAuthorities(userMdl));
    }

    private List<GrantedAuthority> getAuthorities(UserMdl userMdl){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for(RoleMdl roleMdl : userMdl.getRoleMdl()) {
            // JRF role above shoudl be renamed roleMdl for consistency, back to this later
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(roleMdl.getName());
            authorities.add(grantedAuthority);
        }
        return authorities;
    }


}
