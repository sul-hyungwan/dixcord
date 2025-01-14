package org.dixcord.security;

import org.dixcord.domain.UserVO;
import org.dixcord.mapper.UserMapper;
import org.dixcord.security.domain.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import lombok.extern.log4j.Log4j;

@Log4j
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.warn("Loading user by username: " + username);

        // Base64 처리 (필요시 설명 추가)
        username = username.replace("=", "");

        log.warn("Processed username: " + username);

        UserVO user;

        if (username.contains("@")) {
            // Email 로그인 처리
            user = mapper.read(username);
        } else {
            // Kakao ID 로그인 처리
            user = mapper.readByKakaoId(username);
        }

        if (user == null) {
            log.error("User not found: " + username);
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return new CustomUser(user);
    }
}
