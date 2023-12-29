package com.practice.configure;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // IOC 등록
@EnableWebSecurity // 해당 파일로 시큐리티 활성화
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(PathRequest.toStaticResources().atCommonLocations().toString());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable(); // CSRF : 데이터를 변조해서 CRUD를 막기 위함

        httpSecurity.authorizeRequests()
                .antMatchers("/", "/user/**", "/attachment/**", "/memberFollow/**", "/comment/**")
                .authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/auth/sign-in") // GET
                .loginProcessingUrl("/auth/sign-in") // POST ( Spring에서 낚아채서 로그인 Process 진행한다 )
                .defaultSuccessUrl("/");

        return httpSecurity.build();
    }
}
