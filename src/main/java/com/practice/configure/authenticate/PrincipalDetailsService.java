package com.practice.configure.authenticate;

import com.practice.domain.entity.User;
import com.practice.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final AuthRepository repository;

    // Password check는 Spring security에서 자동으로 한다.
    // Return이 잘되면 자동으로 세션이 생성된다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername : " + username);

        if (StringUtils.isBlank(username))
            throw new UsernameNotFoundException("UsernameNotFoundException");

        Optional<User> optUser = repository.findByUsernameIgnoreCase(username);
        if (optUser.isPresent())
            return new PrincipalDetails(optUser.get());

        return null;
    }
}
