package org.dixcord.security.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.dixcord.domain.UserVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class CustomUser extends User{
	
	private static final long serialVersionUID = 1L;
	
	private UserVO member;
	
	public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
	
	public CustomUser(UserVO vo) {
		super(vo.getUserEmail(), vo.getUserPassword(), Collections.singletonList(new SimpleGrantedAuthority(vo.getUserAuth())));
		
		this.member = vo;
	}

	
}
