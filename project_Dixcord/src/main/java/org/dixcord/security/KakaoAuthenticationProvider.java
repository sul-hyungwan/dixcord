package org.dixcord.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class KakaoAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomUserDetailService userDetailsService; // @Autowired 추가

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String kakaoId = authentication.getName();

        UserDetails user = userDetailsService.loadUserByUsername(kakaoId);

        if (user == null) {
            throw new BadCredentialsException("Invalid Kakao ID");
        }

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }
    
    public void setUserDetailsService(CustomUserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

