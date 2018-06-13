package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


	@Configuration
	@EnableWebSecurity
	public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	    @Autowired
	    private UserDetailsService userDetailsService;

	    @Bean
	    public BCryptPasswordEncoder bCryptPasswordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	     // 認可の設定
	            .authorizeRequests()
	                .antMatchers("/", "/user/registration", "/css/**").permitAll()
	                .anyRequest().authenticated()
	                .and()
	                //ログイン設定
	            .formLogin()
	                .loginPage("/user/login") // 認証処理のパス
	                .defaultSuccessUrl("/", true) // ログインフォームのパス
	                .usernameParameter("email")
	                .passwordParameter("password")
	                .permitAll()
	                .and()
	                //ログアウト設定
	            .logout()
	                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
	                .logoutSuccessUrl("/");
	    }

	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	    }
	
}
