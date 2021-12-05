package com.jsw.app.snackbat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .headers().frameOptions().sameOrigin()
            .and()
            .formLogin().defaultSuccessUrl("/")
            .and()
            .authorizeRequests().antMatchers("/chat/**").hasRole("USER")
            .anyRequest().permitAll();
    }

    /*
    *   Test용
    */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = passwordEncoder();
        String defaultPassword = passwordEncoder.encode("1234");

        auth.inMemoryAuthentication()
            .withUser("abc").password(defaultPassword).roles("USER")
            .and()
            .withUser("qwe").password(defaultPassword).roles("USER")
            .and()
            .withUser("zxc").password(defaultPassword).roles("GUEST");
    }

}
